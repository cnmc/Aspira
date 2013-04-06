package edu.asupoly.aspira.test;

import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;
import edu.asupoly.aspira.dmp.devicelogs.UIEventLogParser;
import edu.asupoly.aspira.model.UIEvent;
import edu.asupoly.aspira.model.UIEvents;
import edu.asupoly.aspira.model.UIEventsFactory;
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
    
    private UIEvents __eventReadings = new UIEvents("patient_one");
    Properties p = new Properties();

    @Before
    public void setUp() throws DeviceLogException {
        try {
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:22 MST", "User was shown dynamic reading alert"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:22 MST", "User Started taking reading"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:26 MST", "in PEFValue text box, user entered 5"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:26 MST", "in PEFValue text box, user entered 54"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:26 MST", "in PEFValue text box, user entered 545"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:27 MST", "in PEFValue text box, user entered 545."));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:27 MST", "in PEFValue text box, user entered 545.2"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:28 MST", "User completed PEF reading"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:32 MST", "in FEVValue text box, user entered 1"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:32 MST",  "in FEVValue text box, user entered 12"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:32 MST", "in FEVValue text box, user entered 121"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:33 MST", "in FEVValue text box, user entered 121."));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:33 MST", "in FEVValue text box, user entered 121.2"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:34 MST", "User finished taking reading"));
        __eventReadings.addEvent(new UIEvent("patient_one", "Sat Mar 30 2013 05:11:37 MST","User was shown dynamic reading alert"));
        
        p.put("patientid", "patient_one");
        p.put("uilogfile", "devicelogsamples/sample.txt");
        }
        catch (DeviceLogException ex) {
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
