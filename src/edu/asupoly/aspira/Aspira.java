package edu.asupoly.aspira;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import edu.asupoly.aspira.monitorservice.MonitoringService;


public final class Aspira {
    private static final String PROPERTY_FILENAME = "properties/aspira.properties";
    private Logger ASPIRA_LOGGER  = null;
    private Level ASPIRA_LOGLEVEL = null;
    private static final long __startTime = System.currentTimeMillis();
    private String ASPIRA_HOME = null;
    private Properties __globalProperties;
   
    // Singleton patten. because sometimes we don't go through main (like ConfigApp
    private static Aspira __theApp;
    
    public Logger getLogger() {
        return ASPIRA_LOGGER;
    }

    public Level getLogLevel() {
        return ASPIRA_LOGLEVEL;
    }

    public String getHome() {
        return ASPIRA_HOME;
    }
    
    public static Aspira getAspiraApp() {
        if (__theApp == null) {
            try {
                __theApp = new Aspira();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return __theApp;
    }
    
    public static String getAspiraHome() {
        return Aspira.getAspiraApp().getHome();
    }
    
    public static String getSpirometerId() {
        return getAspiraProperty("spirometer.id");
    }
    public static String getAirQualityMonitorId() {
        return getAspiraProperty("aqmonitor.id");
    }
    public static String getPatientId() {
        return getAspiraProperty("patient.id");
    }
    public static String getClinicianId() {
        return getAspiraProperty("clinician.id");
    }
    
    public String getGlobalProperty(String key) {
        return __globalProperties.getProperty(key);
    }
    
    public static String getAspiraProperty(String key) {
        return Aspira.getAspiraApp().getGlobalProperty(key);
    }
    public static Level getAspiraLogLevel() {
        return Aspira.getAspiraApp().getLogLevel();
    }
    public static Logger getAspiraLogger() {
        return Aspira.getAspiraApp().getLogger();
    }
    public static void log(Level l, String s) {
        Aspira.getAspiraLogger().log(l, s);
    }
    public static void log(Level l, String s, Object o) {
        Aspira.getAspiraLogger().log(l, s, o);
    }
    public static void log(Level l, String s, Object[] objs) {
        Aspira.getAspiraLogger().log(l, s, objs);
    }
    public static void log(Level l, String s, Throwable t) {
        Aspira.getAspiraLogger().log(l, s, t);
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
            theApp = Aspira.getAspiraApp();
            if (theApp == null) {
                System.out.println("EXITING: Cannot initialize Aspira app");
                System.exit(-1);
            }
            
            long upTime = __startTime;
            System.out.println("Started Aspira at " + new Date(__startTime));
            Aspira.log(Level.INFO, "Started Aspira at " + new Date(__startTime));
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
                    Aspira.log(Level.INFO, "Could not shutdown Aspira Monitoring Service cleanly"); 
                }
            }
            Aspira.log(Level.INFO, "Exited the Aspira Monitoring Service at " + new Date(System.currentTimeMillis()));
        }
    }   
    
    // We could get real clever and make sure our patient and devices are in the DB.
    // Now accepting on faith
    private Aspira() throws Exception {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(PROPERTY_FILENAME));
            __globalProperties = new Properties();
            __globalProperties.load(isr);
            
            // check for environment variables ASPIRA_HOME and ASPIRA DATA
            ASPIRA_HOME = System.getenv("ASPIRA_HOME");
            if (ASPIRA_HOME == null) {
                // if no env variable, check for a property
                ASPIRA_HOME = __globalProperties.getProperty("aspira.home");
                if (ASPIRA_HOME == null || ASPIRA_HOME.isEmpty()) {
                    // well darnit, try a default
                    ASPIRA_HOME = "/usr/aspira/";
                }
            }
            if (!ASPIRA_HOME.endsWith(File.separator)) {
                ASPIRA_HOME = ASPIRA_HOME + File.separator;
            }
            
            // We have a value for ASPIRA_HOME but is it valid and can I write to it?
            File f = new File(ASPIRA_HOME);
            if (!f.canWrite()) {
              // write access denied
              System.out.println("No write access to ASPIRA_HOME: " + ASPIRA_HOME);
              throw new Exception("No write access to ASPIRA_HOME: " + ASPIRA_HOME);
            }
            
            // Now deal with the logger
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
            SimpleDateFormat format = new SimpleDateFormat("E_MM_dd_yy__HH_mm_ss");
            logFileName = ASPIRA_HOME + "logs" + File.separator + logFileName + "." + format.format(new Date(__startTime)) + ".log";
            FileHandler fhandler = new FileHandler(logFileName);
            SimpleFormatter sformatter = new SimpleFormatter();
            fhandler.setFormatter(sformatter);
            ASPIRA_LOGGER.addHandler(fhandler);
            
            // Tell the User
            System.out.println("Aspira system starting with values:");
            System.out.println("\tASPIRA_HOME =\t" + ASPIRA_HOME);
            System.out.println("\tLog file = \t" + logFileName);
            System.out.println("\tLog Level =\t" + ASPIRA_LOGLEVEL.toString());
        } catch (Throwable t1) {            
            System.out.println("Unable to initialize Aspira Monitoring Service, exiting");
            t1.printStackTrace();
            System.exit(0);
        } finally {
            try {
                isr.close();
            } catch (Throwable t) {
            }
        }
    }
}
