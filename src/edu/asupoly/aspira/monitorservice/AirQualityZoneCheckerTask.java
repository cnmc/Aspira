package edu.asupoly.aspira.monitorservice;

import edu.asupoly.aspira.AspiraSettings;
import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.ParticleReading;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * This task should wake up and read the air quality reading since the
 * last time we read them - and accordingly update air quality status and zone
 */
public class AirQualityZoneCheckerTask extends AspiraTimerTask {

    private static enum Zones { UNKNOWN, GREEN, YELLOW, RED };
    private static final String[] ZoneDescriptors = { "unknown", "green", "yellow", "red" };
    
    private Zones   __zone;
    private boolean __connected;
    private Date   __lastRead;
    private long   __yellowZoneThreshold;
    private long   __redZoneThreshold;
    private int    __numReadings;
    private String __patientId;
    private String __airQualityStatusFile;

    public AirQualityZoneCheckerTask() {
        super();
        // is there a property to read here?
        // Hardwire the initial last date to something in the past
        __lastRead = new Date(0L); // Jan 1 1970, 00:00:00
    }

    public void run() {
        if (_isInitialized) {
            AspiraSettings.getAspiraLogger().log(Level.INFO, "Monitoring Service: running AQ Zone Checker Task");
            try {
                //System.out.println("Executing  Air Quality Zone Checker Timer Task!");           
                // Now we need to call DAOManager to get DAO
                IAspiraDAO dao = AspiraDAO.getDAO();
                AirQualityReadings  aqr = dao.findAirQualityReadingsForPatientTail(__patientId, __numReadings);

                ParticleReading _pr = aqr.getLastReading();               
                if(_pr == null || _pr.getDateTime().equals(__lastRead)) {
                    // If no new reading that means something is wrong with DylosLogger
                    __connected = false;
                    __zone = Zones.UNKNOWN;
                } else {
                    __connected = true;
                    __lastRead = _pr.getDateTime();
                    Iterator<ParticleReading> iterator = aqr.iterator();
                    Zones z = Zones.RED;
                    while (iterator.hasNext() && z != Zones.GREEN) {
                        _pr = iterator.next();
                        if (_pr.getSmallParticleCount() < __yellowZoneThreshold) {
                            z = Zones.GREEN;
                            AspiraSettings.getAspiraLogger().log(Level.INFO, "AQ Zone GREEN");
                        } else if (_pr.getSmallParticleCount() < __redZoneThreshold) {
                            z = Zones.YELLOW;
                            AspiraSettings.getAspiraLogger().log(Level.INFO, "AQ Zone YELLOW");
                        }
                    }
                    __zone = z;
                }                
                editAirQualityStatus();
            }
            catch (Throwable t) {
                AspiraSettings.getAspiraLogger().log(Level.SEVERE, null, t);
            }
        }
    }

    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        try {
            // check we have deviceId, patientId, and file
            //String patientId = p.getProperty("patientid");
            String patientId = AspiraSettings.getPatientId();
            String pollingTime = p.getProperty("airQualityNumReadings");
            String yellowThreshold = p.getProperty("yellowZoneThreshhold");
            String redThreshold = p.getProperty("redZoneThreshhold");
            String statusfile  = p.getProperty("airqualitystatusfile");

            if (patientId != null && pollingTime != null &&
                    yellowThreshold != null && redThreshold != null && statusfile != null) {
                // convert those needing conversion
                __patientId = patientId;
                __numReadings = new Integer(pollingTime);
                __yellowZoneThreshold = new Integer(yellowThreshold);
                __redZoneThreshold = new Integer(redThreshold);                
                __airQualityStatusFile = AspiraSettings.getAspiraHome() + statusfile;
            } else {
                rval = false;
            }
            _isInitialized = rval;
        } catch (Throwable t) {
            AspiraSettings.getAspiraLogger().log(Level.SEVERE, "Cannot parse Zone properties", t);
            _isInitialized = false;
        }
        return rval;
    }

    // XXX Not convinced this really works
    private void editAirQualityStatus() {
        FileReader fr = null;
        FileOutputStream fop = null;
        try {
            JSONParser parser = new JSONParser(); 
            fr = new FileReader(__airQualityStatusFile);
            Object obj = parser.parse(fr);
            JSONObject statusObject =  (JSONObject) obj;
            JSONObject airQualitystatus =  (JSONObject) statusObject.get("airQualityMeter");
            if (__connected) {
                airQualitystatus.put("isConnected","true");
            } else {
                airQualitystatus.put("isConnected","false");
            }
            airQualitystatus.put("readingZone", ZoneDescriptors[__zone.ordinal()]);
            fr.close();
            File file = new File(__airQualityStatusFile);
            fop = new FileOutputStream(file);
            String jsonString = statusObject.toString();
            if(jsonString!=null) {
                fop.write(jsonString.getBytes());
            }
            fop.flush();
            fop.close();
        }
        catch(Throwable th) {
            AspiraSettings.getAspiraLogger().log(Level.SEVERE, null, th);
        } finally {
            try {
                if (fr != null) fr.close();
                if (fop != null) fop.close();
            } catch (Throwable t2) {
                AspiraSettings.getAspiraLogger().log(Level.SEVERE, null, t2);
            }
        }
    }

}
