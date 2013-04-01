package edu.asupoly.aspira.monitorservice;

import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import edu.asupoly.aspira.dmp.devicelogs.DylosLogParser;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.AirQualityReadingsFactory;
import edu.asupoly.aspira.model.ParticleReading;

/*
 * This task should wake up and read the air quality logs since the
 * last time we read them - so we have to store the last reading or timestamp
 * XXX
 */
public class AirQualityMonitorTask extends AspiraTimerTask {

    private Date __lastRead;
    private Properties __props;
    
    public AirQualityMonitorTask() {
        super();
        // is there a property to read here?
    }

    public void run() {
        if (_isInitialized) {
            try {
                AirQualityReadingsFactory aqrf = new DylosLogParser();  // need to property-ize
                AirQualityReadings aqr = aqrf.createAirQualityReadings(__props);
                // need to push these to DAO, but in the simple way we are reading in the whole
                // logfile so we have to not push those that come after lastReading
                if (aqr != null) {
                    AirQualityReadings aqrAfter = aqr.getAirQualityAfter(__lastRead, true);
                    // Now we need to call DAOManager to get DAO
                    // XXX then call importAirQualityReadings(aqrAfter, true);
                }
            } catch (Throwable t) {
                // XXX need some logging here
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
        String logfile   = p.getProperty("aqlogfile");
        if (deviceId != null && patientId != null && logfile != null) {
            __props.setProperty("deviceid", deviceId);
            __props.setProperty("patientid", patientId);
            __props.setProperty("aqlogfile", logfile);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        return rval;
    }
}
