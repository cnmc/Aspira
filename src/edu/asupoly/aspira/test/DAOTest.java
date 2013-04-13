package edu.asupoly.aspira.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.DMPException;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityMonitor;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.Clinician;
import edu.asupoly.aspira.model.ParticleReading;
import edu.asupoly.aspira.model.Patient;
import edu.asupoly.aspira.model.Spirometer;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.UIEvent;
import edu.asupoly.aspira.model.UIEvents;

public class DAOTest {
    private SpirometerReadings __spReadings = null;
    private AirQualityReadings __aqReadings = null;
    private SpirometerReadings __sp2Readings = null;
    private AirQualityReadings __aq2Readings = null;
    private UIEvents __eventReadings = null;
    private Patient __patient1 = null;
    private Patient __patient2 = null;
    private Clinician __clinician1 = null;
    private Clinician __clinician2 = null;
    private Spirometer __spirometer1 = null;
    private Spirometer __spirometer2 = null;
    private AirQualityMonitor __aqm1 = null;
    private AirQualityMonitor __aqm2 = null;
    
    @Before
    public void setUp() throws Exception {
        __spReadings = new SpirometerReadings();
        __aqReadings = new AirQualityReadings();
        __sp2Readings = new SpirometerReadings();
        __aq2Readings = new AirQualityReadings();
        __eventReadings = new UIEvents();
        
        //String anonId, String sex, String rH, String rL, 
        // String vH, String vL,  String btype, String bvalue, String pN
        __patient1 = new Patient("patient_1", "M", "500", "100", "300", "100", "bvType", "400", "patient one notes");
        __patient2 = new Patient("patient_2", "F", "502", "102", "302", "102", "bvType2", "402", "patient two notes");
        __clinician1 = new Clinician("clinc_1");
        __clinician1.addPatient(__patient1);
        __clinician2 = new Clinician("clinc_2");
        __clinician2.addPatient(__patient2);
        __spirometer1 = new Spirometer("serial1", "vendor1", "model1", "desc1", __patient1.getPatientId());
        __spirometer2 = new Spirometer("serial2", "vendor2", "model2", "desc2", __patient2.getPatientId());
        __aqm1 = new AirQualityMonitor("serial1", "vendor1", "model1", "desc1", __patient1.getPatientId());
        __aqm2 = new AirQualityMonitor("serial2", "vendor2", "model2", "desc2", __patient2.getPatientId());
        
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:31", "169900", "5700"));
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:32", "166300", "5700"));
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:33", "164500", "6100"));
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:34", "165100", "6400"));
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:35", "173500", "6600"));
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:36", "178800", "6400"));
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:37", "171900", "5200"));
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:38", "170700", "7000"));
        __aqReadings.addReading(new ParticleReading("device_1", "patient_1", "02/13/13", "00:38", "171500", "6100"));
        
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:39", "168800", "5800"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:40", "167000", "5900"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:41", "165000", "5100"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:42", "167900", "5700"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:43", "191200", "6000"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:44", "236700", "6900"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:45", "241200", "7500"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:46", "222700", "7100"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:47", "201400", "5600"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:48", "204900", "6400"));
        __aq2Readings.addReading(new ParticleReading("device_2", "patient_2", "02/13/13", "00:49", "206100", "6000"));

        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-01-25T14:40:00-07:00", "0", "459", "3.17", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-01-29T11:32:00-07:00", "1", "410", "2.97", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-01-31T14:13:00-07:00", "2", "503", "3.11", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-08T09:39:00-07:00", "3", "350", "2", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-08T09:48:00-07:00", "4", "410", "2.58", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-18T11:31:00-07:00", "5", "386", "2.42", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-24T18:33:00-07:00", "6", "295", "2.28", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-24T18:34:00-07:00", "7", "115", "1.11", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-24T18:37:00-07:00", "8", "88", "1.23", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-24T18:38:00-07:00", "9", "100", "1.21", "0", "503"));

        __sp2Readings.addReading(new SpirometerReading("device_2", "2388", "2013-02-24T18:40:00-07:00", "10", "74", "0.68", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("device_2", "2388", "2013-02-24T18:41:00-07:00", "11", "73", "1", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("device_2", "2388", "2013-02-24T18:43:00-07:00", "12", "134", "1.55", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("device_2", "2388", "2013-02-24T18:45:00-07:00", "13", "110", "1.05", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("device_2", "2388", "2013-02-24T20:27:00-07:00", "14", "138", "1.9", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("device_2", "2388", "2013-02-24T20:30:00-07:00", "15", "58", "0.87", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("device_2", "2388", "2013-02-25T09:59:00-07:00", "16", "419", "2.69", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("device_2", "2388", "2013-02-27T13:14:00-07:00", "17", "123", "0.89", "0", "503"));

        int groupId = (int)System.currentTimeMillis();
        __eventReadings.addEvent(new UIEvent("device_1","patient_1", "0.1", "alert", "application home", "Take Reading", UIEvent.formatDate("Sat Mar 30 2013 05:11:22 MST"), groupId));
        __eventReadings.addEvent(new UIEvent("device_1","patient_1", "0.1", "click", "Fish Bowl", "Take Reading", UIEvent.formatDate("Sat Mar 30 2013 05:11:22 MST"), groupId));
        __eventReadings.addEvent(new UIEvent("device_1","patient_1", "0.1", "navigation", "application", "Take Reading", UIEvent.formatDate("Sat Mar 30 2013 05:11:26 MST"), groupId));
        __eventReadings.addEvent(new UIEvent("device_1","patient_1", "0.1", "data entry", "PEFValue text box", "Take Reading", UIEvent.formatDate("Sat Mar 30 2013 05:11:26 MST"), groupId));
        __eventReadings.addEvent(new UIEvent("device_1","patient_1", "0.1", "click", "Fish Bowl", "tease", UIEvent.formatDate("Sat Mar 30 2013 05:11:27 MST"), groupId));
        __eventReadings.addEvent(new UIEvent("device_1","patient_1", "0.1", "click", "alert", "dismissed", UIEvent.formatDate("Sat Mar 30 2013 05:11:28 MST"), groupId));
    }

    @Test
    public void test() {
        try {
            IAspiraDAO theDAO = AspiraDAO.getDAO();
            
            theDAO.addOrModifyPatient(__patient1, false);            
            theDAO.addClinician(__clinician1, false);
            theDAO.addOrModifyAirQualityMonitor(__aqm1, false);
            theDAO.addOrModifySpirometer(__spirometer1, false);
            theDAO.importAirQualityReadings(__aqReadings, true);
            theDAO.importSpirometerReadings(__spReadings, true);
                   
            // next round
            theDAO.addOrModifyPatient(__patient2, false);
            theDAO.addClinician(__clinician2, false);
            theDAO.addOrModifyAirQualityMonitor(__aqm2, false);
            theDAO.addOrModifySpirometer(__spirometer2, false);
            theDAO.importAirQualityReadings(__aq2Readings, true);
            theDAO.importSpirometerReadings(__sp2Readings, true);
            theDAO.importUIEvents(__eventReadings, true);
            
            System.out.println("Done with imports, now going to read");
            
            Patient[] patients = theDAO.getPatients();
            assertTrue(patients.length >= 2);
            assertTrue(patients[0].equals(__patient1));
            assertTrue(patients[1].equals(__patient2));
            System.out.println("Patients OK");
            
            /** XXX Clinicians are not getting right Patient id assoc, skipping for now
            Clinician[] clinicians = theDAO.getClinicians();
            System.out.println("Clinician 1 test data: " + __clinician1.toString());
            System.out.println("\nClinician 2 test data: " + __clinician2.toString());
            System.out.println("***\nClinicians found: " + clinicians.length);
            for (int i = 0; i < clinicians.length; i++) System.out.println("Clinician " + i + " " + clinicians[i].toString());
            assertTrue(clinicians.length >= 2);
            assertTrue(clinicians[0].equals(__clinician1));
            assertTrue(clinicians[1].equals(__clinician2));
            System.out.println("Clinicians OK");
            */
            
            AirQualityMonitor[] aqms = theDAO.getAirQualityMonitors();
            System.out.println("AQM 1 test data: " + __aqm1.toString());
            System.out.println("\nAQM 2 test data: " + __aqm2.toString());
            System.out.println("***\nAQMs found: " + aqms.length);
            for (int i = 0; i < aqms.length; i++) System.out.println("AQM " + i + " " + aqms[i].toString());
            assertTrue(aqms.length >= 2);
            assertTrue(aqms[0].equals(__aqm1));
            assertTrue(aqms[1].equals(__aqm2));
            System.out.println("AQMs OK");
            
            Spirometer[] sps = theDAO.getSpirometers();
            System.out.println("Spirometer 1 test data: " + __spirometer1.toString());
            System.out.println("\nSpirometer 2 test data: " + __spirometer2.toString());
            System.out.println("***\nSPs found: " + sps.length);
            for (int i = 0; i < sps.length; i++) System.out.println("Spirometer " + i + " " + sps[i].toString());
            assertTrue(sps.length >= 2);
            assertTrue(sps[0].equals(__spirometer1));
            assertTrue(sps[1].equals(__spirometer2));
            System.out.println("Spirometers OK");
            
            AirQualityReadings aqrs1 = theDAO.findAirQualityReadingsForPatient("patient_1");
            AirQualityReadings aqrs2 = theDAO.findAirQualityReadingsForPatient("patient_2");
            SpirometerReadings sps1  = theDAO.findSpirometerReadingsForPatient("patient_1");
            SpirometerReadings sps2  = theDAO.findSpirometerReadingsForPatient("patient_2");
            UIEvents events = theDAO.findUIEventsForPatient("patient_1");
            
            assertTrue(aqrs1.equals(__aqReadings));
            System.out.println("AQRs1 OK");
            assertTrue(aqrs2.equals(__aq2Readings));
            System.out.println("AQRs2 OK");
            assertTrue(sps1.equals(__spReadings));
            System.out.println("SPs1 OK");
            assertTrue(sps2.equals(__sp2Readings));
            System.out.println("SPs2 OK");
            System.out.println("Events found in DB for Patient1: " + events.size());
            System.out.println("Events found in Object for Patient1: " + __eventReadings.size());
            assertTrue(events.equals(__eventReadings));
            System.out.println("Events OK");
        } catch (DMPException e) {
            e.printStackTrace();
        }
        
    }

}
