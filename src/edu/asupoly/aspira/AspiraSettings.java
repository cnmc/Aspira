package edu.asupoly.aspira;

import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AspiraSettings {
    private static final String PROPERTY_FILENAME = "properties/aspira.properties";
    private static final String PUSH_URL_PROPERTY_KEY = "push.url";
    private Logger ASPIRA_LOGGER  = null;
    private Level ASPIRA_LOGLEVEL = null;
    private String ASPIRA_HOME = null;
    private Properties __globalProperties;
    
    // Singleton patten. because sometimes we don't go through main (like ConfigApp
    private static AspiraSettings __appSettings;
    
    // We could get real clever and make sure our patient and devices are in the DB.
    // Now accepting on faith
    private AspiraSettings() throws Exception {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(PROPERTY_FILENAME));
                    //new InputStreamReader(new FileInputStream(PROPERTY_FILENAME));
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
            SimpleDateFormat format = new SimpleDateFormat("E_MMddyy_HHmmss");
            logFileName = ASPIRA_HOME + "logs" + File.separator + logFileName + "." + format.format(new Date()) + ".log";
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

    /**
     * This allows the __pushURL to be reset if needed. Upon setting we'll re-init URL
     */
    public boolean setURL(String url) {
        // figure the shortest possible valid URL is http://X.YYY
        boolean rval = false;
        if (url != null && url.trim().length() > 12) {  
                String pushURL = url;
                if (!pushURL.endsWith("/")) pushURL = pushURL + "/";
                __globalProperties.setProperty(PUSH_URL_PROPERTY_KEY, pushURL);
                rval = true;
        }
        return rval;
    }
    
    public static AspiraSettings getAspiraSettings() {
        if (__appSettings == null) {
            try {
                __appSettings = new AspiraSettings();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return __appSettings;
    }
    
    public static String getAirQualityMonitorId() {
        return getAspiraProperty("aqmonitor.id");
    }

    public static String getAspiraHome() {
        return AspiraSettings.getAspiraSettings().getHome();
    }

    public static Logger getAspiraLogger() {
        return AspiraSettings.getAspiraSettings().getLogger();
    }

    public static Level getAspiraLogLevel() {
        return AspiraSettings.getAspiraSettings().getLogLevel();
    }

    public static String getAspiraProperty(String key) {
        return AspiraSettings.getAspiraSettings().getGlobalProperty(key);
    }

    public static String getClinicianId() {
        return getAspiraProperty("clinician.id");
    }

    public static String getPatientId() {
        return getAspiraProperty("patient.id");
    }

    public static String getPushURL() {
        return getAspiraProperty("push.url");
    }

    public static String getSpirometerId() {
        return getAspiraProperty("spirometer.id");
    }

    public static void log(Level l, String s) {
        AspiraSettings.getAspiraLogger().log(l, s);
    }

    public static void log(Level l, String s, Object o) {
        AspiraSettings.getAspiraLogger().log(l, s, o);
    }

    public static void log(Level l, String s, Object[] objs) {
        AspiraSettings.getAspiraLogger().log(l, s, objs);
    }

    public static void log(Level l, String s, Throwable t) {
        AspiraSettings.getAspiraLogger().log(l, s, t);
    }

    public String getGlobalProperty(String key) {
        return __globalProperties.getProperty(key);
    }

    public String getHome() {
        return ASPIRA_HOME;
    }

    public Logger getLogger() {
        return ASPIRA_LOGGER;
    }

    public Level getLogLevel() {
        return ASPIRA_LOGLEVEL;
    }

}
