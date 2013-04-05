/**
 * 
 */
package edu.asupoly.aspira.dmp;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import edu.asupoly.aspira.model.AirQualityMonitor;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.Clinician;
import edu.asupoly.aspira.model.Patient;
import edu.asupoly.aspira.model.Spirometer;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;

/**
 * @author kevinagary
 *
 */
public final class AspiraDAO implements IAspiraDAO {
    
    private static String PROPERTY_FILENAME = "properties/dao.properties";
    private static String DAO_CLASS_PROPERTY_KEY = "daoClassName";
    private static final String PUSH_URL_PROPERTY_KEY = "push.url";
    
    private static AspiraDAO  __singletonDAOWrapper;
    private AspiraDAOBaseImpl __dao = null;
    private Properties        __daoProperties = null;
    private URL pushURL;
    
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
        } catch (Throwable t1) {
            // XXX log
            t1.printStackTrace();
            throw new DMPException(t1);
        }
        // all impls need to figure out if they need to push
        setURL(__daoProperties.getProperty(PUSH_URL_PROPERTY_KEY));
    }
    
    /**
     * This allows the pushURL to be reset if needed. Upon setting we'll re-init URL
     */
    public boolean setURL(String url) {
        // figure the shortest possible valid URL is http://X.YYY
        if (url != null && url.trim().length() > 12) {  
            try {
                pushURL = new URL(url);
                return true;
            } catch (MalformedURLException mfe) {
                // XXX a MalformedURLException the most likely cause, log it
                return false;
            } catch (Throwable t) {
                // XXX Anything else we did not imagine
                return false;
            }
        }
        return false;
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
    public SpirometerReadings findSpirometerReadingsForPatient(
            String patientId, Date start, Date end) throws DMPException {
        return __dao.findSpirometerReadingsForPatient(patientId, start, end);
    }

    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(String patientId)
            throws DMPException {
        return __dao.findSpirometerReadingsForPatient(patientId);
    }

    // XXX The writes are more interesting as we want to push if we have a URL to push to
    
    @Override
    public boolean importAirQualityReadings(AirQualityReadings toImport,
            boolean overwrite) throws DMPException {
        // TODO Auto-generated method stub
        return __dao.importAirQualityReadings(toImport, overwrite);
    }

    @Override
    public boolean importSpirometerReadings(SpirometerReadings toImport,
            boolean overwrite) throws DMPException {
        // TODO Auto-generated method stub
        return __dao.importSpirometerReadings(toImport, overwrite);
    }

    @Override
    public boolean importReadings(AirQualityReadings aqImport,
            SpirometerReadings spImport, boolean overwrite) throws DMPException {
        // TODO Auto-generated method stub
        return __dao.importReadings(aqImport, spImport, overwrite);
    }

    @Override
    public boolean addManualSpirometerReading(SpirometerReading sr,
            boolean overwrite) throws DMPException {
        // TODO Auto-generated method stub
        return __dao.addManualSpirometerReading(sr, overwrite);
    }

    @Override
    public boolean addOrModifyPatient(Patient p, boolean overwrite)
            throws DMPException {
        // TODO Auto-generated method stub
        return __dao.addOrModifyPatient(p, overwrite);
    }

    @Override
    public boolean addOrModifySpirometer(Spirometer s, boolean overwrite)
            throws DMPException {
        // TODO Auto-generated method stub
        return __dao.addOrModifySpirometer(s, overwrite);
    }

    @Override
    public boolean addOrModifyAirQualityMonitor(AirQualityMonitor aqm,
            boolean overwrite) throws DMPException {
        // TODO Auto-generated method stub
        return __dao.addOrModifyAirQualityMonitor(aqm, overwrite);
    }

    @Override
    public boolean addOrModifyClinician(Clinician c, boolean overwrite)
            throws DMPException {
        // TODO Auto-generated method stub
        return __dao.addOrModifyClinician(c, overwrite);
    }
}
