package edu.asupoly.aspira.monitorservice;

import java.util.Date;
import java.util.Properties;

import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.dmp.devicelogs.UIEventLogParser;
import edu.asupoly.aspira.model.UIEvent;
import edu.asupoly.aspira.model.UIEvents;
import edu.asupoly.aspira.model.UIEventsFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This task should wake up and read the ui interaction logs since the
 * last time we read them - so we have to store the last event or timestamp
 */
public class UIInteractionMonitorTask extends AspiraTimerTask {
    private static final Logger LOGGER = Logger.getLogger(UIInteractionMonitorTask.class.getName());

    private Date __lastRead;
    private Properties __props;

    public UIInteractionMonitorTask() {
        super();
        // is there a property to read here?
        // Hardwire the initial last date to something in the past
        __lastRead = new Date(0L); // Jan 1 1970, 00:00:00
    }

    public void run() {
        if (_isInitialized) {
            LOGGER.log(Level.INFO, "MonitoringService: UIMonitor executing");
            try {
                //System.out.println("Executing UI Interaction Monitor Timer Task!");
                UIEventsFactory uief = new UIEventLogParser();  // need to property-ize
                UIEvents uie = uief.createUIEvents(__props);
                // need to push these to DAO, but in the simple way we are reading in the whole
                // logfile so we have to not push those that come after lastReading
                if (uie != null) {
                    UIEvents uieAfter = uie.getUIEventsAfter(__lastRead, false);
                    if (uieAfter != null) {
                        UIEvent ue = uieAfter.getLastEvent();
                        if (ue != null) {
                            __lastRead = ue.getDate();

                            // Now we need to call DAOManager to get DAO
                            IAspiraDAO dao = AspiraDAO.getDAO();
                            dao.importUIEvents(uieAfter, true); // return a boolean if we need it 
                            // Log how many we imported here
                            LOGGER.log(Level.INFO, "Imported UIEvents " + uieAfter.size());
                        } else {
                            LOGGER.log(Level.INFO, "No last UIEvent");
                        } 
                    } else {
                        LOGGER.log(Level.INFO, "No UI Events to import");
                    }
                } else {
                    LOGGER.log(Level.INFO, "No UI eventfile to parse");
                }
            } catch (Throwable t) {
                LOGGER.log(Level.SEVERE, "UI Monitor Task throwable: " + edu.asupoly.aspira.GlobalHelper.stackToString(t));
            }
        }
    }

    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        // check we have  patientId, and file
        __props = new Properties();
        String patientId = p.getProperty("patientid");
        String logfile   = p.getProperty("uilogfile");
        if (patientId != null && logfile != null) {
            __props.setProperty("patientid", patientId);
            __props.setProperty("uilogfile", logfile);
        } else {
            rval = false;
        }
        _isInitialized = rval;
        return rval;
    }
}
