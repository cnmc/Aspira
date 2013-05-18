package edu.asupoly.aspira.monitorservice;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.asupoly.aspira.AspiraSettings;
import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.DMPException;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.ServerPushEvent;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.UIEvents;

public class ServerPushTask extends AspiraTimerTask {
    public static final int PUSH_UNSET = 0;    
    public static final int PUSH_BAD_RESPONSE_CODE = -101;
    public static final int PUSH_MALFORMED_URL = -102;
    public static final int PUSH_UNABLE_TO_CONNECT = -100;
    public static final int SERVER_AQ_IMPORT_FAILED = -20;
    public static final int SERVER_NO_AQ_READINGS = -21;
    public static final int SERVER_SPIROMETER_IMPORT_FAILED = -30;
    public static final int SERVER_NO_SPIROMETER_READINGS = -31;
    public static final int SERVER_UIEVENT_IMPORT_FAILED = -40;
    public static final int SERVER_NO_UIEVENTS = -41;
    public static final int SERVER_STREAM_ERROR = -1;
    public static final int SERVER_BAD_OBJECT_TYPE = -2;
    public static final int SERVER_STREAM_CORRUPTED_EXCEPTION = -10;
    public static final int SERVER_IO_EXCEPTION = -11;
    public static final int SERVER_SECURITY_EXCEPTION = -12;
    public static final int SERVER_NULL_POINTER_EXCEPTION = -13;
    public static final int SERVER_UNKNOWN_ERROR = -99;
    
    public static final int SPIROMETER_READINGS_TYPE = 0;
    public static final int AIR_QUALITY_READINGS_TYPE = 1;
    public static final int UI_EVENTS_TYPE = 2;
    private static final String PUSH_URL_PROPERTY_KEY = "push.url";
    private static final Logger LOGGER = AspiraSettings.getAspiraLogger();
    
    private Properties __props;
    private String __pushURL;
    
    protected Date[] _lastRead;  // override of parent
    
    public ServerPushTask() {
        super();
    }

    /**
     * verifies URL form
     */
    private String __setURL(String url) {
        // figure the shortest possible valid URL is http://X.YYY
        String pushURL = null;
        if (url != null && url.trim().length() > 12) {
            pushURL = url;
            if (!pushURL.endsWith("/")) {  
                pushURL = url + "/";
            }
        }
        return pushURL;
    }
    
    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        
        __props = new Properties();  // need this even if not using here
        String deviceId  = AspiraSettings.getSpirometerId();
        String patientId = AspiraSettings.getPatientId();
        __pushURL = __setURL(p.getProperty(PUSH_URL_PROPERTY_KEY));
        if (deviceId != null && patientId != null && __pushURL != null || __pushURL.length() >= 12) { // must be at least http://x.yyy
            __props.setProperty("deviceid", deviceId);
            __props.setProperty("patientid", patientId);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        
        // This section tries to initialize the last reading date
        Date lastRead = new Date(0L);  // Jan 1 1970, 00:00:00
        _lastRead = new Date[3];
        try {
            IAspiraDAO dao = AspiraDAO.getDAO();
            ServerPushEvent spe = dao.getLastServerPush(SPIROMETER_READINGS_TYPE);
            if (spe != null) {                
                _lastRead[SPIROMETER_READINGS_TYPE] = spe.getEventDate();
                LOGGER.log(Level.INFO, "Last server push " + _lastRead.toString());
            } else {
                _lastRead[SPIROMETER_READINGS_TYPE] = lastRead;
                LOGGER.log(Level.INFO, "Last server push unknown, using " + lastRead.toString());
            }
            spe = dao.getLastServerPush(AIR_QUALITY_READINGS_TYPE);
            if (spe != null) {                
                _lastRead[AIR_QUALITY_READINGS_TYPE] = spe.getEventDate();
                LOGGER.log(Level.INFO, "Last server push " + _lastRead.toString());
            } else {
                _lastRead[AIR_QUALITY_READINGS_TYPE] = lastRead;
                LOGGER.log(Level.INFO, "Last server push unknown, using " + lastRead.toString());
            }
            spe = dao.getLastServerPush(UI_EVENTS_TYPE);
            if (spe != null) {                
                _lastRead[UI_EVENTS_TYPE] = spe.getEventDate();
                LOGGER.log(Level.INFO, "Last server push " + _lastRead.toString());
            } else {
                _lastRead[UI_EVENTS_TYPE] = lastRead;
                LOGGER.log(Level.INFO, "Last server push unknown, using " + lastRead.toString());
            }
        } catch (Throwable t) {
            t.printStackTrace();
            LOGGER.log(Level.WARNING, "Unable to get last server push time, using " + lastRead.toString());
        }
        
        return _isInitialized;
    }

