package edu.asupoly.aspira.monitorservice;

import java.util.Date;
import java.util.Properties;

import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.dmp.devicelogs.UIEventLogParser;
import edu.asupoly.aspira.model.UIEvent;
import edu.asupoly.aspira.model.UIEvents;
import edu.asupoly.aspira.model.UIEventsFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This task should wake up and read the ui interaction logs since the
 * last time we read them - so we have to store the last event or timestamp
 * XXX
 */
public class UIInteractionMonitorTask extends AspiraTimerTask {

    private Date __lastRead;
    private Properties __props;
    
    public UIInteractionMonitorTask() {
        super();
        // is there a property to read here?
        // Hardwire the initial last date to something in the past
        __lastRead = new Date(0L); // Jan 1 1970, 00:00:00
    }

    public void run() {
        if (_isInitialized) {
            try {
                System.out.println("Executing UI Interaction Monitor Timer Task!");
                UIEventsFactory uief = new UIEventLogParser();  // need to property-ize
                UIEvents uie = uief.createUIEvents(__props);
                // need to push these to DAO, but in the simple way we are reading in the whole
                // logfile so we have to not push those that come after lastReading
                if (uie != null) {
                    UIEvents uieAfter = uie.getUIEventsAfter(__lastRead, false);
                    if (uieAfter == null) {
                        System.out.println("No UI Event  after " + __lastRead);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        uieAfter.addEvent(new UIEvent(__props.getProperty("patientid"), dateFormat.format(date), "There is no ui interaction to log"));
                    } else {
                        System.out.println("UI Event after " + __lastRead + " " + uieAfter.size());
                        UIEvent ue = uieAfter.getLastUIEvent();
                        __lastRead = ue.getDate();
                    }
                    // Now we need to call DAOManager to get DAO
                    IAspiraDAO dao = AspiraDAO.getDAO();
                    //NEED DAO API 
                    // XXX Log how many we imported here
                }
            } catch (Throwable t) {
                Logger.getLogger(UIInteractionMonitorTask.class.getName()).log(Level.SEVERE, null, t);
            }
        }
    }

    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        // check we have  patientId, and file
        __props = new Properties();
        String patientId = p.getProperty("patientid");
        String logfile   = p.getProperty("uilogfile");
        if (patientId != null && logfile != null) {
            __props.setProperty("patientid", patientId);
            __props.setProperty("uilogfile", logfile);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        return rval;
    }
}
