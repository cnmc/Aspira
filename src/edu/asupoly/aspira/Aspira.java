package edu.asupoly.aspira;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import edu.asupoly.aspira.monitorservice.MonitoringService;


public final class Aspira {
    private static final String PROPERTY_FILENAME = "properties/aspira.properties";
    private static Logger ASPIRA_LOGGER  = null;
    private static Level ASPIRA_LOGLEVEL = null;
    private static final long __startTime = System.currentTimeMillis();
            
    private static Properties __globalProperties;
   
    public static String getSpirometerId() {
        return getGlobalProperty("spirometer.id");
    }
    public static String getAirQualityMonitorId() {
        return getGlobalProperty("aqmonitor.id");
    }
    public static String getPatientId() {
        return getGlobalProperty("patient.id");
    }
    public static String getClinicianId() {
        return getGlobalProperty("clinician.id");
    }
    
    public static String getGlobalProperty(String key) {
        return __globalProperties.getProperty(key);
    }
    public static Level getAspiraLogLevel() {
        return ASPIRA_LOGLEVEL;
    }
    public static Logger getAspiraLogger() {
        return ASPIRA_LOGGER;
    }
    public static void log(Level l, String s) {
        ASPIRA_LOGGER.log(l, s);
    }
    public static void log(Level l, String s, Object o) {
        ASPIRA_LOGGER.log(l, s, o);
    }
    public static void log(Level l, String s, Object[] objs) {
        ASPIRA_LOGGER.log(l, s, objs);
    }
    public static void log(Level l, String s, Throwable t) {
        ASPIRA_LOGGER.log(l, s, t);
    } 
    public static String stackToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString(); // stack trace as a string
    }
    
    // This starts the monitoring service
    public static void main(String[] args) {
        Aspira theApp = null;
        MonitoringService theService = null;
        try {
            theApp = new Aspira();
            
            long upTime = __startTime;
            System.out.println("Started Aspira at " + new Date(__startTime));
            Aspira.log(Level.INFO, "Started Aspira at " + new Date(__startTime));
            theService = MonitoringService.getMonitoringService();
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
                    Aspira.log(Level.INFO, "Could not shutdown Aspira Monitoring Service cleanly"); 
                }
            }
            Aspira.log(Level.INFO, "Exited the Aspira Monitoring Service at " + new Date(System.currentTimeMillis()));
        }
    }   
    
    // We could get real clever and make sure our patient and devices are in the DB.
    // Now accepting on faith
    private Aspira() {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(PROPERTY_FILENAME));
            __globalProperties = new Properties();
            __globalProperties.load(isr);
            
            String globalLogLevel = __globalProperties.getProperty("log.level");
            String loggerScope = __globalProperties.getProperty("log.scope");
            if (loggerScope != null) {
                ASPIRA_LOGGER = Logger.getLogger(loggerScope.trim());
            } else {
                ASPIRA_LOGGER = Logger.getLogger("edu.asupoly.aspira");
            }
            if (globalLogLevel != null) {
                try {
                    ASPIRA_LOGLEVEL = Level.parse(globalLogLevel.trim());
                } catch (IllegalArgumentException iae) {
                    // ok to swallow as we will use the default. Log it.
                    ASPIRA_LOGLEVEL = Level.INFO;
                    ASPIRA_LOGGER.log(Level.WARNING, "Unable to initialize loglevel " + globalLogLevel + 
                            ", using " + ASPIRA_LOGLEVEL.toString());
                }
            }
            
            // now deal with log handlers.
            String logToConsole = __globalProperties.getProperty("log.logToConsole");
            if (logToConsole == null || logToConsole.trim().equals("") || logToConsole.equalsIgnoreCase("false")) {
                ASPIRA_LOGGER.setUseParentHandlers(false);
            }
            
            String logFileName = __globalProperties.getProperty("aspira.log");
            if (logFileName == null || logFileName.trim().equals("")) {
                logFileName = "aspiradefault";
            }
            logFileName = logFileName + "." + new Date(__startTime) + ".log";
            FileHandler fhandler = new FileHandler(logFileName);
            SimpleFormatter sformatter = new SimpleFormatter();
            fhandler.setFormatter(sformatter);
            ASPIRA_LOGGER.addHandler(fhandler);
        } catch (Throwable t1) {            
            System.out.println("Unable to initialize Aspira Monitoring Service, exiting");
            ASPIRA_LOGGER.log(Level.SEVERE, "Unable to initialize Aspira Monitoring Service, exiting");
            System.exit(0);
        } finally {
            try {
                isr.close();
            } catch (Throwable t) {
                ASPIRA_LOGGER.log(Level.WARNING, "Unable to close properties file input stream");
            }
        }
    }
}