    @Override
    public void run() {
        if (_isInitialized) {
            LOGGER.log(Level.INFO, "MonitoringService: ServerPushTask executing");
            Date d = new Date(System.currentTimeMillis());

            try {
                IAspiraDAO dao = AspiraDAO.getDAO();
                
                LOGGER.log(Level.INFO, "Checking for spirometer readings between " + 
                        _lastRead[SPIROMETER_READINGS_TYPE].toString() + " and " + d.toString());
                SpirometerReadings stoImport = dao.findSpirometerReadingsForPatient(__props.getProperty("patientid"), 
                        _lastRead[SPIROMETER_READINGS_TYPE], d);
                LOGGER.log(Level.INFO, "Checking for air quality readings between " + 
                        _lastRead[AIR_QUALITY_READINGS_TYPE].toString() + " and " + d.toString());
                AirQualityReadings atoImport = dao.findAirQualityReadingsForPatient(__props.getProperty("patientid"),
                        _lastRead[AIR_QUALITY_READINGS_TYPE], d);
                LOGGER.log(Level.INFO, "Checking for UI event between " + 
                        _lastRead[UI_EVENTS_TYPE].toString() + " and " + d.toString());
                UIEvents utoImport = dao.findUIEventsForPatient(__props.getProperty("patientid"),
                        _lastRead[UI_EVENTS_TYPE], d);
                
                int rval = 0;
                if (__pushURL != null) {
                    if (stoImport != null && stoImport.size() > 0) {                                
                        rval = __pushToServer(stoImport, "spirometerreadings");
                        __recordResult(dao, rval, "spirometer readings", d, SPIROMETER_READINGS_TYPE);
                    } else {
                        LOGGER.log(Level.INFO, " No Spirometer Readings to push");
                    }
                    if (atoImport != null && atoImport.size() > 0) {                                
                        rval = __pushToServer(atoImport, "airqualityreadings");
                        __recordResult(dao, rval, "air quality readings", d, AIR_QUALITY_READINGS_TYPE);
                    } else {
                        LOGGER.log(Level.INFO, " No Air Quality Readings to push");
                    }
                    if (utoImport != null && utoImport.size() > 0) {                                
                        rval = __pushToServer(utoImport, "uievents");
                        __recordResult(dao, rval, "UI events", d, UI_EVENTS_TYPE);
                    } else {
                        LOGGER.log(Level.INFO, " No UI Events to push");
                    }
                }
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Error pushing to the server " + t.getMessage());
            }
        }
    }
    
    private void __recordResult(IAspiraDAO dao, int rval, String label, Date d, int type) {
        String msg = "";
        if (rval >= 0) {
            msg = "Pushed " + rval + " " + label + " to the server";            
        } else {
            msg = "Unable to push " + label + " to the server";
        }
        LOGGER.log(Level.INFO, msg);
        _lastRead[type] = d;    // whether we are successful or not we update the date.
                                // otherwise this could happen over and over (say, UNIQUE constraint)
        try {
            dao.addPushEvent(new ServerPushEvent(d, rval, type, msg));
        } catch (Throwable ts) {
            LOGGER.log(Level.WARNING, "Unable to record " + label + " push event");
        }
    }
    
    private int __pushToServer(java.io.Serializable objects, String type) throws DMPException {
        HttpURLConnection urlConn = null;
        ObjectOutputStream oos = null;
        BufferedReader br = null;
        int rval = 0;
        try {
            LOGGER.log(Level.FINE, "Pushing to server " + __pushURL+type);
            urlConn = (HttpURLConnection) new URL(__pushURL+type).openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST");
            urlConn.connect();
            oos = new ObjectOutputStream(urlConn.getOutputStream());
            oos.writeObject(objects);
            oos.flush();
            oos.close();
            LOGGER.log(Level.FINE, "Push complete " + __pushURL+type);
            
            // Process the response
            if (urlConn.getResponseCode() != 200) {
                throw new DMPException("Did not receive OK from server for request");
            } else {
                // Get the return value
                br = new BufferedReader(new InputStreamReader(new DataInputStream (urlConn.getInputStream())));
                String str = br.readLine();
                try {
                    rval = Integer.parseInt(str);
                } catch (NumberFormatException nfe) {
                    LOGGER.log(Level.WARNING, "Unable to convert server response to return code");
                    rval = PUSH_BAD_RESPONSE_CODE;
                }
            }
        } catch (MalformedURLException mue) {
            LOGGER.log(Level.SEVERE, "Malformed URL " + __pushURL+type);
            rval = PUSH_MALFORMED_URL;
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Error trying to connect to push server");
            rval = PUSH_UNABLE_TO_CONNECT;
        } finally {
            try {
                if (br != null) br.close();
                if (oos != null) oos.close();
            } catch (Throwable t2) {
                LOGGER.log(Level.WARNING, "Unable to close Object Output Stream");
            }
        }
        __logReturnValue(rval);
        return rval;                
    }
    
    private void __logReturnValue(int rval) {
        // This is a total hack right now
        LOGGER.log(Level.INFO, "Return code from server push: " + rval);
        if (rval > 0) LOGGER.log(Level.INFO, "This is the number of elements pushed successfully");
        else if (rval == 0) LOGGER.log(Level.INFO, "Server did not think there was anything to push");
        else if (rval <= -100) LOGGER.log(Level.INFO, "Some error on the client prevented server push round trip");
        else if (rval <= -90) LOGGER.log(Level.INFO, "Server side servlet error");
        else if (rval <= -40) LOGGER.log(Level.INFO, "Could not push UI Events");
        else if (rval <= -30) LOGGER.log(Level.INFO, "Could not push Spirometer Readings");
        else if (rval <= -20) LOGGER.log(Level.INFO, "Could not push Air Quality Readings");
        else if (rval <= -10) LOGGER.log(Level.INFO, "Server encountered an exception");
        else if (rval < 0) LOGGER.log(Level.INFO, "Server encountered parameters it did not understand");
        
    }   
}
