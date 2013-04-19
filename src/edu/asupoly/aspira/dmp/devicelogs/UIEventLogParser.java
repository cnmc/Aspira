/*
 * ASPIRA Project
 * This program does parsing
 * of Dylos log files 
 */
package edu.asupoly.aspira.dmp.devicelogs;

import edu.asupoly.aspira.model.UIEvent;
import edu.asupoly.aspira.model.UIEvents;
import edu.asupoly.aspira.model.UIEventsFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UIEventLogParser implements UIEventsFactory  {
    public final String UI_EVENT_DELIMITER = "###";
    
    public UIEventLogParser() {}
    
    @Override
    public UIEvents createUIEvents(Properties props) throws Exception {    
        UIEvents uiEvents = new UIEvents();
                
        BufferedReader br = null;
        String eventType;
        String target;
        String deviceid;
        String patientid;
        String versionid;
        String eventValue;
        String eventTimestamp;
        StringTokenizer st;

        try {
            String filename = props.getProperty("uilogfile");           
            br = new BufferedReader(new FileReader(filename));
            String finput = br.readLine();
            while (finput != null && !finput.isEmpty())
            {
                finput = finput.trim();
                try {
                    st = new StringTokenizer(finput, UI_EVENT_DELIMITER, false);
                    // first 5 tokens are deviceid, patientid, build version, event type, target
                    deviceid  = st.nextToken().trim();
                    patientid = st.nextToken().trim();
                    versionid = st.nextToken().trim();
                    eventType = st.nextToken().trim();
                    target    = st.nextToken().trim();
                    // the 6th token is always the event value, but the semantics of it
                    // are determined by the eventType
                    eventValue = st.nextToken().trim();
                    // 7th is always the timestamp
                    eventTimestamp = st.nextToken().trim();

                    // Ideally we would have a factory method create a proper subclass type
                    // of event. But we will stick with just shoving it to an event as Strings
                    //_uiEvents.addEvent(new UIEvent(patientId, date, event));
                    uiEvents.addEvent(new UIEvent(deviceid, patientid, versionid, eventType, target, eventValue, eventTimestamp));            
                } catch (Throwable ti) {
                    // if there is a bogus line, still get the others
                    Logger.getLogger(UIEventLogParser.class.getName()).log(Level.SEVERE, "Unable to parse line " + finput, ti);
                }
                finput = br.readLine(); 
            }
        } catch (Throwable t) {
            Logger.getLogger(UIEventLogParser.class.getName()).log(Level.SEVERE, null, t);
            throw new DeviceLogException(t);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable t) {
                    Logger.getLogger(UIEventLogParser.class.getName()).log(Level.SEVERE, null, t);
                }
            }
        }
        return uiEvents;
    }
}
