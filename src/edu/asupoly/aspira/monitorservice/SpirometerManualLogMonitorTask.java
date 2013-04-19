package edu.asupoly.aspira.monitorservice;

import edu.asupoly.aspira.Aspira;
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
 */
public class SpirometerManualLogMonitorTask extends AspiraTimerTask {

    private static final Logger LOGGER = Logger.getLogger(SpirometerManualLogMonitorTask.class.getName());
    private Properties __props;
    
    public SpirometerManualLogMonitorTask() {
        super();
    }

    public void run() {
        if (_isInitialized) {
            LOGGER.log(Level.INFO, "Monitoring Service: running Spirometer Manual Log Monitor Task");
            try {
                //System.out.println("Executing Spirometer Manual Log Timer Task!");
                SpirometerTextReadingFactory strf = new SpirometerTextLogParser();  // need to property-ize
                SpirometerReadings spr = strf.createSpirometerTextReadings(__props);
                // need to push these to DAO, but in the simple way we are reading in the whole
                // logfile so we have to not push those that come after lastReading
                if (spr != null) {
                    SpirometerReadings sprAfter = spr.getSpirometerReadingsAfter(_lastRead, false);
                    if (sprAfter == null) {
                        LOGGER.log(Level.INFO, "Manual Spirometer Task: nothing to process");                                                
                    } else {
                        LOGGER.log(Level.INFO, "Manual Spirometer Task: processing records " + sprAfter.toString()); 
                        SpirometerReading sp = sprAfter.getLastReading();
                        if (sp != null) {
                            // Now we need to call DAOManager to get DAO
                            IAspiraDAO dao = AspiraDAO.getDAO();
                            dao.importSpirometerReadings(sprAfter, true); // return a boolean if we need it
                            _lastRead = sp.getMeasureDate();
                        }
                    }
                }
            } catch (Throwable t) {
                LOGGER.log(Level.SEVERE, null, t);
            }
        }
    }

    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        // check we have deviceId, patientId, and file
        __props = new Properties();
        //String deviceId  = p.getProperty("deviceid");
        //String patientId = p.getProperty("patientid");
        String deviceId  = Aspira.getSpirometerId();
        String patientId = Aspira.getPatientId();
        String logfile   = p.getProperty("sptxtlogfile");
        if (deviceId != null && patientId != null && logfile != null) {
            __props.setProperty("deviceid", deviceId);
            __props.setProperty("patientid", patientId);
            __props.setProperty("sptxtlogfile", logfile);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        
        // This section tries to initialize the last reading date
        _lastRead = new Date(0L);  // Jan 1 1970, 00:00:00
        try {
            IAspiraDAO dao = AspiraDAO.getDAO();
            SpirometerReadings sps = dao.findSpirometerReadingsForPatientTail(__props.getProperty("patientid"), 1);
            if (sps != null) {
                SpirometerReading e = sps.getFirstReading();
                if (e != null) {
                    _lastRead = e.getMeasureDate();
                }
            }
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Unable to get last reading time");
        }
        return rval;
    }
}
