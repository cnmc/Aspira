/**
 * 
 */
package edu.asupoly.aspira.dmp;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import edu.asupoly.aspira.model.AirQualityMonitor;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.Clinician;
import edu.asupoly.aspira.model.ParticleReading;
import edu.asupoly.aspira.model.Patient;
import edu.asupoly.aspira.model.ServerPushEvent;
import edu.asupoly.aspira.model.Spirometer;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.UIEvent;
import edu.asupoly.aspira.model.UIEvents;

/**
 * @author kevinagary
 *
 */
public class AspiraDAOInMemoryImpl extends AspiraDAOBaseImpl implements Serializable {

    private static final long serialVersionUID = -4574092811303193281L;

    // This object just keeps everything in memory inside itself.
    // After each CUD it writes itself out to disk
    protected HashMap<String, Patient>    __patients;
    protected HashMap<String, Clinician>  __clinicians;
    protected HashMap<String, Spirometer> __spirometers;
    protected HashMap<String, AirQualityMonitor> __aqMonitors;

    protected AirQualityReadings __aqReadings; 
    protected SpirometerReadings __spReadings;
    protected UIEvents __events;
    
    /**
     * 
     */
    public AspiraDAOInMemoryImpl() {
        super();
        __patients    = new HashMap<String, Patient>();
        __clinicians  = new HashMap<String, Clinician>();
        __spirometers = new HashMap<String, Spirometer>();
        __aqMonitors  = new HashMap<String, AirQualityMonitor>();
        __aqReadings  = new AirQualityReadings();
        __spReadings  = new SpirometerReadings();
    }

