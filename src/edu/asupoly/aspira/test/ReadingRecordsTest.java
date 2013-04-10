package edu.asupoly.aspira.test;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.asupoly.aspira.dmp.AspiraWorkbook;
import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.ParticleReading;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;

public class ReadingRecordsTest {

    Workbook testCase;
    Sheet testCaseSheet;
    Workbook test;
    Sheet testSheet;
    private SpirometerReadings __spReadings = new SpirometerReadings();
    private AirQualityReadings __aqReadings = new AirQualityReadings();

    @Before
    public void setUp() throws Exception {
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:31", "169900", "5700"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:32", "166300", "5700"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:33", "164500", "6100"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:34", "165100", "6400"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:35", "173500", "6600"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:36", "178800", "6400"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:37", "171900", "5200"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:38", "170700", "7000"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:38", "171500", "6100"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:39", "168800", "5800"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:40", "167000", "5900"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:41", "165000", "5100"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:42", "167900", "5700"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:43", "191200", "6000"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:44", "236700", "6900"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:45", "241200", "7500"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:46", "222700", "7100"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:47", "201400", "5600"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:48", "204900", "6400"));
        __aqReadings.addReading(new ParticleReading("device_one", "p_one", "02/13/13", "00:49", "206100", "6000"));

        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-01-25T14:40:00-07:00", "0", "459", "3.17", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-01-29T11:32:00-07:00", "1", "410", "2.97", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-01-31T14:13:00-07:00", "2", "503", "3.11", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-08T09:39:00-07:00", "3", "350", "2", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-08T09:48:00-07:00", "4", "410", "2.58", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-18T11:31:00-07:00", "5", "386", "2.42", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:33:00-07:00", "6", "295", "2.28", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:34:00-07:00", "7", "115", "1.11", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:37:00-07:00", "8", "88", "1.23", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:38:00-07:00", "9", "100", "1.21", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:40:00-07:00", "10", "74", "0.68", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:41:00-07:00", "11", "73", "1", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:43:00-07:00", "12", "134", "1.55", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T18:45:00-07:00", "13", "110", "1.05", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T20:27:00-07:00", "14", "138", "1.9", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-24T20:30:00-07:00", "15", "58", "0.87", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-25T09:59:00-07:00", "16", "419", "2.69", "0", "503"));
        __spReadings.addReading(new SpirometerReading("device_one", "2388", "2013-02-27T13:14:00-07:00", "17", "123", "0.89", "0", "503"));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParticleandSpirometer() throws DeviceLogException {
        AspiraWorkbook wbs = new AspiraWorkbook("Spirometer Readings");
        AspiraWorkbook wba = new AspiraWorkbook("Air Quality Readings");
        AspiraWorkbook wbb = new AspiraWorkbook("Both");
        
        try {
            wbs.exportToExcel("EmptyWorkbook.xls");
            wbs.appendFromSpirometerReadings(__spReadings.iterator());
            wba.appendFromAirQualityReadings(__aqReadings.iterator());
            wbb.appendFromAirQualityReadings(__aqReadings.iterator());
            wbb.appendFromSpirometerReadings(__spReadings.iterator());
            
            wbs.exportToExcel("SprTest.xls");
            wba.exportToExcel("AqrTest.xls");
            wbb.exportToExcel("both.xls");
        } catch (Throwable e1) {
            System.out.println("Save Failed");
            e1.printStackTrace();
        }
    }
}

