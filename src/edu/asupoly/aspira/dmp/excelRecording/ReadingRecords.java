package edu.asupoly.aspira.dmp.excelRecording;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.ParticleReading;

public class ReadingRecords {
	//I believe you requested that it take in an AirQualityReading
	//However, as far as I can tell there is no way, currently,
	// to get particleReadings from AirQualityReadings

	public static void ParticleandSpirometer(ParticleReading part[] , SpirometerReading spiro[], String saveLocation) throws IOException{
		int rowNumb = 1;
		Workbook wb = new HSSFWorkbook();
		Sheet mySheet = wb.createSheet("Particle and Spirometer Readings");
		Row currentRow = mySheet.createRow((short)0);
		currentRow.createCell(0).setCellValue("Particle readings:");
		currentRow.createCell(1).setCellValue("Small particles");
		currentRow.createCell(2).setCellValue("Large particles");
		CreationHelper createHelper = wb.getCreationHelper();
		
		CellStyle cellStyle = wb.createCellStyle();
	    cellStyle.setDataFormat(
	        createHelper.createDataFormat().getFormat("MM/dd/yy HH:mm"));
		
		for(int i = 0; i < part.length; i++ )
		{
			currentRow = mySheet.createRow((short)rowNumb);
			Cell cell = currentRow.createCell(0);
			cell.setCellValue(part[i].getDateTime());
			cell.setCellStyle(cellStyle);
			currentRow.createCell(1).setCellValue(part[i].getSmallParticleCount());
			currentRow.createCell(2).setCellValue(part[i].getLargeParticleCount());
			rowNumb++;
		}
		
		currentRow = mySheet.createRow((short)rowNumb);
		currentRow.createCell(0).setCellValue("Spirometer readings:");
		currentRow.createCell(1).setCellValue("pid");
		currentRow.createCell(2).setCellValue("Measure ID");
		currentRow.createCell(3).setCellValue("PEF value");
		currentRow.createCell(4).setCellValue("FEV1 value");
		currentRow.createCell(5).setCellValue("Error");
		currentRow.createCell(6).setCellValue("Best value");
		
		rowNumb++;
		
		cellStyle.setDataFormat(
		        createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss-MM:SS"));
		
		for(int i = 0; i < spiro.length; i++)
		{
			currentRow = mySheet.createRow((short)rowNumb);
			Cell cell = currentRow.createCell(0);
			cell.setCellValue(spiro[i].getMeasureDate());
			cell.setCellStyle(cellStyle);
			currentRow.createCell(1).setCellValue(spiro[i].getPid());
			currentRow.createCell(2).setCellValue(spiro[i].getMeasureID());
			currentRow.createCell(3).setCellValue(spiro[i].getPEFValue());
			currentRow.createCell(4).setCellValue(spiro[i].getFEV1Value());
			currentRow.createCell(5).setCellValue(spiro[i].getError());
			currentRow.createCell(6).setCellValue(spiro[i].getBestValue());
			rowNumb++;
		}
		
		FileOutputStream out = new FileOutputStream(saveLocation);
		wb.write(out);
		out.close();
	}
}
