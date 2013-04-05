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

public class SerializedDAOTest {
    private SpirometerReadings __spReadings = null;
    private AirQualityReadings __aqReadings = null;
    private SpirometerReadings __sp2Readings = null;
    private AirQualityReadings __aq2Readings = null;
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
        __spReadings = new SpirometerReadings("device_one", "patient_one");
        __aqReadings = new AirQualityReadings("device_one", "patient_one");
        __sp2Readings = new SpirometerReadings("device_two", "patient_two");
        __aq2Readings = new AirQualityReadings("device_two", "patient_two");
        //String anonId, String sex, String rH, String rL, 
        // String vH, String vL,  String btype, String bvalue, String pN
        __patient1 = new Patient("patient_one", "M", "500", "100", "300", "100", "bvType", "400", "patient one notes");
        __patient2 = new Patient("patient_two", "F", "502", "102", "302", "102", "bvType2", "402", "patient two notes");
        __clinician1 = new Clinician("clinician_one");
        __clinician1.addPatient(__patient1);
        __clinician2 = new Clinician("clinician_two");
        __clinician2.addPatient(__patient2);
        __spirometer1 = new Spirometer("serial1", "vendor1", "model1", "desc1", __patient1);
        __spirometer2 = new Spirometer("serial2", "vendor2", "model2", "desc2", __patient2);
        __aqm1 = new AirQualityMonitor("serial1", "vendor1", "model1", "desc1", __patient1);
        __aqm2 = new AirQualityMonitor("serial2", "vendor2", "model2", "desc2", __patient2);
        
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:31", "169900", "5700"));
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:32", "166300", "5700"));
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:33", "164500", "6100"));
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:34", "165100", "6400"));
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:35", "173500", "6600"));
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:36", "178800", "6400"));
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:37", "171900", "5200"));
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:38", "170700", "7000"));
        __aqReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:38", "171500", "6100"));
        
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:39", "168800", "5800"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:40", "167000", "5900"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:41", "165000", "5100"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:42", "167900", "5700"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:43", "191200", "6000"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:44", "236700", "6900"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:45", "241200", "7500"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:46", "222700", "7100"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:47", "201400", "5600"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:48", "204900", "6400"));
        __aq2Readings.addReading(new ParticleReading("device_two", "patient_two", "02/13/13", "00:49", "206100", "6000"));

        __spReadings.addReading(new SpirometerReading("2388", "2013-01-25T14:40:00-07:00", "0", "459", "3.17", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-01-29T11:32:00-07:00", "1", "410", "2.97", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-01-31T14:13:00-07:00", "2", "503", "3.11", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-02-08T09:39:00-07:00", "3", "350", "2", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-02-08T09:48:00-07:00", "4", "410", "2.58", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-02-18T11:31:00-07:00", "5", "386", "2.42", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-02-24T18:33:00-07:00", "6", "295", "2.28", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-02-24T18:34:00-07:00", "7", "115", "1.11", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-02-24T18:37:00-07:00", "8", "88", "1.23", "0", "503"));
        __spReadings.addReading(new SpirometerReading("2388", "2013-02-24T18:38:00-07:00", "9", "100", "1.21", "0", "503"));

        __sp2Readings.addReading(new SpirometerReading("2388", "2013-02-24T18:40:00-07:00", "10", "74", "0.68", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("2388", "2013-02-24T18:41:00-07:00", "11", "73", "1", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("2388", "2013-02-24T18:43:00-07:00", "12", "134", "1.55", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("2388", "2013-02-24T18:45:00-07:00", "13", "110", "1.05", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("2388", "2013-02-24T20:27:00-07:00", "14", "138", "1.9", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("2388", "2013-02-24T20:30:00-07:00", "15", "58", "0.87", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("2388", "2013-02-25T09:59:00-07:00", "16", "419", "2.69", "0", "503"));
        __sp2Readings.addReading(new SpirometerReading("2388", "2013-02-27T13:14:00-07:00", "17", "123", "0.89", "0", "503"));
    }

    @Test
    public void test() {
        try {
            IAspiraDAO theDAO = AspiraDAO.getDAO();
            
            theDAO.addOrModifyPatient(__patient1, false);            
            theDAO.addOrModifyClinician(__clinician1, false);
            theDAO.addOrModifyAirQualityMonitor(__aqm1, false);
            theDAO.addOrModifySpirometer(__spirometer1, false);
            theDAO.importReadings(__aqReadings, __spReadings, false);
            
            
            
            // next round
            theDAO.addOrModifyPatient(__patient2, false);
            theDAO.addOrModifyClinician(__clinician2, false);
            theDAO.addOrModifyAirQualityMonitor(__aqm2, false);
            theDAO.addOrModifySpirometer(__spirometer2, false);
            theDAO.importReadings(__aqReadings, __spReadings, false);
        } catch (DMPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
