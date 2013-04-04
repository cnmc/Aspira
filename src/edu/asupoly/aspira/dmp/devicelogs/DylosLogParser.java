/*
 * ASPIRA Project
 * This program does parsing
 * of Dylos log files 
 */
package edu.asupoly.aspira.dmp.devicelogs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.util.StringTokenizer; 

import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.AirQualityReadingsFactory;
import edu.asupoly.aspira.model.ParticleReading;

public class DylosLogParser implements AirQualityReadingsFactory  {
    
    public DylosLogParser() {}
    
    @Override
    public AirQualityReadings createAirQualityReadings(Properties props) throws Exception {    
        String deviceId =  props.getProperty("deviceid");
        String patientId = props.getProperty("patientid");
        AirQualityReadings dyloslog = new AirQualityReadings(deviceId, patientId);
        
        BufferedReader br = null;
        String date;
        String time;
        String small;
        String large;
        StringTokenizer st, _dt;
        String dt;

        try {
            String filename = props.getProperty("aqlogfile");
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);

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

                dyloslog.addReading(new ParticleReading(deviceId, patientId, date, time, small, large));

                finput = br.readLine(); 
            }
        } catch (Throwable t) {
            // XXX Log here
            throw new DeviceLogException(t);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable t) {
                    // XXX log here
                    // now swallow hard
                }
            }
        }
        return dyloslog;
    }
}
