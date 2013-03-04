/*
 * ASPIRA Project
 * This program does parsing
 * of Dylos log files 
 */
package dyloslogparser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer; 

public class DylosLogParser 
{
   List<ParticleReading> _dyloslog= new ArrayList<ParticleReading>();
    
    void addEntryToLog(ParticleReading pr)
    {
        _dyloslog.add(pr);
    }
    
    void printValues()
    {
        System.out.println("Date\t\tTime\tSmall\tLarge");
        for(ParticleReading p:_dyloslog)
        {
            System.out.println(p.date + "\t" + p.time + "\t" + " " + p.small + "\t" + p.large);
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException
    {          
            if(args.length <1)
            {
                System.out.println("No log file passed as argument.");
                return;
            }
            String filename = args[0];
            BufferedReader br = new BufferedReader(new FileReader(filename)); 
            DylosLogParser _dl = new DylosLogParser();
            StringTokenizer st, _dt;
            String dt;
            
            String finput = br.readLine();
            while (finput.indexOf("Date/Time, Small, Large") == -1)
            {
                finput = br.readLine();
            }
            finput = br.readLine();
            while (finput != null)
             {
                 if( !(finput.isEmpty()) || finput.contains("-------"))
                 {
                       ParticleReading pr = new ParticleReading();
                        st = new StringTokenizer(finput, ",", false);     
                        dt = st.nextToken();
                        _dt = new StringTokenizer(dt, " ");
                        pr.date = _dt.nextToken();
                        pr.time =_dt.nextToken();
                        pr.small = st.nextToken();
                        pr.large = st.nextToken();  
                        _dl.addEntryToLog(pr);
                        finput = br.readLine();  
                 }
                 else {
                     break;
                 }
              }
            br.close(); 
            System.out.println("Completed parsing file");
            _dl.printValues();
    }
}
