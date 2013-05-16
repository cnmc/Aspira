/**
 * 
 */
package edu.asupoly.aspira.dmp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.asupoly.aspira.Aspira;
import edu.asupoly.aspira.model.AirQualityMonitor;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.Clinician;
import edu.asupoly.aspira.model.Patient;
import edu.asupoly.aspira.model.Spirometer;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.UIEvents;

/**
 * @author kevinagary
 *
 */
public final class AspiraDAO implements IAspiraDAO {
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
    
    private static String PROPERTY_FILENAME = "properties/dao.properties";
    private static String DAO_CLASS_PROPERTY_KEY = "daoClassName";
    private static final String PUSH_URL_PROPERTY_KEY = "push.url";
    private static final Logger LOGGER = Logger.getLogger(AspiraDAO.class.getName());
    
    private static AspiraDAO  __singletonDAOWrapper;
    private AspiraDAOBaseImpl __dao = null;
    private Properties        __daoProperties = null;
    private String            __pushURL = null;
    
    /**
     * Singleton accessor
     * @return
     * @throws DMPException
     */
    public static IAspiraDAO getDAO() throws DMPException {
        if (__singletonDAOWrapper == null) {
            __singletonDAOWrapper = new AspiraDAO();
        }
        return __singletonDAOWrapper;
    }
    
    /**
     * The wrapper constructor determines whether to push to a server URL,
     * and in the future can add any other interceptor-style behavior we wish
     * @throws DMPException if the DAO cannot be initialized
     */
    private AspiraDAO() throws DMPException {
        __daoProperties = new Properties();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(PROPERTY_FILENAME));
            __daoProperties.load(isr);
            // let's create a DAO based on a known property
            String daoClassName = __daoProperties.getProperty(DAO_CLASS_PROPERTY_KEY);
            Class<?> daoClass = Class.forName(daoClassName);
            __dao = (AspiraDAOBaseImpl)daoClass.newInstance();
            __dao.init(__daoProperties);
            
