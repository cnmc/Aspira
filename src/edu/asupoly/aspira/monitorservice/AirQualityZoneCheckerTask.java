package edu.asupoly.aspira.monitorservice;

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
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * This task should wake up and read the air quality reading since the
 * last time we read them - and accordingly update air quality status and zone
 * XXX
 */
public class AirQualityZoneCheckerTask extends AspiraTimerTask {

    private Date __lastRead;
    private Properties __props;
    private long yellowZoneThreshold;
    private long redZoneThreshold;
    private int no_of_readings;
    
    public AirQualityZoneCheckerTask() {
        super();
        // is there a property to read here?
        // Hardwire the initial last date to something in the past
        __lastRead = new Date(0L); // Jan 1 1970, 00:00:00
    }

    public void run() {
        if (_isInitialized) {
            try {
                    System.out.println("Executing  Air Quality Zone Checker Timer Task!");
                    String connected = "true";
                    String zone = "green";     
                    getZonesAndPollTime();       
                    // Now we need to call DAOManager to get DAO
                   IAspiraDAO dao = AspiraDAO.getDAO();
                    AirQualityReadings  aqr = null;
                    // Please change dao api, we want this api to return Air Quality Reading object i.e date, time, small particle value
                    //dao.importNAirQualityReadingsAfter(aqr, _lastRead, polltime); // return a boolean if we need it
                    
                    ParticleReading _pr = aqr.getLastReading();               
                    if(_pr.getDateTime().equals(__lastRead)) 
                    {
                        // If no new reading that means something is wrong with DylosLogger
                        connected = "false";
                        zone = "red";
                    }
                    else
                    {
                        int yellowcount = 0, redcount = 0, greencount =0;
                        __lastRead = _pr.getDateTime();
                       Iterator<ParticleReading> iterator = aqr.iterator();
                        while (iterator.hasNext()) {
                            _pr = iterator.next();
                            if (_pr.getSmallParticleCount() < yellowZoneThreshold) {
                                if(_pr.getSmallParticleCount() < redZoneThreshold)
                                    redcount++;
                                else
                                    yellowcount++;
                             }
                            else
                                greencount++;
                    }
                    if(greencount > yellowcount && greencount > redcount)
                            zone = "green";
                    else if(yellowcount > greencount && yellowcount > redcount)
                        zone = "yellow";
                    else
                        zone = "red";
                    } 
                    EditAirQualityStatus(connected, zone);
            }
            catch (Throwable t) {
                            Logger.getLogger(AirQualityZoneCheckerTask.class.getName()).log(Level.SEVERE, null, t);
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
        String config   = p.getProperty("configfile");
        String statusfile = p.getProperty("airqualitystatusfile");
        if (deviceId != null && patientId != null && config != null) {
            __props.setProperty("deviceid", deviceId);
            __props.setProperty("patientid", patientId);
            __props.setProperty("configfile", config);
            __props.setProperty("airqualitystatusfile", statusfile);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        return rval;
    }
    
    public void getZonesAndPollTime()
    {
           try
           {
                String configfile = __props.getProperty("configfile");
                JSONParser parser = new JSONParser();  
                Object obj = parser.parse(new FileReader(configfile));
                JSONObject configObject =  (JSONObject) obj;
                JSONObject config =  (JSONObject) configObject.get("config");
                JSONObject airQualityconfig = (JSONObject) config.get("airQualityConfig");
                long  pollingTime =  (Long) airQualityconfig.get("monitorServicePollingTime");
                yellowZoneThreshold = (Long) airQualityconfig.get("yellowZone");
                redZoneThreshold = (Long) airQualityconfig.get("redZone");
               
                // We get polltime in milliseconds, need to convert it into minutes      
               no_of_readings = (int) (pollingTime/60000);
                
                // Making sure that we are monitoring atleast one reading
                if(no_of_readings == 0) {
                   no_of_readings = 1;
               }
           }
           catch(Throwable th)
           {
                Logger.getLogger(AirQualityZoneCheckerTask.class.getName()).log(Level.SEVERE, null, th);
           }
    }
    
    public void EditAirQualityStatus(String connected, String zone)
    {
        try
        {
                String statusfile = __props.getProperty("airqualitystatusfile");
                JSONParser parser = new JSONParser();  
                Object obj = parser.parse(new FileReader(statusfile));
                JSONObject statusObject =  (JSONObject) obj;
                JSONObject airQualitystatus =  (JSONObject) statusObject.get("airQualityMeter");
                airQualitystatus.put("isConnected",connected);
                airQualitystatus.put("readingZone",zone);
                File file = new File(__props.getProperty("airqualitystatusfile"));
                FileOutputStream fop=new FileOutputStream(file);
                String jsonString = statusObject.toString();
                if(jsonString!=null) {
                    fop.write(jsonString.getBytes());
                }
                fop.flush();
                fop.close();
        }
    catch(Throwable th)
    {
            Logger.getLogger(AirQualityZoneCheckerTask.class.getName()).log(Level.SEVERE, null, th);
    }
  }
    
}
