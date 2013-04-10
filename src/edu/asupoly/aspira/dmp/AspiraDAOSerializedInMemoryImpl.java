/**
 * 
 */
package edu.asupoly.aspira.dmp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
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
public class AspiraDAOSerializedInMemoryImpl extends AspiraDAOInMemoryImpl {

    private static final String SERIALIZED_DAO_FILENAME_KEY="serializedDAOfilenameKey";
    /**
     * 
     */
    private static final long serialVersionUID = -2016938516422704861L;

    private String __filename = null;
    
    /**
     * 
     */
    public AspiraDAOSerializedInMemoryImpl() {
        super();
    }


    /**
     * This implementation requires a filename from the properties
     */
    @SuppressWarnings("unchecked")
    @Override
    public void init(Properties p) throws DMPException {
        ObjectInputStream ois = null;
        __filename = p.getProperty(SERIALIZED_DAO_FILENAME_KEY);
        
        if (__filename == null || __filename.trim().length() == 0) {
            throw new DMPException("Could not read property " + SERIALIZED_DAO_FILENAME_KEY);
        } else {
            // check for the existence of the file, 
            // if it does not exist create it
            // if it does try and initialize the data structures from it
            try {
                if (! new File(__filename).createNewFile()) {
                    ois = new ObjectInputStream(new FileInputStream(__filename));
                    __patients = (HashMap<String, Patient>) ois.readObject();
                    __clinicians  = (HashMap<String, Clinician>) ois.readObject();
                    __spirometers = (HashMap<String, Spirometer>) ois.readObject();
                    __aqMonitors  = (HashMap<String, AirQualityMonitor>) ois.readObject();
                    __aqReadings  = (AirQualityReadings) ois.readObject();
                    __spReadings  = (HashMap<String, SpirometerReadings>) ois.readObject();
                }
            } catch (IOException ie) {
                __filename = null;
                throw new DMPException("Unable to serialize with file " + __filename);
                // XXX log
            } catch (ClassNotFoundException cfe) {
                __filename = null;
                throw new DMPException("File not found " + __filename);
                // XXX this would be strange, need to log
            } catch (Throwable t) {
                __filename = null;
                throw new DMPException("Unable to serialize with file, something else happened " + __filename);
                // XXX catch-all, just log
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (Throwable t2) {
                        // XXX log that the object stream is not closed
                    }
                }
            }
        }
    }
    
    protected void _writeStore() throws DMPException {
        ObjectOutputStream oos = null;
        System.out.println("Object filename is " + __filename);
        if (__filename == null) throw new DMPException("Object store not initialized");
        try {
            oos = new ObjectOutputStream(new FileOutputStream(__filename));
            oos.writeObject(__patients);
            oos.writeObject(__clinicians);
            oos.writeObject(__spirometers);
            oos.writeObject(__aqMonitors);
            oos.writeObject(__aqReadings);
            oos.writeObject(__spReadings);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new DMPException(t);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ie) {
                    // XXX log and swallow
                }
            }
        }
    }
    
    // CUD operations
    public boolean importAirQualityReadings(AirQualityReadings toImport, boolean overwrite) 
            throws DMPException  {
        boolean rval = super.importAirQualityReadings(toImport, overwrite);
        if (rval) {
            _writeStore();
        }
        return true;
    }
    
    public boolean importSpirometerReadings(SpirometerReadings toImport, boolean overwrite) 
            throws DMPException  {
        boolean rval = super.importSpirometerReadings(toImport, overwrite);
        if (rval) {
            _writeStore();
        }
        return true;
    }
    
    // We only have this one so we can do in the context of a single transaction and rollback if needed
    public boolean importReadings(AirQualityReadings aqImport, SpirometerReadings spImport, boolean overwrite) 
            throws DMPException  {
        boolean rval = false;
        if ((rval = importAirQualityReadings(aqImport, overwrite))) {
            rval = importSpirometerReadings(spImport, overwrite);
        }
        return rval;
    }
    
    // This is for the manual readings we can get via data entry
    public boolean addManualSpirometerReading(SpirometerReading sr, boolean overwrite) 
            throws DMPException {
        boolean rval = super.addManualSpirometerReading(sr, overwrite);
        if (rval) {
            _writeStore();
        }
        return true;
    }
    
    // Simple ones
    public boolean addOrModifyPatient(Patient p, boolean overwrite) throws DMPException {
        boolean rval = super.addOrModifyPatient(p, overwrite);
        if (rval) {
            _writeStore();
        }
        return true;
    }
    
    public boolean addOrModifySpirometer(Spirometer s, boolean overwrite) throws DMPException {
        boolean rval = super.addOrModifySpirometer(s, overwrite);
        if (rval) {
            _writeStore();
        }
        return true;
    }
    
    public boolean addOrModifyAirQualityMonitor(AirQualityMonitor aqm, boolean overwrite) throws DMPException {
        boolean rval = super.addOrModifyAirQualityMonitor(aqm, overwrite);
        if (rval) {
            _writeStore();
        }
        return true;
    }
    
    public boolean addOrModifyClinician(Clinician c, boolean overwrite) throws DMPException {      
        boolean rval = super.addOrModifyClinician(c, overwrite);
        if (rval) {
            _writeStore();
        }
        return true;
    }
}
