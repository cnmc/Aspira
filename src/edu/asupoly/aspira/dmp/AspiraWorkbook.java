package edu.asupoly.aspira.dmp;

import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.ParticleReading;


// XXX
// make column mod to separate data and time as requested by Kristin
// Update the unit test, have it work together with AQR/SR unit tests
// KEEP IT SIMPLE IN THE INTERESTS OF TIME!!!
public class AspiraWorkbook {
    
    Workbook __wb;
    int __rowIndex;
    Sheet __sheet;
    
    /*
     * @param title sets the only sheet title to this string if anew workbook has been created
     */
    public AspiraWorkbook(String title) {
        __wb = new HSSFWorkbook();
        __rowIndex = 0;
        __sheet = __wb.createSheet(title);
    }
    
    /*
     * Creates or modifies the Workbook, inserting rows for spirometer readings contained in the 1st parameter
     * @param sr an iterator through spirometer readings, presumably contructed from an accessor on SpirometerReadings
     * @return void
     */
    public void appendFromSpirometerReadings(Iterator<SpirometerReading> sr) {
        Row currentRow = __sheet.createRow(__rowIndex++);
        currentRow.createCell(0).setCellValue("Spirometer readings:");
        currentRow.createCell(2).setCellValue("pid");
        currentRow.createCell(3).setCellValue("Measure ID");
        currentRow.createCell(4).setCellValue("PEF value");
        currentRow.createCell(5).setCellValue("FEV1 value");
        currentRow.createCell(6).setCellValue("Error");
        currentRow.createCell(7).setCellValue("Best value");
        
        CreationHelper createHelper = __wb.getCreationHelper();
        
        CellStyle cellStyleDate = __wb.createCellStyle();
        CellStyle cellStyleTime = __wb.createCellStyle();
        cellStyleDate.setDataFormat(
                createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
        cellStyleTime.setDataFormat(createHelper.createDataFormat().getFormat("hh:mm:ss-MM:SS"));
 
        if (sr != null) {
            while (sr.hasNext()) {
                SpirometerReading r = sr.next();         
                currentRow = __sheet.createRow(__rowIndex++);
                Cell cell = currentRow.createCell(0);
                cell.setCellValue(r.getMeasureDate());
                cell.setCellStyle(cellStyleDate);
                cell = currentRow.createCell(1);
                cell.setCellValue(r.getMeasureDate());
                cell.setCellStyle(cellStyleTime);
                currentRow.createCell(2).setCellValue(r.getPatientId());
                currentRow.createCell(3).setCellValue(r.getMeasureID());
                currentRow.createCell(4).setCellValue(r.getPEFValue());
                currentRow.createCell(5).setCellValue(r.getFEV1Value());
                currentRow.createCell(6).setCellValue(r.getError());
                currentRow.createCell(7).setCellValue(r.getBestValue());
            }
        }
    }
    
    /*
     * Creates or modifies a Workbook, inserting rows for particle readings contained in the 1st parameter
     * @param aqr a ParticleReading iterator presumably constructed from an accessor on AirQualityReadings
     * @return void
     */
    public void appendFromAirQualityReadings(Iterator<ParticleReading> aqr) {
        Row currentRow = __sheet.createRow(__rowIndex++);
        currentRow.createCell(0).setCellValue("Particle readings:");
        currentRow.createCell(2).setCellValue("Small particles");
        currentRow.createCell(3).setCellValue("Large particles");
        CreationHelper createHelper = __wb.getCreationHelper();
        
        CellStyle cellStyleDate = __wb.createCellStyle();
        cellStyleDate.setDataFormat(
            createHelper.createDataFormat().getFormat("MM/dd/yy"));
        CellStyle cellStyleTime = __wb.createCellStyle();
        cellStyleTime.setDataFormat(
            createHelper.createDataFormat().getFormat("HH:mm"));
        
        if (aqr != null) {
            while (aqr.hasNext()) {
                ParticleReading pr = aqr.next();
                currentRow = __sheet.createRow(__rowIndex++);
                Cell cell = currentRow.createCell(0);
                cell.setCellValue(pr.getDateTime());
                cell.setCellStyle(cellStyleDate);
                cell = currentRow.createCell(1);
                cell.setCellValue(pr.getDateTime());
                cell.setCellStyle(cellStyleTime);
                currentRow.createCell(2).setCellValue(pr.getSmallParticleCount());
                currentRow.createCell(3).setCellValue(pr.getLargeParticleCount());
            }
        }
    }
    
	public boolean exportToExcel(String saveLocation) {
	    FileOutputStream out = null;

	    try {
	        if (__wb != null) {
	            out = new FileOutputStream(saveLocation);
	            __wb.write(out);
	        }
	        return true;
	    } catch (Throwable t) {
	        // need to log here XXX
	        return false;
	    } finally {
	        try {
	            if (out != null) out.close();
	        } catch (Throwable t2) {
	            // XXX log again we tried to close
	        }
	    }
	}
}
