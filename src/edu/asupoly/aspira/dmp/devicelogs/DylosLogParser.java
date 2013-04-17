/*
 * ASPIRA Project
 * This program does parsing
 * of Dylos log files 
 */
package edu.asupoly.aspira.dmp.devicelogs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer; 
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.asupoly.aspira.dmp.AspiraDAODerbyImpl;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.AirQualityReadingsFactory;
import edu.asupoly.aspira.model.ParticleReading;

public class DylosLogParser implements AirQualityReadingsFactory  {
    private static final Logger LOGGER = Logger.getLogger(AspiraDAODerbyImpl.class.getName());
    
    public DylosLogParser() {}
    
    protected String _readToSectionHeading(String finput, BufferedReader br) throws Exception {       
        while (finput != null && finput.indexOf("Date/Time, Small, Large") == -1)
        {            
            finput = br.readLine();
        }
        if (finput != null) finput = br.readLine(); // line after this starts the fun        
        return finput;
    }
    
    protected void _readEntries(String deviceId, String patientId, String finput,
            BufferedReader br, AirQualityReadings aqr) throws Throwable {
        String date;
        String time;
        String small;
        String large;
        StringTokenizer st, _dt;
        String dt;
        
        while (finput != null && !finput.isEmpty() && !finput.contains("---------"))
        {
            st = new StringTokenizer(finput, ",", false);     
            dt = st.nextToken();
            _dt = new StringTokenizer(dt, " ");

            date = _dt.nextToken().trim();
            time = _dt.nextToken().trim();
            small = st.nextToken().trim();
            large = st.nextToken().trim();

            aqr.addReading(new ParticleReading(deviceId, patientId, date, time, small, large));

            finput = br.readLine(); 
        }
    }
    
    @Override
    public AirQualityReadings createAirQualityReadings(Properties props) throws Exception {    
        String deviceId =  props.getProperty("deviceid");
        String patientId = props.getProperty("patientid");
        AirQualityReadings dyloslog = new AirQualityReadings();        
        BufferedReader br = null;

        try {
            String filename = props.getProperty("aqlogfile");
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String finput = null;
            while ((finput = _readToSectionHeading(br.readLine(), br)) != null) {
                // We are positioned just fine
                _readEntries(deviceId, patientId, finput, br, dyloslog);
            }
        } catch (IOException ie) {
            LOGGER.log(Level.SEVERE, "File IO trying to parse AQ Logs", ie);
            throw new DeviceLogException(ie);
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Unknown error trying to parse AQ Logs", t);
            throw new DeviceLogException(t);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable t) {
                    LOGGER.log(Level.INFO, "Unable to close AQ Logfile");
                }
            }
        }
        return dyloslog;
    }
}
