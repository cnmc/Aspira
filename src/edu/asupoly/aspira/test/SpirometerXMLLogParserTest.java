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
    
    public SpirometerXMLLogParserTest() {
    }
    
    private SpirometerReadings __benchmarkReadings = new SpirometerReadings("device_one", "patient_one");
    Properties p = new Properties();

    @Before
    public void setUp() throws Exception {
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-01-25T14:40:00-07:00", "0", "459", "3.17", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-01-29T11:32:00-07:00", "1", "410", "2.97", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-01-31T14:13:00-07:00", "2", "503", "3.11", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-08T09:39:00-07:00", "3", "350", "2", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-08T09:48:00-07:00", "4", "410", "2.58", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-18T11:31:00-07:00", "5", "386", "2.42", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:33:00-07:00", "6", "295", "2.28", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:34:00-07:00", "7", "115", "1.11", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:37:00-07:00", "8", "88", "1.23", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:38:00-07:00", "9", "100", "1.21", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:40:00-07:00", "10", "74", "0.68", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:41:00-07:00", "11", "73", "1", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:43:00-07:00", "12", "134", "1.55", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:45:00-07:00", "13", "110", "1.05", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T20:27:00-07:00", "14", "138", "1.9", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T20:30:00-07:00", "15", "58", "0.87", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-25T09:59:00-07:00", "16", "419", "2.69", "0", "503"));
        __benchmarkReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-27T13:14:00-07:00", "17", "123", "0.89", "0", "503"));
        p.put("deviceid", "device_one");
        p.put("patientid", "patient_one");
        p.put("splogfile", "devicelogsamples/2388.datAA3XML"); 
    }

    /**
     * Test of createSpirometerXMLReadings method, of class SpirometerXMLLogParser.
     */
    @Test
    public void testCreateSpirometerXMLReadings() throws Exception {
            SpirometerXMLReadingsFactory factory = new SpirometerXMLLogParser();
            SpirometerReadings readings = factory.createSpirometerXMLReadings(p);
            assertEquals(readings, __benchmarkReadings); 
            
    }
}
