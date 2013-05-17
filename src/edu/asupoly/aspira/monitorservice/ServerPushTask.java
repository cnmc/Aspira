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
    
    public static final int SPIROMETER_READINGS_TYPE = 1;
    public static final int AIR_QUALITY_READINGS_TYPE = 2;
    public static final int UI_EVENTS_TYPE = 4;
    
    private static final Logger LOGGER = AspiraSettings.getAspiraLogger();
    
    private Properties __props;
    private String __pushURL;
    
    public ServerPushTask() {
        super();
    }

    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        
        __props = new Properties();  // need this even if not using here
        
        // all impls need to figure out if they need to push
        __pushURL = AspiraSettings.getPushURL();
        if (__pushURL == null || __pushURL.length() < 12) { // must be at least http://x.yyy
            rval = false;
        }
        
        _isInitialized = rval;
        
        // This section tries to initialize the last reading date
        _lastRead = new Date(0L);  // Jan 1 1970, 00:00:00
        try {
            IAspiraDAO dao = AspiraDAO.getDAO();
            ServerPushEvent spe = dao.getLastServerPush();
            if (spe != null) {                
                _lastRead = spe.getEventDate();
                LOGGER.log(Level.INFO, "Last server push " + _lastRead.toString());
            } else {
                LOGGER.log(Level.INFO, "Last server push unknown, using " + _lastRead.toString());
            }
        } catch (Throwable t) {            
            LOGGER.log(Level.WARNING, "Unable to get last server push time, using " + _lastRead.toString());
        }
        
        return _isInitialized;
    }

    @Override
    public void run() {
        if (_isInitialized) {
            LOGGER.log(Level.INFO, "MonitoringService: UIMonitor executing");
            Date d = new Date(System.currentTimeMillis());
            try {
                IAspiraDAO dao = AspiraDAO.getDAO();
                SpirometerReadings stoImport = dao.findSpirometerReadingsForPatient(__props.getProperty("patientid"), 
                        _lastRead, d);
                AirQualityReadings atoImport = dao.findAirQualityReadingsForPatient(__props.getProperty("patientid"),
                        _lastRead, d);
                UIEvents utoImport = dao.findUIEventsForPatient(__props.getProperty("patientid"),
                        _lastRead, d);
                
                int rval = 0;
                if (__pushURL != null) {
                    if (stoImport != null && stoImport.size() > 0) {                                
                        rval = __pushToServer(stoImport, "spirometerreadings");
                        __recordResult(dao, rval, "spirometer readings", d, SPIROMETER_READINGS_TYPE);
                    }
                    if (atoImport != null && atoImport.size() > 0) {                                
                        rval = __pushToServer(atoImport, "airqualityreadings");
                        __recordResult(dao, rval, "air quality readings", d, AIR_QUALITY_READINGS_TYPE);
                    }
                    if (utoImport != null && utoImport.size() > 0) {                                
                        rval = __pushToServer(utoImport, "uievents");
                        __recordResult(dao, rval, "UI events", d, UI_EVENTS_TYPE);
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
        LOGGER.log(Level.WARNING, msg);
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
                    rval = -101;
                }
            }
        } catch (MalformedURLException mue) {
            LOGGER.log(Level.SEVERE, "Malformed URL " + __pushURL+type);
            rval = -102;
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Error trying to connect to push server");
            rval = -100;
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
