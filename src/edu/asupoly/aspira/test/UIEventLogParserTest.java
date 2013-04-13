package edu.asupoly.aspira.test;

import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;
import edu.asupoly.aspira.dmp.devicelogs.UIEventLogParser;
import edu.asupoly.aspira.model.UIEvent;
import edu.asupoly.aspira.model.UIEvents;
import edu.asupoly.aspira.model.UIEventsFactory;

import java.util.Date;
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
public  final class UIEventLogParserTest {

    private UIEvents __eventReadings = new UIEvents();
    Properties p = new Properties();

    @Before
    public void setUp() throws DeviceLogException {
        try {
            int groupId = (int)System.currentTimeMillis();
            __eventReadings.addEvent(new UIEvent("device1","patient1", "0.1", "alert", "application home", "Take Reading", UIEvent.formatDate("Sat Mar 30 2013 05:11:22 MST"), groupId));
            __eventReadings.addEvent(new UIEvent("device1","patient1", "0.1", "click", "Fish Bowl", "Take Reading", UIEvent.formatDate("Sat Mar 30 2013 05:11:22 MST"), groupId));
            __eventReadings.addEvent(new UIEvent("device1","patient1", "0.1", "navigation", "application", "Take Reading", UIEvent.formatDate("Sat Mar 30 2013 05:11:26 MST"), groupId));
            __eventReadings.addEvent(new UIEvent("device1","patient1", "0.1", "data entry", "PEFValue text box", "Take Reading", UIEvent.formatDate("Sat Mar 30 2013 05:11:26 MST"), groupId));
            __eventReadings.addEvent(new UIEvent("device1","patient1", "0.1", "click", "Fish Bowl", "tease", UIEvent.formatDate("Sat Mar 30 2013 05:11:27 MST"), groupId));
            __eventReadings.addEvent(new UIEvent("device1","patient1", "0.1", "click", "alert", "dismissed", UIEvent.formatDate("Sat Mar 30 2013 05:11:28 MST"), groupId));

            p.put("patientid", "patient1");
            p.put("uilogfile", "devicelogsamples/sample.txt");
        }
        catch (Throwable ex) {
            Logger.getLogger(UIEventLogParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Test of createUIEvents method, of class UIEventLogParser.
     */
    @Test
    public void testCreateUIEvents() throws Exception {
        UIEventsFactory  factory = new UIEventLogParser();
        UIEvents events =  factory.createUIEvents(p);
        assertEquals(__eventReadings, events);
    }
}
