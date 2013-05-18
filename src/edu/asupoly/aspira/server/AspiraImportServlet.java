package edu.asupoly.aspira.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.asupoly.aspira.Aspira;
import edu.asupoly.aspira.AspiraSettings;
import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.DMPException;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.ParticleReading;
import edu.asupoly.aspira.model.ServerPushEvent;
import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.UIEvent;
import edu.asupoly.aspira.model.UIEvents;
import edu.asupoly.aspira.monitorservice.ServerPushTask;

@SuppressWarnings("serial")
public class AspiraImportServlet extends HttpServlet {

    private static Date lastImportTime = new Date();
    private static Logger LOGGER = AspiraSettings.getAspiraLogger();
    public static final int SPIROMETER_READINGS_TYPE = 0;
    public static final int AIR_QUALITY_READINGS_TYPE = 1;
    public static final int UI_EVENTS_TYPE = 2;
    private static final String[] __TYPES = { "SpirometerReadings", "AirQualityReadings", "UIEvents" };
    
    /**
     * doGet returns the time of the last successful import for patient patientid
     */
    public final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = null;
        try {
            response.setContentType("text/plain");
            out = response.getWriter();
            Map<String, String[]> requestParams = request.getParameterMap();
            IAspiraDAO dao = AspiraDAO.getDAO();            
            for (int i = 0; i < 3; i++) {
                printByType(requestParams, dao, i, out);            
            }        
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Throwable t2) {
                LOGGER.log(Level.WARNING, "Could not flush and close output stream on doGet");
            }
        }
    }

    private void printByType(Map<String, String[]> requestParams, IAspiraDAO dao, int type, PrintWriter out) {
        ServerPushEvent spe = null;
        try {
            spe = dao.getLastServerPush(type);
        } catch (DMPException dme) {
            LOGGER.log(Level.WARNING, "Could not retrieve last server push for type " + __TYPES[type]);
        }
        
        if (spe == null) {
          out.println("\nNo server push records for type " + __TYPES[type]);
        } else {
          out.println("\nLast server push for type " + __TYPES[type] + ":\n" + spe.toString());
          String[] tail = requestParams.get(__TYPES[type]);
          if (tail != null && tail.length > 0) {
              LOGGER.log(Level.INFO, "Requesting " + tail[0] + " records for type " + __TYPES[type]);
              switch (type) {
              case SPIROMETER_READINGS_TYPE : 
                  printSpirometerReadings(dao, out, tail[0]);
                  break;
              case AIR_QUALITY_READINGS_TYPE : 
                  printAirQualityReadings(dao, out, tail[0]);
                  break;
              case UI_EVENTS_TYPE : 
                  printUIEvents(dao, out, tail[0]);
                  break;
              default:
                  out.println("\nUnknown event type passed to print: " + type);
                  break;
              }
          }
        }
    }
    
    private void printAirQualityReadings(IAspiraDAO dao, PrintWriter out, String tail) {
        try {
            AirQualityReadings sprs = null;
            int tailNum = Integer.parseInt(tail);
            if (tailNum > 0) {
                sprs = dao.findAirQualityReadingsForPatientTail(null, tailNum);
            } else {
                sprs = dao.findAirQualityReadingsForPatient(null);
            }
            if (sprs == null) {
                out.println("No Air Quality Readings available");
            } else {
                Iterator<ParticleReading> iter = sprs.iterator();
                if (iter == null) {
                    out.println("No Air Quality Readings are available");
                } else {
                    while (iter.hasNext()) {
                        out.println(iter.next().toString());
                    }
                }
            }
        } catch (Throwable ts) {
            LOGGER.log(Level.WARNING, "Unable to retrieve air quality readings " + Aspira.stackToString(ts));
            out.println("\nUnable to retrieve air quality readings\n");
        }
    }
    
    private void printSpirometerReadings(IAspiraDAO dao, PrintWriter out, String tail) {
        try {
            SpirometerReadings sprs = null;
            int tailNum = Integer.parseInt(tail);
            if (tailNum > 0) {
                sprs = dao.findSpirometerReadingsForPatientTail(null, tailNum);
            } else {
                sprs = dao.findSpirometerReadingsForPatient(null);
            }
            if (sprs == null) {
                out.println("No Spirometer Readings available");
            } else {
                Iterator<SpirometerReading> iter = sprs.iterator();
                if (iter == null) {
                    out.println("No Spirometer Readings are available");
                } else {
                    while (iter.hasNext()) {
                        out.println(iter.next().toString());
                    }
                }
            }
        } catch (Throwable ts) {
            LOGGER.log(Level.WARNING, "Unable to retrieve spirometer readings " + ts.getMessage());
            out.println("\nUnable to retrieve spirometer readings\n");
        }
    }
    
    private void printUIEvents(IAspiraDAO dao, PrintWriter out, String tail) {
        try {
            UIEvents sprs = null;
            int tailNum = Integer.parseInt(tail);
            if (tailNum > 0) {
                sprs = dao.findUIEventsForPatientTail(null, tailNum);
            } else {
                sprs = dao.findUIEventsForPatient(null);
            }
            if (sprs == null) {
                out.println("No User Interaction Events available");
            } else {
                Iterator<UIEvent> iter = sprs.iterator();
                if (iter == null) {
                    out.println("No User Interaction Event are available");
                } else {
                    while (iter.hasNext()) {
                        out.println(iter.next().toString());
                    }
                }
            }
        } catch (Throwable ts) {
            LOGGER.log(Level.WARNING, "Unable to retrieve user interaction events " + ts.getMessage());
            out.println("\nUnable to retrieve user interaction events\n");
        }
    }
    
    /**
     * Handle upload of serialized objects
     *
     * @param request HTTP Request object
     * @param response HTTP Response object
     *
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the input stream and read the object off of it
        ObjectInputStream  ois = null;
        ServletInputStream sis = null;        
        int appReturnValue = ServerPushTask.PUSH_UNSET;
        lastImportTime = new Date();
        try {
            String objectType = request.getPathInfo();
            LOGGER.log(Level.INFO,"Server received push request for " + objectType);
            if (objectType != null && objectType.length() > 0) {
                if (objectType.startsWith("/")) objectType = objectType.substring(1);
                sis = request.getInputStream();
                if (sis != null) {
                    ois = new ObjectInputStream(sis);                    
                    IAspiraDAO dao = AspiraDAO.getDAO();
                    if (objectType.startsWith("airqualityreadings")) {                        
                        AirQualityReadings aqrs = (AirQualityReadings)ois.readObject();                        
                        if (aqrs != null && aqrs.size() > 0) {
                            appReturnValue = (dao.importAirQualityReadings(aqrs, false) ? aqrs.size() : ServerPushTask.SERVER_AQ_IMPORT_FAILED);                            
                            LOGGER.log(Level.INFO, "Server imported AQ Readings: " + appReturnValue);
                        } else {
                            appReturnValue = ServerPushTask.SERVER_NO_AQ_READINGS;
                        }
                        __recordResult(dao, appReturnValue, "airqualityreadings", lastImportTime, AIR_QUALITY_READINGS_TYPE);
                    } 
                    else if (objectType.startsWith("spirometerreadings")) {
                        SpirometerReadings sprs = (SpirometerReadings)ois.readObject();
                        if (sprs != null && sprs.size() > 0) {
                            appReturnValue = (dao.importSpirometerReadings(sprs, false) ? sprs.size() : ServerPushTask.SERVER_SPIROMETER_IMPORT_FAILED);
                            LOGGER.log(Level.INFO,"Server imported Spirometer Readings: " + appReturnValue);
                        } else {
                            appReturnValue = ServerPushTask.SERVER_NO_SPIROMETER_READINGS;
                        }
                        __recordResult(dao, appReturnValue, "spirometerreadings", lastImportTime, SPIROMETER_READINGS_TYPE);
                    } 
                    else if (objectType.startsWith("uievents")) {
                        UIEvents events = (UIEvents)ois.readObject();
                        if (events != null && events.size() > 0) {
                            appReturnValue = (dao.importUIEvents(events, false) ? events.size() : ServerPushTask.SERVER_UIEVENT_IMPORT_FAILED);
                            LOGGER.log(Level.INFO,"Server imported UI Events: " + appReturnValue);
                        } else {
                            appReturnValue = ServerPushTask.SERVER_NO_UIEVENTS;
                        }
                        __recordResult(dao, appReturnValue, "uievents", lastImportTime, UI_EVENTS_TYPE);
                    }                     
                } else appReturnValue = ServerPushTask.SERVER_STREAM_ERROR;
            } else appReturnValue = ServerPushTask.SERVER_BAD_OBJECT_TYPE;
        } catch (StreamCorruptedException sce) {
            sce.printStackTrace();
            appReturnValue = ServerPushTask.SERVER_STREAM_CORRUPTED_EXCEPTION;
        } catch (IOException ie) {
            ie.printStackTrace();
            appReturnValue = ServerPushTask.SERVER_IO_EXCEPTION;
        } catch (SecurityException se) {
            se.printStackTrace();
            appReturnValue = ServerPushTask.SERVER_SECURITY_EXCEPTION;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            appReturnValue = ServerPushTask.SERVER_NULL_POINTER_EXCEPTION;
        } catch (Throwable t) {
            t.printStackTrace();
            appReturnValue = ServerPushTask.SERVER_UNKNOWN_ERROR;
        } 
        PrintWriter pw = null;
        try {
            LOGGER.log(Level.INFO,"Server returning value: " + appReturnValue);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            pw = response.getWriter();
            pw.println(""+appReturnValue);
        } catch (Throwable t3) {
            LOGGER.log(Level.SEVERE, "Server pushed stacktrace on response: " + t3.getMessage());
            t3.printStackTrace();
        } finally {        
            try {
                if (pw != null) {
                    pw.close();            
                }
                if (sis != null) sis.close();              
            } catch (Throwable t2) {
                t2.printStackTrace();
            }
        }
    }
    
    private void __recordResult(IAspiraDAO dao, int rval, String label, Date d, int type) {
        String msg = "";
        if (rval >= 0) {
            msg = "Pushed " + rval + " " + label + " to the server";            
        } else {
            msg = "Unable to push " + label + " to the server";
        }
        LOGGER.log(Level.INFO, msg);

        try {
            dao.addPushEvent(new ServerPushEvent(d, rval, type, msg));
        } catch (Throwable ts) {
            LOGGER.log(Level.WARNING, "Unable to record " + label + " push event");
        }
    }
}
