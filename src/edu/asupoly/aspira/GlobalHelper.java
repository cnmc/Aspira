package edu.asupoly.aspira;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public final class GlobalHelper {
    private static final String PROPERTY_FILENAME = "properties/aspira.properties";
    public static final Logger ASPIRA_LOGGER = Logger.getLogger(GlobalHelper.class.getName());
    private static Level ASPIRA_LOGLEVEL = Level.WARNING;
    private static Properties __globalProperties;
    
    static {
     try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(PROPERTY_FILENAME));
            __globalProperties = new Properties();
            __globalProperties.load(isr);
            String logFileName = __globalProperties.getProperty("aspira.log");
            String globalLogLevel = __globalProperties.getProperty("log.level");
            if (globalLogLevel != null) {
                try {
                    ASPIRA_LOGLEVEL = Level.parse(globalLogLevel.trim());
                } catch (IllegalArgumentException iae) {
                    // ok to swallow as we will use the default above. Log it
                    ASPIRA_LOGGER.log(Level.WARNING, "Unable to initialize loglevel " + globalLogLevel + 
                            ", using " + ASPIRA_LOGLEVEL.toString());
                }
            }
            FileHandler fhandler = new FileHandler(logFileName);
            SimpleFormatter sformatter = new SimpleFormatter();
            fhandler.setFormatter(sformatter);
            ASPIRA_LOGGER.addHandler(fhandler);
        } catch (Throwable t1) {            
            System.out.println("Unable to load global properties");
            System.exit(0);
        }
    }
   
    public static Level getAspiraLogLevel() {
        return ASPIRA_LOGLEVEL;
    }
    
    public static String stackToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString(); // stack trace as a string
    }
    
    private GlobalHelper() {}
}

/*

*/