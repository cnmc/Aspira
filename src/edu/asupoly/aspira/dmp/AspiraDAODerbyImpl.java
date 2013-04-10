/**
 * 
 */
package edu.asupoly.aspira.dmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import edu.asupoly.aspira.model.AirQualityMonitor;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.Clinician;
import edu.asupoly.aspira.model.ParticleReading;
import edu.asupoly.aspira.model.Patient;
import edu.asupoly.aspira.model.Spirometer;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;

/**
 * @author kevinagary
 *
 */
public class AspiraDAODerbyImpl extends AspiraDAOBaseImpl {

    /**
     * 
     */
    public AspiraDAODerbyImpl() {
        super();  // true initialization goes in the init method
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#getPatients()
     */
    @Override
    public Patient[] getPatients() throws DMPException {
       Connection c = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       ArrayList<Patient> patients = new ArrayList<Patient>();
       try {
           c = DriverManager.getConnection(__jdbcURL);
           ps = c.prepareStatement(__derbyProperties.getProperty("sql.getPatients"));
           rs = ps.executeQuery();
           while (rs.next()) {
              patients.add(new Patient(rs.getString("patientid"), rs.getString("sex"),
                           rs.getInt("ratehigh"), rs.getInt("ratelow"),
                           rs.getInt("valuehigh"), rs.getInt("valuelow"),
                           rs.getString("bestvaluetype"), rs.getInt("bestvaluetarget"),
                           rs.getString("patientnotes")
                          ));
           }
           return patients.toArray(new Patient[0]);
       } catch (SQLException se) {
           // XXX log a DB problem
           throw new DMPException(se);
       } catch (Throwable t) {
           // XXX The unknown happened, log it
           throw new DMPException(t);
       } finally {
           try {
               if (rs != null) rs.close();
               if (ps != null) ps.close();
               if (c != null) c.close();
           } catch (SQLException se2) {
               // XXX log it and give up
           }
       }
    }
    
    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#getSpirometers()
     */
    @Override
    public Spirometer[] getSpirometers() throws DMPException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Spirometer> devices = new ArrayList<Spirometer>();
        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.getSpirometers"));
            rs = ps.executeQuery();
            while (rs.next()) {
               devices.add(new Spirometer(rs.getString("serialid"), rs.getString("vendor"),
                            rs.getString("model"), rs.getString("description")
                           ));
            }
            return devices.toArray(new Spirometer[0]);
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#getAirQualityMonitors()
     */
    @Override
    public AirQualityMonitor[] getAirQualityMonitors() throws DMPException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<AirQualityMonitor> devices = new ArrayList<AirQualityMonitor>();
        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.getAirQualityMonitors"));
            rs = ps.executeQuery();
            while (rs.next()) {
               devices.add(new AirQualityMonitor(rs.getString("serialid"), rs.getString("vendor"),
                            rs.getString("model"), rs.getString("description")
                           ));
            }
            return devices.toArray(new AirQualityMonitor[0]);
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#getClinicians()
     */
    @Override
    public Clinician[] getClinicians() throws DMPException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Clinician> clinicians = new ArrayList<Clinician>();
        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.getPatients"));
            rs = ps.executeQuery();
            while (rs.next()) {
               clinicians.add(new Clinician(rs.getString("clinicianid")));
            }
            return clinicians.toArray(new Clinician[0]);
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#findSpirometerForPatient(java.lang.String)
     */
    @Override
    public Spirometer findSpirometerForPatient(String patientId) throws DMPException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Spirometer rval = null;
        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.findSpirometerForPatient"));
            ps.setString(1, patientId);
            rs = ps.executeQuery();
            if (rs.next()) {
               rval = new Spirometer(rs.getString("serialid"), rs.getString("vendor"),
                       rs.getString("model"), rs.getString("description"));
            }
            return rval;
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#findAirQualityMonitorForPatient(java.lang.String)
     */
    @Override
    public AirQualityMonitor findAirQualityMonitorForPatient(String patientId)
            throws DMPException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AirQualityMonitor rval = null;
        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.findAirQualityMonitorForPatient"));
            ps.setString(1, patientId);
            rs = ps.executeQuery();
            if (rs.next()) {
               rval = new AirQualityMonitor(rs.getString("serialid"), rs.getString("vendor"),
                       rs.getString("model"), rs.getString("description"));
            }
            return rval;
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#findClinicianForPatient(java.lang.String)
     */
    @Override
    public Clinician findClinicianForPatient(String patientId) throws DMPException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Clinician rval = null;
        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.findClinicianForPatient"));
            ps.setString(1, patientId);
            rs = ps.executeQuery();
            if (rs.next()) {
               rval = new Clinician(rs.getString("clinicianid"));
            }
            return rval;
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#findAirQualityReadingsForPatient(java.lang.String, java.util.Date, java.util.Date)
     */
    @Override
    public AirQualityReadings findAirQualityReadingsForPatient(
            String patientId, Date start, Date end) throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#findAirQualityReadingsForPatient(java.lang.String)
     */
    @Override
    public AirQualityReadings findAirQualityReadingsForPatient(String patientId)
            throws DMPException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AirQualityReadings rval = new AirQualityReadings();
        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.findAirQualityReadingsForPatient"));
            ps.setString(1, patientId);
            rs = ps.executeQuery();
            while (rs.next()) {
               rval.addReading(new ParticleReading(rs.getString("deviceid"), rs.getString("patientid"),
                       rs.getDate("readingtime"), rs.getInt("smallparticle"), rs.getInt("largeparticle")));
            }
            return rval;
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#findSpirometerReadingsForPatient(java.lang.String, java.util.Date, java.util.Date)
     */
    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(
            String patientId, Date start, Date end) throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#findSpirometerReadingsForPatient(java.lang.String)
     */
    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(String patientId)
            throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#importAirQualityReadings(edu.asupoly.aspira.model.AirQualityReadings, boolean)
     */
    @Override
    public boolean importAirQualityReadings(AirQualityReadings toImport, boolean overwrite) throws DMPException {
        Connection c = null;
        PreparedStatement ps = null;
        PreparedStatement psgroup = null;
        ParticleReading next = null;
        ResultSet rs = null;
        int id = -1; // in case get unique fails
        
        if (toImport == null || toImport.size() == 0) return true;
        
        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.importAirQualityReadings"));
            psgroup = c.prepareStatement(__derbyProperties.getProperty("sql.getUniqueId"));
            
            // every import is a batch insert of particle readings, so we track those imports in
            // a particular table
            rs = psgroup.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            
            Iterator<ParticleReading> iter = toImport.iterator();
            while (iter.hasNext()) {
                next = iter.next();
                ps.setString(1, next.getDeviceId());
                ps.setString(2, next.getPatientId());
                // java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(utilDate.getTime())
                ps.setTimestamp(3, new java.sql.Timestamp(next.getDateTime().getTime()));
                ps.setInt(4, next.getSmallParticleCount());
                ps.setInt(5, next.getLargeParticleCount());
                ps.setInt(6, id);
                ps.executeUpdate();
                ps.clearParameters();
            }
            c.commit();
        } catch (SQLException se) {
            // XXX log a db error
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (psgroup != null) psgroup.close();
                if (c != null) {
                    c.rollback();
                    c.close();
                }
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#importSpirometerReadings(edu.asupoly.aspira.model.SpirometerReadings, boolean)
     */
    @Override
    public boolean importSpirometerReadings(SpirometerReadings toImport,
            boolean overwrite) throws DMPException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#importReadings(edu.asupoly.aspira.model.AirQualityReadings, edu.asupoly.aspira.model.SpirometerReadings, boolean)
     */
    @Override
    public boolean importReadings(AirQualityReadings aqImport, SpirometerReadings spImport, boolean overwrite) 
            throws DMPException {
        // TODO
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#addManualSpirometerReading(edu.asupoly.aspira.model.SpirometerReading, boolean)
     */
    @Override
    public boolean addManualSpirometerReading(SpirometerReading sr,
            boolean overwrite) throws DMPException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#addOrModifyPatient(edu.asupoly.aspira.model.Patient, boolean)
     */
    @Override
    public boolean addOrModifyPatient(Patient p, boolean overwrite) throws DMPException {
        if (p == null) return false;
        
        Connection c = null;
        PreparedStatement ps = null;
        PreparedStatement psdml = null;
        ResultSet rs = null;

        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.getPatient"));
            ps.setString(1, p.getPatientId());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (overwrite) {
                    psdml = c.prepareStatement(__derbyProperties.getProperty("sql.modifyPatient"));
                } else return false;  // duplicate with no overwrite flag
            } else {
                psdml = c.prepareStatement(__derbyProperties.getProperty("sql.addPatient"));
            }
            // now write the new or updated Patient
            psdml.setString(1, p.getSex());
            psdml.setString(2, p.getPatientNotes());
            psdml.setString(3, p.getBestValueType());
            psdml.setInt(4, p.getBestValueTarget());
            psdml.setInt(5, p.getRateH());
            psdml.setInt(6, p.getRateL());
            psdml.setInt(7, p.getValueH());
            psdml.setInt(8, p.getValueL());
            psdml.setString(9,  p.getPatientId());
            return (psdml.executeUpdate() == 1);
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (psdml != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#addOrModifySpirometer(edu.asupoly.aspira.model.Spirometer, boolean)
     */
    @Override
    public boolean addOrModifySpirometer(Spirometer s, boolean overwrite)
            throws DMPException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#addOrModifyAirQualityMonitor(edu.asupoly.aspira.model.AirQualityMonitor, boolean)
     */
    @Override
    public boolean addOrModifyAirQualityMonitor(AirQualityMonitor aqm, boolean overwrite) throws DMPException {
       if (aqm == null) return false;
        
        Connection c = null;
        PreparedStatement ps = null;
        PreparedStatement psdml = null;
        ResultSet rs = null;

        try {
            c = DriverManager.getConnection(__jdbcURL);
            ps = c.prepareStatement(__derbyProperties.getProperty("sql.getAirQualityMonitor"));
            ps.setString(1, aqm.getSerialId());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (overwrite) {
                    psdml = c.prepareStatement(__derbyProperties.getProperty("sql.modifyAirQualityMonitor"));
                } else return false; // duplicate with no overwrite flag
            } else {
                psdml = c.prepareStatement(__derbyProperties.getProperty("sql.addAirQualityMonitor"));
            }   

            // now write the new or updated Patient
            // vendor,model,description,patientid,serialid
            psdml.setString(1, aqm.getVendor());
            psdml.setString(2, aqm.getModel());
            psdml.setString(3, aqm.getDescription());
            psdml.setString(4, aqm.getAssignedToPatient());
            psdml.setString(5, aqm.getSerialId());
            
            return (psdml.executeUpdate() == 1);
        } catch (SQLException se) {
            // XXX log a DB problem
            throw new DMPException(se);
        } catch (Throwable t) {
            // XXX The unknown happened, log it
            throw new DMPException(t);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (psdml != null) ps.close();
                if (c != null) c.close();
            } catch (SQLException se2) {
                // XXX log it and give up
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.IAspiraDAO#addOrModifyClinician(edu.asupoly.aspira.model.Clinician, boolean)
     */
    @Override
    public boolean addOrModifyClinician(Clinician c, boolean overwrite)
            throws DMPException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAOBaseImpl#init(java.util.Properties)
     */
    @Override
    public void init(Properties p) throws DMPException {
        __derbyProperties = new Properties();
        String jdbcDriver = p.getProperty("jdbc.driver");
        String jdbcURL    = p.getProperty("jdbc.url");
        // do we need user and password?
        
        if (jdbcDriver == null || jdbcURL == null) {
            throw new DMPException("JDBC not configured");
        }
        // load the driver, test the URL
        try {
            // read in all the derby properties and SQL queries we need  
            Enumeration<?> keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                if (key.startsWith("sql") || key.startsWith("derby")) {
                    System.out.println("found property with key, value\t" + key + ", " + p.getProperty(key));
                    __derbyProperties.setProperty(key, p.getProperty(key));
                }
            }
            
            Class.forName(jdbcDriver);
            // test the connection
            if (!__testConnection(jdbcURL, p.getProperty("sql.checkConnectionQuery"))) {
                throw new DMPException("Unable to connect to database");
            }
            __jdbcURL = jdbcURL;
            __derbyProperties.setProperty("jdbc.driver", jdbcDriver);
            __derbyProperties.setProperty("jdbc.url", jdbcURL);
        } catch (Throwable t) {
            // XXX log here
            throw new DMPException(t);
        }
    }
    
    private static boolean __testConnection(String url, String query) {
        Connection c = null;
        Statement s = null;
        try {
            c = DriverManager.getConnection(url);
            s = c.createStatement();
            return s.execute(query);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        } finally {
            try {
                if (s != null) s.close();
                if (c != null) c.close();
            } catch (SQLException se) {
                // XX log here
            }
        }
    }

    private String __jdbcURL;
    private Properties __derbyProperties;
}
