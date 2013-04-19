package edu.asupoly.aspira.monitorservice;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.asupoly.aspira.Aspira;
import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.dmp.devicelogs.DylosLogParser;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.AirQualityReadingsFactory;
import edu.asupoly.aspira.model.ParticleReading;

/*
 * This task should wake up and read the air quality logs since the
 * last time we read them - so we have to store the last reading or timestamp
 */
public class AirQualityMonitorTask extends AspiraTimerTask {    
    
    private static final Logger LOGGER = Logger.getLogger(AirQualityMonitorTask.class.getName());
    
    private Properties __props;
    
    public AirQualityMonitorTask() {
        super();
    }

    public void run() {
        if (_isInitialized) {
            LOGGER.log(Level.INFO, "MonitorService: executing AQMonitor Task");
            try {
                // System.out.println("Executing timer task!");
                AirQualityReadingsFactory aqrf = new DylosLogParser();  // need to property-ize
                AirQualityReadings aqr = aqrf.createAirQualityReadings(__props);
                // need to push these to DAO, but in the simple way we are reading in the whole
                // logfile so we have to not push those that come after lastReading
                if (aqr != null) {
                    AirQualityReadings aqrAfter = aqr.getAirQualityAfter(_lastRead, false);
                    if (aqrAfter != null) {
                        ParticleReading pr = aqrAfter.getLastReading();
                        if (pr != null) {
                            _lastRead = pr.getDateTime();

                            // Now we need to call DAOManager to get DAO
                            IAspiraDAO dao = AspiraDAO.getDAO();
                            dao.importAirQualityReadings(aqrAfter, true); // return a boolean if we need it
                            // Log how many we imported here
                            LOGGER.log(Level.INFO, "Imported AQ Readings " + aqrAfter.size());
                        } else {
                            LOGGER.log(Level.INFO, "No last ParticleReading");
                        } 
                    } else {
                        LOGGER.log(Level.INFO, "No AQ Readings to import");
                    }
                } else {
                    LOGGER.log(Level.INFO, "No AQ logfile to parse");
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
        String deviceId  = Aspira.getAirQualityMonitorId();
        String patientId = Aspira.getPatientId();
        String logfile   = p.getProperty("aqlogfile");
        if (deviceId != null && patientId != null && logfile != null) {
            __props.setProperty("deviceid", deviceId);
            __props.setProperty("patientid", patientId);
            __props.setProperty("aqlogfile", logfile);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        
        // This section tries to initialize the last reading date
        _lastRead = new Date(0L);  // Jan 1 1970, 00:00:00
        try {
            IAspiraDAO dao = AspiraDAO.getDAO();
            AirQualityReadings aqr = dao.findAirQualityReadingsForPatientTail(__props.getProperty("patientid"), 1);
            if (aqr != null) {
                ParticleReading e = aqr.getFirstReading();
                if (e != null) {
                    _lastRead = e.getDateTime();
                }
            }
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Unable to get last reading time");
        }
        
        return rval;
    }
}
