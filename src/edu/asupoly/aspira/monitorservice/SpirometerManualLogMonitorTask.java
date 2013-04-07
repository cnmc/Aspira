package edu.asupoly.aspira.monitorservice;

import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.dmp.devicelogs.SpirometerTextLogParser;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.SpirometerTextReadingFactory;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This task should wake up and read the spirometer readings manually entered by the user logs since the
 * last time we read them - so we have to store the last reading or timestamp
 * XXX
 */
public class SpirometerManualLogMonitorTask extends AspiraTimerTask {

    private Date __lastRead;
    private Properties __props;
    
    public SpirometerManualLogMonitorTask() {
        super();
        // is there a property to read here?
        // Hardwire the initial last date to something in the past
        __lastRead = new Date(0L); // Jan 1 1970, 00:00:00
    }

    public void run() {
        if (_isInitialized) {
            try {
                System.out.println("Executing Spirometer Manual Log Timer Task!");
                SpirometerTextReadingFactory strf = new SpirometerTextLogParser();  // need to property-ize
                SpirometerReadings spr = strf.createSpirometerTextReadings(__props);
                // need to push these to DAO, but in the simple way we are reading in the whole
                // logfile so we have to not push those that come after lastReading
                if (spr != null) {
                    SpirometerReadings sprAfter = spr.getSpirometerReadingsAfter(__lastRead);
                    if (sprAfter == null) {
                        System.out.println("No Spirometer Readings after " + __lastRead);
                    } else {
                        System.out.println("Readings after " + __lastRead + " " + sprAfter.size());
                        SpirometerReading sp = sprAfter.getLastReading();
                        __lastRead = sp.getMeasureDate();
                    }
                    // Now we need to call DAOManager to get DAO
                    IAspiraDAO dao = AspiraDAO.getDAO();
                    dao.importSpirometerReadings(sprAfter, true); // return a boolean if we need it
                    // XXX Log how many we imported here
                }
            } catch (Throwable t) {
                Logger.getLogger(SpirometerManualLogMonitorTask.class.getName()).log(Level.SEVERE, null, t);
            }
        }
    }

    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        // check we have deviceId, patientId, and file
        __props = new Properties();
        String deviceId  = p.getProperty("deviceid");
        String patientId = p.getProperty("patientid");
        String logfile   = p.getProperty("sptxtlogfile");
        if (deviceId != null && patientId != null && logfile != null) {
            __props.setProperty("deviceid", deviceId);
            __props.setProperty("patientid", patientId);
            __props.setProperty("sptxtlogfile", logfile);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        return rval;
    }
}
