package edu.asupoly.aspira.test;

import org.junit.Before;
import org.junit.Test;

import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.DMPException;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityMonitor;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.ParticleReading;
import edu.asupoly.aspira.model.Patient;

public class AQMDAOTest {
    private AirQualityReadings __aqReadings = null;
    private AirQualityReadings __aq2Readings = null;
    private Patient __patient1 = null;
    private Patient __patient2 = null;
    private AirQualityMonitor __aqm1 = null;
    private AirQualityMonitor __aqm2 = null;
    
    @Before
    public void setUp() throws Exception {
        __aqReadings = new AirQualityReadings();

        __aq2Readings = new AirQualityReadings();
        //String anonId, String sex, String rH, String rL, 
        // String vH, String vL,  String btype, String bvalue, String pN
        __patient1 = new Patient("patient_1", "M", "500", "100", "300", "100", "bvType", "400", "patient one notes");
        __patient2 = new Patient("patient_2", "F", "502", "102", "302", "102", "bvType2", "402", "patient two notes");

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
    }

    @Test
    public void test() {
        try {
            IAspiraDAO theDAO = AspiraDAO.getDAO();
            
            theDAO.addOrModifyPatient(__patient1, false);            
            theDAO.addOrModifyAirQualityMonitor(__aqm1, false);
                   
            // next round
            theDAO.addOrModifyPatient(__patient2, false);
            theDAO.addOrModifyAirQualityMonitor(__aqm2, false);
            
            theDAO.importAirQualityReadings(__aqReadings, true);
            theDAO.importAirQualityReadings(__aq2Readings, true);

        } catch (DMPException e) {
            e.printStackTrace();
        }
        
    }

}
