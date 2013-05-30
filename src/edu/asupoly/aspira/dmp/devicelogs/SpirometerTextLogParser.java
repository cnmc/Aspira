/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 */
package edu.asupoly.aspira.dmp.devicelogs;

import edu.asupoly.aspira.Aspira;
import edu.asupoly.aspira.dmp.DMPException;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.SpirometerTextReadingFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpirometerTextLogParser implements SpirometerTextReadingFactory
{
    public SpirometerTextLogParser() {}

    @Override
    public SpirometerReadings createSpirometerTextReadings(Properties props) throws Exception
    {
        String deviceId  = props.getProperty("deviceid");
        String patientId = props.getProperty("patientid");
        SpirometerReadings _spReadings = new SpirometerReadings();

        /* format this parses:
           Sun Mar 24 17:26:00 MST 2013
           PEF Value : 488.2
           FEV Value : 145.3
           Symptoms? : Yes
           Sun Mar 24 23:39:15 MST 2013
           PEF Value : 555.5
           FEV Value : 111.1
           Symptoms? : No
         
           Note the FEV Value need to be divided by 100 and the 
           PEF value must be converted to an int.
         */
        BufferedReader br = null;
        StringTokenizer st;
        try {
            String filename = props.getProperty("sptxtlogfile");
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String dateLine = null;
            String pefLine  = null;
            String fevLine  = null;
            String symptoms = null;
            
            Date _date;
            String pef, fev;
            Boolean hasSymptoms;
            dateLine = br.readLine();
            pefLine  = br.readLine();
            fevLine  = br.readLine();
            symptoms = br.readLine();
            while (dateLine != null && pefLine != null && fevLine != null && symptoms != null) {                
                dateLine = dateLine.trim();
                pefLine  = pefLine.trim();
                fevLine  = fevLine.trim();
                symptoms = symptoms.trim();
                if (!dateLine.isEmpty() && !pefLine.isEmpty() && !fevLine.isEmpty() && !symptoms.isEmpty()) {                
                    try {
                        _date = formatDate(dateLine);                    
                        st = new StringTokenizer(pefLine, ":", false);    
                        st.nextToken();
                        pef = st.nextToken().trim();                    
                        st = new StringTokenizer(fevLine, ":", false);    
                        st.nextToken();
                        fev = st.nextToken().trim();
                        st = new StringTokenizer(symptoms, ":", false);    
                        st.nextToken();
                        hasSymptoms = "Yes".equals(st.nextToken().trim());
                        _spReadings.addReading(new SpirometerReading(deviceId, patientId, _date, "-1",  true,
                                pef, fev, "0", "0", hasSymptoms));
                    } catch (Throwable it) {
                        Logger.getLogger(SpirometerTextLogParser.class.getName()).log(Level.SEVERE, 
                                "In SpirometerReadingTextParser inner " + Aspira.stackToString(it));
                    }
                }
                dateLine = br.readLine();
                pefLine  = br.readLine();
                fevLine  = br.readLine();
                symptoms = br.readLine();
            }
        }
        catch (Throwable t) {
            Logger.getLogger(SpirometerTextLogParser.class.getName()).log(Level.SEVERE, 
                    "In SpirometerReadingTextParser " + Aspira.stackToString(t));
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ie) {
                Logger.getLogger(SpirometerTextLogParser.class.getName()).log(Level.SEVERE, 
                        "Could not close spirometer text logfile ");
            }
        }
        return _spReadings;
    }

    public Date formatDate(String date) throws DMPException
    {
        if (date == null) return null;
        
        try {     
            // Spirometer txt gives this format Sun Mar 24 17:26:00 MST 2013
            SimpleDateFormat sdfOrig   = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            return sdfOrig.parse(date);
        } catch (Throwable t) {
            Logger.getLogger(SpirometerTextLogParser.class.getName()).log(Level.SEVERE, "Unable to parse date");
            throw new DMPException(t);
        }        
    }
        // This mess used to be in the method above for some reason
        /*
        // We need yyyy-MM-ddTHH:mm:ss Z
        SimpleDateFormat sdfTarget = new SimpleDateFormat("yyyy-MM-dd")
        // BTW this is awful coding, we actually string manipulate the date
        // field out of our program to look like the device XML format just
        // so the SpirometerReading constructor can treat it the same - yikes!
        StringTokenizer st = new StringTokenizer(date, " ", false);
        String mm = st.nextToken();
        mm = getMonth(st.nextToken());
        String dd = st.nextToken();
        String time = st.nextToken();
        String zone = st.nextToken().trim();
        String yy = st.nextToken();
        
        // convert Timezone 3 character string to GMT offset
        System.out.println("zone: " + zone);
        TimeZone tz = TimeZone.getTimeZone(zone);
        Calendar cal = Calendar.getInstance(tz);
        TimeZone gmttz  = TimeZone.getTimeZone("GMT");
        Calendar calgmt = Calendar.getInstance(gmttz);
        if (tz.equals(gmttz)) System.out.println("Timezones are the same");
        else System.out.println("Timezones are NOT the same");
        if (cal.equals(calgmt)) System.out.println("Calendars are the same");
        else System.out.println("Calendars are NOT the same");
        System.out.println("TZ cal in ms: " + cal.getTimeInMillis());
        System.out.println("GMT cal in ms: " + calgmt.getTimeInMillis());
        long delta = calgmt.getTimeInMillis() - cal.getTimeInMillis();
        delta = delta / (1000*60*60); // milliseconds in one hour
        System.out.println("DELTA: " + delta);
        zone = String.format("%+03d", delta) + ":00";                
        
        _dt = yy + '-' + mm + '-' + dd + 'T' + time + zone;
        System.out.println("Return date string from SP text log parser: " + _dt);
    }
    return _dt;
    */
        

    public String getMonth(String mm)
    {
        String _month = null;

        if(mm != null)
        {
            if(mm.equals("Jan"))
                _month = "01";
            else if (mm.equals("Feb"))
                _month = "02";
            else if (mm.equals("Mar"))
                _month = "03";
            else if (mm.equals("Apr"))
                _month = "04";
            else if (mm.equals("May"))
                _month = "05";
            else if (mm.equals("Jun"))
                _month = "06";
            else if (mm.equals("Jul"))
                _month = "07";
            else if (mm.equals("Aug"))
                _month = "08";
            else if (mm.equals("Sep"))
                _month = "09";
            else if (mm.equals("Oct"))
                _month = "10";
            else if (mm.equals("Nov"))
                _month = "11";
            else if (mm.equals("Dec"))
                _month = "12";
        }

        return _month;
    }
}
