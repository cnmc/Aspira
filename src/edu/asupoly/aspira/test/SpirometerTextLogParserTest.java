package edu.asupoly.aspira.test;

import edu.asupoly.aspira.AspiraSettings;
import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;
import edu.asupoly.aspira.dmp.devicelogs.SpirometerTextLogParser;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.SpirometerTextReadingFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private static final Boolean __F = new Boolean(false);
    private static final Boolean __T = new Boolean(true);

    public SpirometerTextLogParserTest() {
    }

    private SpirometerReadings __benchmarkReadings = new SpirometerReadings();
    Properties p = new Properties();
    @Before
    public void setUp() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-24 17:26:00 -0900"), "-1", false, "488.2", "145.3", "0", "-1", __F));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-24 23:39:15 -0800"), "-1", false, "555.5", "111.1", "0", "-1", __F));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-24 23:41:57 -0700"), "-1", false, "545.2", "111.6", "0", "-1", __F));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-24 23:43:26 -0730"), "-1", false, "454.2", "141.2", "0", "-1", __F));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-24 23:55:42 -0600"), "-1", false, "222.2", "111.1", "0", "-1", __T));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-25 00:00:42 -0500"), "-1", false, "555.5", "111.1", "0", "-1", __F));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-25 00:07:02 -0400"), "-1", false, "555.5", "111.1", "0", "-1", __F));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-25 00:22:38 +0900"), "-1", false, "555.2", "111.1", "0", "-1", __F));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-29 13:12:48 +0500"), "-1", false, "333.3", "122.2", "0", "-1", __F));
            __benchmarkReadings.addReading(new SpirometerReading("device_one","p_one", df.parse("2013-03-29 14:20:30 -0000"), "-1", false, "452.2", "111.1", "0", "-1", __F));
            p.put("deviceid", "device_one");
            p.put("patientid", "p_one");
            p.put("sptxtlogfile", AspiraSettings.getAspiraHome() + "devicelogsamples/samplespirometerlog.txt"); 
        } catch (Exception ex) {
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
