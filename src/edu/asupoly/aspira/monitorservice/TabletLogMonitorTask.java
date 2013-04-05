package edu.asupoly.aspira.monitorservice;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This task should wake up and check for event log
 * from tablet application and push the logs to remote database
 */
public class TabletLogMonitorTask extends AspiraTimerTask {

    private Date __lastRead;
    private Properties __props;
    
    public TabletLogMonitorTask() {
        super();
        // is there a property to read here?
    }

    public void run() {
        if (_isInitialized) {
            try {
                // Read Config file to get Event log path 
                String tabletlog = __props.getProperty("tabletlogfile");
                File logfile = new File(tabletlog);
                if(!(logfile.exists()))
                {
                    logfile.createNewFile();
                    String logtxt = "There are no user interaction to log";
                     FileWriter fw = new FileWriter(logfile);
                    fw.write(logtxt);
                    fw.close();
                }
                //
                // Push log file to Remote Database 
                // Please note :
                // Remote Database URL can found using __props.getProperty("URL")
                //
            } catch (Throwable t) {
                Logger.getLogger(TabletLogMonitorTask.class.getName()).log(Level.SEVERE, null, t);
            }
        }
    }

    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        // check we have deviceId, patientId, and file
        __props = new Properties();
        String patientId = p.getProperty("patientid");
        String logfile   = p.getProperty("tabletlogfile");
        String url = p.getProperty("URL");
        if  (patientId != null && logfile != null) {
            __props.setProperty("patientid", patientId);
            __props.setProperty("tabletlogfile", logfile);
            __props.setProperty("URL", url);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        return rval;
    }
 
}
