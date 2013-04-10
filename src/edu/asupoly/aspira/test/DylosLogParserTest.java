package edu.asupoly.aspira.test;

import java.util.Properties;

import edu.asupoly.aspira.dmp.devicelogs.DylosLogParser;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.AirQualityReadingsFactory;
import edu.asupoly.aspira.model.ParticleReading;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public final class DylosLogParserTest {

    private AirQualityReadings __benchmarkReadings = new AirQualityReadings();
    Properties p = new Properties();

    @Before
    public void setUp() throws Exception {
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:31", "169900", "5700"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:32", "166300", "5700"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:33", "164500", "6100"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:34", "165100", "6400"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:35", "173500", "6600"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:36", "178800", "6400"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:37", "171900", "5200"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:38", "170700", "7000"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:38", "171500", "6100"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:39", "168800", "5800"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:40", "167000", "5900"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:41", "165000", "5100"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:42", "167900", "5700"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:43", "191200", "6000"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:44", "236700", "6900"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:45", "241200", "7500"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:46", "222700", "7100"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:47", "201400", "5600"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:48", "204900", "6400"));
        __benchmarkReadings.addReading(new ParticleReading("device_one", "patient_one", "02/13/13", "00:49", "206100", "6000"));

        p.put("deviceid", "device_one");
        p.put("patientid", "patient_one");
        p.put("aqlogfile", "devicelogsamples/DylosLog.txt");
    }

    @Test
    public void testFromLogfile() {
        try {
            AirQualityReadingsFactory factory = new DylosLogParser();
            AirQualityReadings readings = factory.createAirQualityReadings(p);
            assertEquals(readings, __benchmarkReadings);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
