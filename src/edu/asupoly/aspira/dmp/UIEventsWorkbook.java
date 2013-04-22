package edu.asupoly.aspira.dmp;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import edu.asupoly.aspira.model.UIEvents;
import edu.asupoly.aspira.model.UIEvent;

public class UIEventsWorkbook {
	
	Workbook __wb;
    int __rowIndex;
    Sheet __sheet;
    
    public UIEventsWorkbook(String title) {
        __wb = new HSSFWorkbook();
        __rowIndex = 0;
        __sheet = __wb.createSheet(title);
        Row currentRow = __sheet.createRow(__rowIndex++);
        currentRow.createCell(0).setCellValue("UI events:");
    }
    
    public void writeEventsFromPatient(String Patient, UIEvents events)
    {
    	writeEventsToSheet(events.getUIEventsForPatient(Patient).iterator());
    }
    
    public void writeLastNEvents(int n, UIEvents events)
    {
    	writeEventsToSheet(events.getLastNReadings(n).iterator());
    }
    
    public void writeEventsAfterDate(Date start, boolean inclusive, UIEvents events)
    {
    	writeEventsToSheet(events.getUIEventsAfter(start, inclusive).iterator());
    }
    
    public void writeEventsBeforeDate(Date end, boolean inclusive, UIEvents events)
    {
    	writeEventsToSheet(events.getUIEventsBefore(end, inclusive).iterator());
    }
    
    public void writeEventsBetweenDates(Date start, boolean includeStart,
    		Date end, boolean includeEnd, UIEvents events){
    	writeEventsToSheet(
    			events.getUIEventsBetween(start, includeStart, end, includeEnd));
    }
	
	private void writeEventsToSheet(Iterator<UIEvent> events)
	{
		Row currentRow = __sheet.createRow(__rowIndex++);
        currentRow = __sheet.createRow(__rowIndex++);
        currentRow.createCell(0).setCellValue("Patient");
        currentRow.createCell(1).setCellValue("Date");
        currentRow.createCell(3).setCellValue("Time zone");
        currentRow.createCell(4).setCellValue("Device ID");
        currentRow.createCell(5).setCellValue("Software version");
        currentRow.createCell(6).setCellValue("Event type");
        currentRow.createCell(7).setCellValue("Target");
        currentRow.createCell(8).setCellValue("Value");
        currentRow.createCell(9).setCellValue("Group id");
        CreationHelper createHelper = __wb.getCreationHelper();
		CellStyle cellStyleDate = __wb.createCellStyle();
        cellStyleDate.setDataFormat(
            createHelper.createDataFormat().getFormat("MM-dd-yy"));
        CellStyle cellStyleTime = __wb.createCellStyle();
        cellStyleTime.setDataFormat(
            createHelper.createDataFormat().getFormat("hh:mm:ss"));
        CellStyle cellStyleTimeZone = __wb.createCellStyle();
        cellStyleTimeZone.setDataFormat(
            createHelper.createDataFormat().getFormat("z"));
        
		
		while(events.hasNext())
		{
			currentRow = __sheet.createRow(__rowIndex++);
			UIEvent currentEvent = events.next();
			currentRow.createCell(0).setCellValue(currentEvent.getPatientId());
			Cell cell = currentRow.createCell(1);
			cell.setCellStyle(cellStyleDate);
			cell.setCellValue(currentEvent.getDate());
			cell = currentRow.createCell(2);
			cell.setCellStyle(cellStyleTime);
			cell.setCellValue(currentEvent.getDate());
			cell = currentRow.createCell(3);
			cell.setCellStyle(cellStyleTimeZone);
			cell.setCellValue(currentEvent.getDate());
			currentRow.createCell(4).setCellValue(currentEvent.getDeviceId());
			currentRow.createCell(5).setCellValue(currentEvent.getVersion());
			currentRow.createCell(6).setCellValue(currentEvent.getEventType());
			currentRow.createCell(7).setCellValue(currentEvent.getEventTarget());
			currentRow.createCell(8).setCellValue(currentEvent.getEventValue());
			currentRow.createCell(9).setCellValue(currentEvent.getGroupId());
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