package edu.asupoly.aspira;
// another comment
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Level;

import edu.asupoly.aspira.monitorservice.MonitoringService;


public final class Aspira {
   
    private static final long __startTime = System.currentTimeMillis();
    
    public static String stackToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString(); // stack trace as a string
    }
    
    // This starts the monitoring service
    public static void main(String[] args) {
        MonitoringService theService = null;
        try {            
            long upTime = __startTime;
            System.out.println("Started Aspira at " + new Date(__startTime));
            AspiraSettings settings = AspiraSettings.getAspiraSettings();
            AspiraSettings.log(Level.INFO, "Started Aspira at " + new Date(__startTime));
            theService = MonitoringService.getMonitoringService();
            if (theService == null) {
                System.out.println("EXITING: Cannot initialize the Monitoring Service");
            }
            char c = 'a';
            boolean done = false;
            do {
                c = (char) System.in.read();
                switch (c) {    
                case 'U' : 
                    upTime = System.currentTimeMillis();
                    System.out.println("Uptime: " + (upTime - __startTime)/60 + " seconds");
                    break;
                case 'C' :
                    System.out.println("\n\nSpecify a task to Cancel:");
                    String[] tasks = theService.listTasks();
                    if (tasks == null) {
                        System.out.println("No active tasks to cancel!");
                    } else {
                        int i = 0;
                         for (String task : tasks) {
                             System.out.println("\t[" + i++ + "] " + task);
                         }                         
                         c = (char) System.in.read();
                         if (Character.isDigit(c)) {
                             int tnum = Character.digit(c, 10);
                             if (tnum >= 0 && tnum < tasks.length) {
                                 theService.cancelTask(tasks[tnum]);
                             }
                         }
                    }
                    break;
                case 'L':                
                    System.out.println("\nCurrent tasks:");
                    String[] tasks2 = theService.listTasks();
                    if (tasks2 == null) {
                        System.out.println("NONE!");
                    } else {                        
                         for (String task : tasks2) {
                             System.out.println(task);
                         }
                    }
                    break;
                case 'P' :
                case 'p' :
                    System.out.println("\nOptions:\n\t[U]ptime\n\t[L]ist tasks\n\t[C]ancel tasks\n]t[Q]uit\n\t[P]rint this menu");
                    break;
                case 'Q' :
                    done = true;
                    break;
                default: 
                    System.out.println(c + " is not a valid option. Press P to list options");
                    break;
                }
            } while (!done);
            System.out.println("Shutting down the monitoring service");
            theService.shutdownService();
            System.out.println("Shut down the monitoring service");
            System.exit(0);
        } catch (Throwable t2) {
            t2.printStackTrace();
            System.exit(-1);
        } finally {
            if (theService != null) {
                try {
                    theService.shutdownService();
                } catch (Throwable te) {
                    AspiraSettings.log(Level.INFO, "Could not shutdown Aspira Monitoring Service cleanly"); 
                }
            }
            AspiraSettings.log(Level.INFO, "Exited the Aspira Monitoring Service at " + new Date(System.currentTimeMillis()));
        }
    }   
    

}
