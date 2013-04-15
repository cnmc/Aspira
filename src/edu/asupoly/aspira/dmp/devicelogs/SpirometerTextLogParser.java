/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 */
package edu.asupoly.aspira.dmp.devicelogs;

import edu.asupoly.aspira.GlobalHelper;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.SpirometerTextReadingFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
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

        BufferedReader br = null;
        StringTokenizer st;
        try {
            String filename = props.getProperty("sptxtlogfile");
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String finput = br.readLine().trim();
            String _date,  pef, fev;
            while (finput != null && !finput.isEmpty())
            {
                _date = formatDate(finput);
                finput = br.readLine(); 
                st = new StringTokenizer(finput, ":", false);    
                pef = st.nextToken();
                pef = st.nextToken().trim();
                finput = br.readLine();
                st = new StringTokenizer(finput, ":", false);    
                fev = st.nextToken();
                fev = st.nextToken().trim();
                finput = br.readLine();
                // For now sending measure id as -1, error as 0 and best value as -1
                try {  // if it doesn't work we'll skip it                    
                    _spReadings.addReading(new SpirometerReading(deviceId, patientId, _date, "-1",  true,
                        pef, fev, "0", "0"));
                } catch (Throwable it) {
                    Logger.getLogger(SpirometerTextLogParser.class.getName()).log(Level.SEVERE, 
                            "In SpirometerReadingTextParser inner " + GlobalHelper.stackToString(it));
                }
            }
        }
        catch (Throwable t) {
            Logger.getLogger(SpirometerTextLogParser.class.getName()).log(Level.SEVERE, 
                    "In SpirometerReadingTextParser " + GlobalHelper.stackToString(t));
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

    public String formatDate(String date)
    {
        String _dt = null;
        if(date != null)
        {
            // Spirometer txt gives this format Sun Mar 24 17:26:00 MST 2013
            // We need yyyy-mm-ddThh:mm:ss-MM:SS
            StringTokenizer st = new StringTokenizer(date, " ", false);
            String mm = st.nextToken();
            mm = getMonth(st.nextToken());
            String dd = st.nextToken();
            String time = st.nextToken();
            String yy = st.nextToken();
            yy = st.nextToken();
            String zone = "00:00";
            _dt = yy + '-' + mm + '-' + dd + 'T' + time + '-' +  zone;
        }
        return _dt;
    }

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
