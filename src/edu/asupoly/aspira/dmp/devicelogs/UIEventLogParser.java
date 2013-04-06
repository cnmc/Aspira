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
    
    public UIEventLogParser() {}
    
    @Override
    public UIEvents createUIEvents(Properties props) throws Exception {    
        String patientId = props.getProperty("patientid");
        UIEvents _uiEvents = new UIEvents(patientId);
        
        BufferedReader br = null;
        String date;
        String event;
        StringTokenizer st;

        try {
            String filename = props.getProperty("uilogfile");
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String finput = br.readLine().trim();
            while (finput != null && !finput.isEmpty())
            {
                st = new StringTokenizer(finput, "-", false);     
                date = st.nextToken();
                date = st.nextToken().trim();
                event =st.nextToken().trim();
                _uiEvents.addEvent(new UIEvent(patientId, date, event));
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
        return _uiEvents;
    }
}
