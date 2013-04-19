/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asupoly.aspira.test;

import edu.asupoly.aspira.dmp.devicelogs.SpirometerXMLLogParser;
import edu.asupoly.aspira.model.SpirometerReadings;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerXMLReadingsFactory;

/**
 *
 * @author DJ
 */
public class SpirometerXMLLogParserTest {
    private static final Boolean __F = new Boolean(false);
    private static final Boolean __T = new Boolean(true);
    
    public SpirometerXMLLogParserTest() {
    }
    
    private SpirometerReadings __spReadings = new SpirometerReadings();
    Properties p = new Properties();

    @Before
    public void setUp() throws Exception {
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-01-25T14:40:00-07:00", "0", false, "459", "3.17", "0", "503", __F));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-01-29T11:32:00-07:00", "1", false, "410", "2.97", "0", "503", __F));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-01-31T14:13:00-07:00", "2", false, "503", "3.11", "0", "503", __F));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-08T09:39:00-07:00", "3", false, "350", "2", "0", "503", __F));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-08T09:48:00-07:00", "4", false, "410", "2.58", "0", "503", __F));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-18T11:31:00-07:00", "5", false, "386", "2.42", "0", "503", __T));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-24T18:33:00-07:00", "6", false, "295", "2.28", "0", "503", __F));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-24T18:34:00-07:00", "7", false, "115", "1.11", "0", "503", __F));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-24T18:37:00-07:00", "8", false, "88", "1.23", "0", "503", __T));
        __spReadings.addReading(new SpirometerReading("device_1", "2388", "2013-02-24T18:38:00-07:00", "9", false, "100", "1.21", "0", "503", __F));


        p.put("deviceid", "device_1");
        p.put("patientid", "p_1");
        p.put("splogfile", "devicelogsamples/2388.datAA3XML"); 
    }

    /**
     * Test of createSpirometerXMLReadings method, of class SpirometerXMLLogParser.
     */
    @Test
    public void testCreateSpirometerXMLReadings() throws Exception {
            SpirometerXMLReadingsFactory factory = new SpirometerXMLLogParser();
            SpirometerReadings readings = factory.createSpirometerXMLReadings(p);
            assertEquals(readings, __spReadings); 
            
    }
}
