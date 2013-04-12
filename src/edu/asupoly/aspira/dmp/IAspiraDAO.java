/**
 * 
 */
package edu.asupoly.aspira.dmp;

import edu.asupoly.aspira.model.AirQualityMonitor;
import edu.asupoly.aspira.model.Clinician;
import edu.asupoly.aspira.model.Patient;
import edu.asupoly.aspira.model.Spirometer;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.UIEvents;

import java.util.Date;

/**
 * @author kevinagary
 * This interface defines how we will work with persistent storage
 */
public interface IAspiraDAO {
    
    // Queries
    Patient[] getPatients() throws DMPException;
    Spirometer[] getSpirometers() throws DMPException;
    AirQualityMonitor[] getAirQualityMonitors() throws DMPException;
    Clinician[] getClinicians() throws DMPException;
    Spirometer findSpirometerForPatient(String patientId) throws DMPException;
    AirQualityMonitor findAirQualityMonitorForPatient(String patientId) throws DMPException;
    Clinician findClinicianForPatient(String patientId) throws DMPException;
    
    AirQualityReadings findAirQualityReadingsForPatient(String patientId, Date start, Date end) throws DMPException;
    AirQualityReadings findAirQualityReadingsForPatient(String patientId) throws DMPException;
    AirQualityReadings findAirQualityReadingsForPatient(String patientId,int groupId) throws DMPException;
    AirQualityReadings findAirQualityReadingsForPatientTail(String patientId, int tail) throws DMPException;
    AirQualityReadings findAirQualityReadingsForPatientHead(String patientId, int head) throws DMPException;
    SpirometerReadings findSpirometerReadingsForPatient(String patientId, Date start, Date end) throws DMPException;
    SpirometerReadings findSpirometerReadingsForPatient(String patientId) throws DMPException;
    SpirometerReadings findSpirometerReadingsForPatient(String patientId,int groupId) throws DMPException;
    UIEvents findUIEventsForPatient(String patientId, Date start, Date end) throws DMPException;
    UIEvents findUIEventsForPatient(String patientId) throws DMPException;
    UIEvents findUIEventsForPatient(String patientId,int groupId) throws DMPException;
    
    // CUD operations
    boolean importAirQualityReadings(AirQualityReadings toImport, boolean overwrite) throws DMPException;
    boolean importSpirometerReadings(SpirometerReadings toImport, boolean overwrite) throws DMPException;
    boolean importUIevents(UIEvents toImport, boolean overwrite) throws DMPException;
    
    // This is for the manual readings we can get via data entry
    boolean addManualSpirometerReading(SpirometerReading sr, boolean overwrite) throws DMPException;
    
    // Simple ones
    boolean addOrModifyPatient(Patient p, boolean overwrite) throws DMPException;
    boolean addOrModifySpirometer(Spirometer s, boolean overwrite) throws DMPException;
    boolean addOrModifyAirQualityMonitor(AirQualityMonitor aqm, boolean overwrite) throws DMPException;
    boolean addClinician(Clinician c, boolean overwrite) throws DMPException;
    
    // XXX We are going to want to track imports
}
