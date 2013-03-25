package edu.asupoly.aspira.dmp.excelRecording.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;
import edu.asupoly.aspira.dmp.excelRecording.ReadingRecords;
import edu.asupoly.aspira.model.ParticleReading;
import edu.asupoly.aspira.model.SpirometerReading;

public class ReadingRecordsTest {

	Workbook testCase;
	Sheet testCaseSheet;
	Workbook test;
	Sheet testSheet;
	
	@Before
	public void setUp() throws Exception {
		//testCase = WorkbookFactory.create(new File("ExcelTest.xls"));
		//testCaseSheet = testCase.getSheetAt(0);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParticleandSpirometer() throws DeviceLogException {
		ArrayList<String> testStringCells = new ArrayList<String>();
		ArrayList<Double> testDoubleCells = new ArrayList<Double>();
		ArrayList<Date> testDateCells = new ArrayList<Date>();
		ArrayList<Boolean> testBooleanCells = new ArrayList<Boolean>();
		
		ArrayList<String> testedStringCells = new ArrayList<String>();
		ArrayList<Double> testedDoubleCells = new ArrayList<Double>();
		ArrayList<Date> testedDateCells = new ArrayList<Date>();
		ArrayList<Boolean> testedBooleanCells = new ArrayList<Boolean>();
		
		ParticleReading[] testParticles = new ParticleReading[2];
		testParticles[0] = new ParticleReading("01/01/01", "12:01", "3", "6");
		testParticles[1] = new ParticleReading("01/01/02", "12:01", "1", "0");
		SpirometerReading[] testSpiro = new SpirometerReading[2];
		testSpiro[0] = new SpirometerReading("pid", "2001-01-01T01:01:01-02:02", "1", "2", "3.5", "6", "2");
		testSpiro[1] = new SpirometerReading("pid", "2001-01-02T01:01:01-02:02", "2", "4", "4.2", "12", "1");
		
		try {
			ReadingRecords.ParticleandSpirometer(testParticles, testSpiro, "WriteTest.xls");
		} catch (IOException e1) {
			System.out.println("Save Failed");
			e1.printStackTrace();
		}
		
		try {
			test = WorkbookFactory.create(new File("WriteTest.xls"));
		} catch (InvalidFormatException e) {
			assert(false);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		testSheet = test.getSheetAt(0);
		
		
		for (Row row : testCaseSheet) {
	        for (Cell cell : row) {
	            CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
	            System.out.print(cellRef.formatAsString());
	            System.out.print(" - ");

	            switch (cell.getCellType()) {
	                case Cell.CELL_TYPE_STRING:
	                    testStringCells.add(cell.getRichStringCellValue().getString());
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                        testDateCells.add(cell.getDateCellValue());
	                    } else {
	                        testDoubleCells.add(cell.getNumericCellValue());
	                    }
	                    break;
	                case Cell.CELL_TYPE_BOOLEAN:
	                    testBooleanCells.add(cell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                    System.out.println(cell.getCellFormula());
	                    break;
	                default:
	                    System.out.println();
	            }
	        }
		}
	        
	    for (Row row : testSheet) {
		        for (Cell cell : row) {
		            CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
		            System.out.print(cellRef.formatAsString());
		            System.out.print(" - ");

		            switch (cell.getCellType()) {
		                case Cell.CELL_TYPE_STRING:
		                    testedStringCells.add(cell.getRichStringCellValue().getString());
		                    break;
		                case Cell.CELL_TYPE_NUMERIC:
		                    if (DateUtil.isCellDateFormatted(cell)) {
		                        testedDateCells.add(cell.getDateCellValue());
		                    } else {
		                        testedDoubleCells.add(cell.getNumericCellValue());
		                    }
		                    break;
		                case Cell.CELL_TYPE_BOOLEAN:
		                    testedBooleanCells.add(cell.getBooleanCellValue());
		                    break;
		                case Cell.CELL_TYPE_FORMULA:
		                    System.out.println(cell.getCellFormula());
		                    break;
		                default:
		                    System.out.println();
		            }
		        }
	    }
	    
	    assertEquals(testStringCells, testedStringCells);
	    assertEquals(testDateCells, testedDateCells);
	    assertEquals(testDoubleCells, testedDoubleCells);
	    assertEquals(testBooleanCells, testedBooleanCells);
	    
	}
		
}