            __pushURL = Aspira.getPushURL();
        } catch (Throwable t1) {
            LOGGER.log(Level.SEVERE, "Throwable in constructor for AspiraDAO");
            //t1.printStackTrace();
            throw new DMPException(t1);
        }
        // all impls need to figure out if they need to push
        setURL(__daoProperties.getProperty(PUSH_URL_PROPERTY_KEY));
    }
    
    /**
     * This allows the __pushURL to be reset if needed. Upon setting we'll re-init URL
     */
    public boolean setURL(String url) {
        // figure the shortest possible valid URL is http://X.YYY
        boolean rval = false;
        if (url != null && url.trim().length() > 12) {  
                __pushURL = url;
                if (!__pushURL.endsWith("/")) __pushURL = __pushURL + "/";
                rval = true;
        }
        return rval;
    }

    @Override
    public Patient[] getPatients() throws DMPException {
        return __dao.getPatients();
    }

    @Override
    public Spirometer[] getSpirometers() throws DMPException {
        return __dao.getSpirometers();
    }

    @Override
    public AirQualityMonitor[] getAirQualityMonitors() throws DMPException {
        return __dao.getAirQualityMonitors();
    }

    @Override
    public Clinician[] getClinicians() throws DMPException {
        return __dao.getClinicians();
    }

    @Override
    public Spirometer findSpirometerForPatient(String patientId) throws DMPException {
        return __dao.findSpirometerForPatient(patientId);
    }

    @Override
    public AirQualityMonitor findAirQualityMonitorForPatient(String patientId) throws DMPException {
        return __dao.findAirQualityMonitorForPatient(patientId);
    }

    @Override
    public Clinician findClinicianForPatient(String patientId) throws DMPException {
        return __dao.findClinicianForPatient(patientId);
    }

    @Override
    public AirQualityReadings findAirQualityReadingsForPatient(
            String patientId, Date start, Date end) throws DMPException {
        return __dao.findAirQualityReadingsForPatient(patientId, start, end);
    }

    @Override
    public AirQualityReadings findAirQualityReadingsForPatient(String patientId)
            throws DMPException {
        return __dao.findAirQualityReadingsForPatient(patientId);
    }

    @Override
    public AirQualityReadings findAirQualityReadingsForPatient(String patientId, int groupId)
            throws DMPException {
        return __dao.findAirQualityReadingsForPatient(patientId, groupId);
    }
    
    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(
            String patientId, Date start, Date end) throws DMPException {
        return __dao.findSpirometerReadingsForPatient(patientId, start, end);
    }

    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(String patientId)
            throws DMPException {
        return __dao.findSpirometerReadingsForPatient(patientId);
    }

    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(String patientId, int groupId)
            throws DMPException {
        return __dao.findSpirometerReadingsForPatient(patientId, groupId);
    }
    
    @Override
    public AirQualityReadings findAirQualityReadingsForPatientTail(String patientId, int tail) 
            throws DMPException {
        return __dao.findAirQualityReadingsForPatientTail(patientId, tail);
    }
    
    @Override
    public AirQualityReadings findAirQualityReadingsForPatientHead(String patientId, int head) 
            throws DMPException {
        return __dao.findAirQualityReadingsForPatientTail(patientId, head);
    }
    
    @Override
    public boolean importAirQualityReadings(AirQualityReadings toImport,
            boolean overwrite) throws DMPException {
        try {
            if (__pushURL != null && toImport != null && toImport.size() > 0) {                
                int rval = __pushToServer(toImport, "airqualityreadings");
                if (rval >= 0) {
                    LOGGER.log(Level.WARNING, "Pushed " + rval + " Air Quality Readings to the server");
                } else {
                    LOGGER.log(Level.WARNING, "Unable to push Air Quality Readings to the server");
                }
            }
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Error pushing Air Quality Readings to the server " + t.getMessage());
        }
        return __dao.importAirQualityReadings(toImport, overwrite);
    }

    @Override
    public boolean importSpirometerReadings(SpirometerReadings toImport,
            boolean overwrite) throws DMPException {
        try {
            if (__pushURL != null && toImport != null && toImport.size() > 0) {                
                int rval = __pushToServer(toImport, "spirometerreadings");
                if (rval >= 0) {
                    LOGGER.log(Level.WARNING, "Pushed " + rval + " Spirometer Readings to the server");
                } else {
                    LOGGER.log(Level.WARNING, "Unable to push Spirometer Readings to the server");
                }
            }
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Error pushing Spirometer Readings to the server " + t.getMessage());
        }
        return __dao.importSpirometerReadings(toImport, overwrite);
    }

    @Override
    public boolean addManualSpirometerReading(SpirometerReading sr,
            boolean overwrite) throws DMPException {
        return __dao.addManualSpirometerReading(sr, overwrite);
    }

    @Override
    public boolean addOrModifyPatient(Patient p, boolean overwrite)
            throws DMPException {
        return __dao.addOrModifyPatient(p, overwrite);
    }

    @Override
    public boolean addOrModifySpirometer(Spirometer s, boolean overwrite)
            throws DMPException {
        return __dao.addOrModifySpirometer(s, overwrite);
    }

    @Override
    public boolean addOrModifyAirQualityMonitor(AirQualityMonitor aqm,
            boolean overwrite) throws DMPException {
        return __dao.addOrModifyAirQualityMonitor(aqm, overwrite);
    }

    @Override
    public boolean addClinician(Clinician c, boolean overwrite)
            throws DMPException {
        return __dao.addClinician(c, overwrite);
    }

    @Override
    public UIEvents findUIEventsForPatient(String patientId, Date start, Date end) throws DMPException {
        return __dao.findUIEventsForPatient(patientId, start, end);
    }

    @Override
    public UIEvents findUIEventsForPatient(String patientId) throws DMPException {
        return __dao.findUIEventsForPatient(patientId);
    }

    @Override
    public UIEvents findUIEventsForPatient(String patientId, int groupId)
            throws DMPException {
        return __dao.findUIEventsForPatient(patientId, groupId);
    }

    @Override
    public boolean importUIEvents(UIEvents toImport, boolean overwrite)
            throws DMPException {
        try {
            if (__pushURL != null && toImport != null && toImport.size() > 0) {                
                int rval = __pushToServer(toImport, "uievents");
                if (rval >= 0) {
                    LOGGER.log(Level.WARNING, "Pushed " + rval + " UI Events to the server");
                } else {
                    LOGGER.log(Level.WARNING, "Unable to push UI Events to the server");
                }
            }
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Error pushing UI Events to the server " + t.getMessage());
        }
        return __dao.importUIEvents(toImport, overwrite);
    }

    @Override
    public SpirometerReadings findSpirometerReadingsForPatientTail(
            String patientId, int tail) throws DMPException {
        return __dao.findSpirometerReadingsForPatientTail(patientId, tail);
    }

    @Override
    public UIEvents findUIEventsForPatientTail(String patientId, int tail)
            throws DMPException {
        return __dao.findUIEventsForPatientTail(patientId, tail);
    }
    
    private int __pushToServer(java.io.Serializable objects, String type)
            throws DMPException {
        HttpURLConnection urlConn = null;
        ObjectOutputStream oos = null;
        BufferedReader br = null;
        int rval = 0;
        try {
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
