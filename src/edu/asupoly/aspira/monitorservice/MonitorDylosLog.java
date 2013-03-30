package edu.asupoly.aspira.monitorservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MonitorDylosLog {
    
        public MonitorDylosLog() {}
        
        public void MonitorDylosLogRoutine() throws FileNotFoundException{
            try {               
                // TODO: This needs to be configured
                File logtxt = new File("devicelogsamples/DylosLog.txt");

                if(logtxt.exists())
                {          
                    BufferedReader br = null;
                    String date;
                    String time;
                    String small;
                    String large;
                    StringTokenizer st, _dt;
                    String dt;

                    br = new BufferedReader(new FileReader(logtxt));

                    String finput = br.readLine();
                    while (finput.indexOf("Date/Time, Small, Large") == -1)
                    {
                          finput = br.readLine();
                    }   
                    finput = br.readLine().trim();
                    while (finput != null && !finput.isEmpty() && !finput.contains("---------"))
                    {
                        st = new StringTokenizer(finput, ",", false);     
                        dt = st.nextToken();
                        _dt = new StringTokenizer(dt, " ");
                        date = _dt.nextToken().trim();
                        time = _dt.nextToken().trim();
                        small = st.nextToken().trim();
                        large = st.nextToken().trim();          
                        //
                        // date,time, small, large needs to pushed 
                        // to local as well as remote server
                        //
                        finput = br.readLine(); 
                        }
                       ModifyConfigFile("true", "green");
                       DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
                        Date d = new Date(); 
                        String newFile = "DylosLog" + dateFormat.format(d) + ".txt";
                        System.out.println(newFile);
                        File newfile = new File(newFile);
                        logtxt.renameTo(newfile);
                }
                else
                {
                    ModifyConfigFile("false", "red");
                }
                
            }
            catch(Throwable t)
            {
                 Logger.getLogger(MonitorDylosLog.class.getName()).log(Level.SEVERE, null, t);
            }
        }
        
        public static void main(String args[])
        {
            MonitorDylosLog cdl = new MonitorDylosLog();
        try {            
            cdl.MonitorDylosLogRoutine();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MonitorDylosLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
        private static void ModifyConfigFile(String status, String zone)
        {
            try
            {
                JSONParser parser = new JSONParser();  
                // TODO : JSON file will be in My Documents
                // Need to change this path
                Object obj = parser.parse(new FileReader("tablet/AsthmaMonitoring/airQualityStatus.json"));
                File file = new File("tablet/AsthmaMonitoring/airQualityStatus.json");
                FileOutputStream fop=new FileOutputStream(file);
                JSONObject jsonObject =  (JSONObject) obj;
                JSONObject airquality =  (JSONObject) jsonObject.get("airQualityMeter");
                airquality.put("isConnected",status);
                airquality.put("readingZone", zone);
                String jsonString = jsonObject.toString();
                if(jsonString!=null) {
                    fop.write(jsonString.getBytes());
                }
                fop.flush();
                fop.close();
            }
            catch(Throwable th)
            {
                Logger.getLogger(MonitorDylosLog.class.getName()).log(Level.SEVERE, null, th);
            }
        }
            
}