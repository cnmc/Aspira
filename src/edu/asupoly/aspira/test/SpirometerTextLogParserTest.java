package edu.asupoly.aspira.test;

import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;
import edu.asupoly.aspira.dmp.devicelogs.SpirometerTextLogParser;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.SpirometerTextReadingFactory;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author DJ
 */
public class SpirometerTextLogParserTest {
    
    public SpirometerTextLogParserTest() {
    }
    
    private SpirometerReadings __benchmarkReadings = new SpirometerReadings("device_one", "patient_one");
    Properties p = new Properties();
    @Before
    public void setUp() {
        try {
            __benchmarkReadings.addReading(new SpirometerReading("device_one", "patient_one", "2013-03- 24T17:26:00-00:00", "-1", "488.2", "145.3", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-24T23:39:15-00:00", "-1", "555.5", "111.1", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-24T23:41:57-00:00", "-1", "545.2", "111.6", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-24T23:43:26-00:00", "-1", "454.2", "141.2", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-24T23:55:42-00:00", "-1", "222.2", "111.1", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-25T00:00:42-00:00", "-1", "555.5", "111.1", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-25T00:07:02-00:00", "-1", "555.5", "111.1", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-25T00:22:38-00:00", "-1", "555.2", "111.1", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-29T13:12:48-00:00", "-1", "333.3", "122.2", "0", "-1"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one","patient_one", "2013-03-29T14:20:30-00:00", "-1", "452.2", "111.1", "0", "-1"));
        p.put("deviceid", "device_one");
        p.put("patientid", "patient_one");
        p.put("sptxtlogfile", "devicelogsamples/samplespirometerlog.txt"); 
    } catch (DeviceLogException ex) {
            Logger.getLogger(SpirometerTextLogParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of createSpirometerTextReadings method, of class SpirometerTextLogParser.
     */
    @Test
    public void testCreateSpirometerTextReadings() throws Exception {
            SpirometerTextReadingFactory factory = new SpirometerTextLogParser();
            SpirometerReadings readings = factory.createSpirometerTextReadings(p);
            assertEquals(readings, __benchmarkReadings); 
            
    }
}