    /**
     * This implementation does not require anything from the properties
     */
    @Override
    public void init(Properties p) throws DMPException {        
    }
    
    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#getPatients()
     */
    @Override
    public Patient[] getPatients() throws DMPException {
        try {
            return __patients.values().toArray(new Patient[0]);
        } catch (Throwable t) {
            throw new DMPException(t);
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#getSpirometers()
     */
    @Override
    public Spirometer[] getSpirometers() throws DMPException {
        try {
            return __spirometers.values().toArray(new Spirometer[0]);
        } catch (Throwable t) {
            throw new DMPException(t);
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#getAirQualityMonitors()
     */
    @Override
    public AirQualityMonitor[] getAirQualityMonitors() throws DMPException {
        try {
            return __aqMonitors.values().toArray(new AirQualityMonitor[0]);
        } catch (Throwable t) {
            throw new DMPException(t);
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#getClinicians()
     */
    @Override
    public Clinician[] getClinicians() throws DMPException {
        try {
            return __clinicians.values().toArray(new Clinician[0]);
        } catch (Throwable t) {
            throw new DMPException(t);
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#findSpirometerForPatient(java.lang.String)
     */
    @Override
    public Spirometer findSpirometerForPatient(String patientId)
            throws DMPException {
        // We're storing the assignedToPatient on the Spirometer so we have to go that way
        for (Spirometer s : __spirometers.values()) {
            if (s.getAssignedToPatient() != null && s.getAssignedToPatient().equals(patientId)) return s;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#findAirQualityMonitorForPatient(java.lang.String)
     */
    @Override
    public AirQualityMonitor findAirQualityMonitorForPatient(String patientId)
            throws DMPException {
        
        if (patientId != null) {
            for (AirQualityMonitor aqm : __aqMonitors.values()) {
                if (patientId.equals(aqm.getAssignedToPatient())) return aqm;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#findClinicianForPatient(java.lang.String)
     */
    @Override
    public Clinician findClinicianForPatient(String patientId)
            throws DMPException {
        
        if (patientId != null) {
            for (Clinician c : __clinicians.values()) {
                // Currently storing Patient IDs on the Clinician side which is awkward
                // as it is the many, but don't want to pollute Patient
                String[] patientIds = c.getPatientIds();
                for (String s : patientIds) {
                    if (__patients.containsKey(s)) return c;
                }
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#findAirQualityReadingsForPatient(java.lang.String, java.util.Date, java.util.Date)
     */
    @Override
    public AirQualityReadings findAirQualityReadingsForPatient(
            String patientId, Date start, Date end) throws DMPException {
        
        AirQualityReadings rval = findAirQualityReadingsForPatient(patientId);
        if (rval != null) {
            rval = rval.getAirQualityBetween(start, true, end, true);
        }
        return rval;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#findAirQualityReadingsForPatient(java.lang.String)
     */
    @Override
    public AirQualityReadings findAirQualityReadingsForPatient(String patientId)
            throws DMPException {
        
        return __aqReadings.getAirQualityReadingsForPatient(patientId);
    }

    @Override
    public AirQualityReadings findAirQualityReadingsForPatientTail(
            String patientId, int tail) throws DMPException {
        AirQualityReadings aqr = __aqReadings.getAirQualityReadingsForPatient(patientId);
        if (aqr == null) return null;
        return aqr.getLastNReadings(tail);
    }


    @Override
    public SpirometerReadings findSpirometerReadingsForPatientTail(
            String patientId, int tail) throws DMPException {
        SpirometerReadings aqr = __spReadings.getSpirometerReadingsForPatient(patientId);
        if (aqr == null) return null;
        return aqr.getLastNReadings(tail);
    }

    @Override
    public UIEvents findUIEventsForPatientTail(String patientId, int tail)
            throws DMPException {
        UIEvents aqr = __events.getUIEventsForPatient(patientId);
        if (aqr == null) return null;
        return aqr.getLastNReadings(tail);
    }
    
    @Override
    public AirQualityReadings findAirQualityReadingsForPatientHead(
            String patientId, int head) throws DMPException {
        AirQualityReadings aqr = __aqReadings.getAirQualityReadingsForPatient(patientId);
        if (aqr == null) return null;
        return aqr.getFirstNReadings(head);
    }
    
    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#findSpirometerReadingsForPatient(java.lang.String, java.util.Date, java.util.Date)
     */
    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(
            String patientId, Date start, Date end) throws DMPException {

        SpirometerReadings rval = findSpirometerReadingsForPatient(patientId);
        if (rval != null) {
            Iterator<SpirometerReading> iter = rval.getSpirometerReadingsBetween(start, end);
            rval = new SpirometerReadings();
            while (iter.hasNext()) {
                rval.addReading(iter.next());
            }
        }
        return rval;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#findSpirometerReadingsForPatient(java.lang.String)
     */
    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(String patientId)
            throws DMPException {
        
        return __spReadings.getSpirometerReadingsForPatient(patientId);
    }


    @Override
    public AirQualityReadings findAirQualityReadingsForPatient(
            String patientId, int groupId) throws DMPException {
        AirQualityReadings aqr = findAirQualityReadingsForPatient(patientId);
        if (aqr == null) return null;

        AirQualityReadings rval = new AirQualityReadings();
        ParticleReading pr = null;
        Iterator<ParticleReading> iter = aqr.iterator();
        while (iter.hasNext()) {
            pr = iter.next();
            if (pr.getGroupId() == groupId) {
                rval.addReading(pr);
            }
        }
        return rval;
    }

    @Override
    public SpirometerReadings findSpirometerReadingsForPatient(
            String patientId, int groupId) throws DMPException {
        SpirometerReadings spr = findSpirometerReadingsForPatient(patientId);
        if (spr == null) return null;

        SpirometerReadings rval = new SpirometerReadings();
        SpirometerReading pr = null;
        Iterator<SpirometerReading> iter = spr.iterator();
        while (iter.hasNext()) {
            pr = iter.next();
            if (pr.getGroupId() == groupId) {
                rval.addReading(pr);
            }
        }
        return rval;
    }
    
    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#importAirQualityReadings(edu.asupoly.aspira.model.AirQualityReadings, boolean)
     */
    @Override
    public boolean importAirQualityReadings(AirQualityReadings toImport, boolean overwrite) throws DMPException {
        // we are disregarding overwrite now and just always overwriting
        try {
            boolean rval = true;
            if (toImport != null) {
                __aqReadings.addReadings(toImport);
            } else rval = false;   // if trying to import null
            return rval;
        } catch (Throwable t) {
            // XXX log
            throw new DMPException(t);
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#importSpirometerReadings(edu.asupoly.aspira.model.SpirometerReadings, boolean)
     */
    @Override
    public boolean importSpirometerReadings(SpirometerReadings toImport, boolean overwrite) throws DMPException {
        // we are disregarding overwrite now and just always overwriting
        try {
            boolean rval = true;
            if (toImport != null) {
                __spReadings.addReadings(toImport);
            } else rval = false;   // if trying to import null
            return rval;
        } catch (Throwable t) {
            // XXX log
            throw new DMPException(t);
        }
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#addManualSpirometerReading(edu.asupoly.aspira.model.SpirometerReading, boolean)
     */
    @Override
    public boolean addManualSpirometerReading(SpirometerReading sr, boolean overwrite) throws DMPException {
        return false;
    }
    
    /* XXX THIS METHOD NEEDS COMPLETION, include manual flag in the equals computation for SR
        boolean rval = false;
        boolean saveTheReading = true;
        
        SpirometerReadings readings = findSpirometerReadingsForPatient(sr.getPatientId());
        if (readings == null) {
            readings = new SpirometerReadings("", "");
            __spReadings.put(sr.getPid(), readings);
        } else {
            // patient had readings
            SpirometerReading preExist = readings.getSpirometerReadingAt(sr.getMeasureDate());
            saveTheReading = (preExist == null) || (overwrite);
        }
        
        if (saveTheReading) {
            rval = readings.addReading(sr);
        }
        
        return rval;
    }
    */
    
    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#addOrModifyPatient(edu.asupoly.aspira.model.Patient, boolean)
     */
    @Override
    public boolean addOrModifyPatient(Patient p, boolean overwrite)
            throws DMPException {
        
        Patient existing = __patients.get(p.getPatientId());
        if (overwrite || existing == null) {
            return (__patients.put(p.getPatientId(), p) == null);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#addOrModifySpirometer(edu.asupoly.aspira.model.Spirometer, boolean)
     */
    @Override
    public boolean addOrModifySpirometer(Spirometer s, boolean overwrite)
            throws DMPException {
        
        Spirometer existing = __spirometers.get(s.getSerialId());
        if (overwrite || existing == null) {
            return (__spirometers.put(s.getSerialId(), s) == null);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#addOrModifyAirQualityMonitor(edu.asupoly.aspira.model.AirQualityMonitor, boolean)
     */
    @Override
    public boolean addOrModifyAirQualityMonitor(AirQualityMonitor aqm, boolean overwrite)
            throws DMPException {
        
        Spirometer existing = __spirometers.get(aqm.getSerialId());
        if (overwrite || existing == null) {
            return (__aqMonitors.put(aqm.getSerialId(), aqm) == null);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see edu.asupoly.aspira.dmp.AspiraDAO#addOrModifyClinician(edu.asupoly.aspira.model.Clinician, boolean)
     */
    @Override
    public boolean addClinician(Clinician c, boolean overwrite) throws DMPException {
        
        Clinician existing = __clinicians.get(c.getClinicianId());
        if (overwrite || existing == null) {
            return (__clinicians.put(c.getClinicianId(), c) == null);
        }
        return false;
    }

    @Override
    public UIEvents findUIEventsForPatient(String patientId, Date start, Date end) throws DMPException {
        UIEvents rval = findUIEventsForPatient(patientId);
        
        if (rval != null) {
            Iterator<UIEvent> iter = rval.getUIEventsBetween(start, true, end, true);
            rval = new UIEvents();
            while (iter.hasNext()) {
                rval.addEvent(iter.next());
            }
        }
        return rval;
    }

    @Override
    public UIEvents findUIEventsForPatient(String patientId)
            throws DMPException {
        return __events.getUIEventsForPatient(patientId);
    }

    @Override
    public UIEvents findUIEventsForPatient(String patientId, int groupId)
            throws DMPException {
        UIEvents events = findUIEventsForPatient(patientId);
        if (events == null) return null;

        UIEvents rval = new UIEvents();
        UIEvent pr = null;
        Iterator<UIEvent> iter = events.iterator();
        while (iter.hasNext()) {
            pr = iter.next();
            if (pr.getGroupId() == groupId) {
                rval.addEvent(pr);
            }
        }
        return rval;
    }

    @Override
    public boolean importUIEvents(UIEvents toImport, boolean overwrite)
            throws DMPException {
        // we are disregarding overwrite now and just always overwriting
        try {
            boolean rval = true;
            if (toImport != null) {
                __events.addEvents(toImport);
            } else rval = false;   // if trying to import null
            return rval;
        } catch (Throwable t) {
            // XXX log
            throw new DMPException(t);
        }
    }

    @Override
    public ServerPushEvent getLastServerPush() throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ServerPushEvent getLastServerPush(int type) throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ServerPushEvent[] getServerPushEvents() throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ServerPushEvent[] getServerPushEvents(int type) throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ServerPushEvent getLastValidServerPush() throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ServerPushEvent getLastValidServerPush(int type) throws DMPException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean addPushEvent(ServerPushEvent s) throws DMPException {
        // TODO Auto-generated method stub
        return false;
    }
}
