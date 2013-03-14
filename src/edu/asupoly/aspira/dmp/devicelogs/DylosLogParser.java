/*
 * ASPIRA Project
 * This program does parsing
 * of Dylos log files 
 */
package edu.asupoly.aspira.dmp.devicelogs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer; 

import edu.asupoly.aspira.model.ParticleReading;

public class DylosLogParser 
{
    public static List<ParticleReading> parseLog(String filename) throws DeviceLogException {          
        List<ParticleReading> _dyloslog = new ArrayList<ParticleReading>();
        BufferedReader br = null;
        String date;
        String time;
        String small;
        String large;
        StringTokenizer st, _dt;
        String dt;

        try {
            br = new BufferedReader(new FileReader(filename));

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

		ParticleReading pr = new ParticleReading(date, time, small, large);
		_dyloslog.add(pr);

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
        return _dyloslog;
    }
}
