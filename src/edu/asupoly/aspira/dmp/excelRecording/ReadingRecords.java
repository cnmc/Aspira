package edu.asupoly.aspira.dmp.excelRecording;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.ParticleReading;

public class ReadingRecords {

	public static void ParticleandSpirometer(ParticleReading part , SpirometerReading spiro){
		Workbook wb = new HSSFWorkbook();
		Sheet mySheet = wb.createSheet();
	}
}
