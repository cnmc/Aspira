package edu.asupoly.aspira.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.asupoly.aspira.AspiraSettings;
import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.UIEvents;
import edu.asupoly.aspira.monitorservice.ServerPushTask;

@SuppressWarnings("serial")
public class AspiraImportServlet extends HttpServlet {

    private static Date lastImportTime = new Date();

    /**
     * doGet returns the time of the last successful import for patient patientid
     */
    public final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = null;
        try {
            response.setContentType("text/plain");
            out = response.getWriter();

            //No this is not good form   
            if (lastImportTime == null) {
                out.println("No imports yet");
                System.out.println("No imports yet");
            } else {
                synchronized (lastImportTime) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy at HH:mm:ss");
                    out.println(sdf.format(lastImportTime));
                    System.out.println(sdf.format(lastImportTime));
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
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
        try {
            String objectType = request.getPathInfo();
            AspiraSettings.getAspiraLogger().log(Level.INFO,"Server received push request for " + objectType);
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
                            AspiraSettings.getAspiraLogger().log(Level.INFO, "Server imported AQ Readings: " + appReturnValue);
                        } else {
                            appReturnValue = ServerPushTask.SERVER_NO_AQ_READINGS;
                        }
                    } 
                    else if (objectType.startsWith("spirometerreadings")) {
                        SpirometerReadings sprs = (SpirometerReadings)ois.readObject();
                        if (sprs != null && sprs.size() > 0) {
                            appReturnValue = (dao.importSpirometerReadings(sprs, false) ? sprs.size() : ServerPushTask.SERVER_SPIROMETER_IMPORT_FAILED);
                            AspiraSettings.getAspiraLogger().log(Level.INFO,"Server imported Spirometer Readings: " + appReturnValue);
                        } else {
                            appReturnValue = ServerPushTask.SERVER_NO_SPIROMETER_READINGS;
                        }
                    } 
                    else if (objectType.startsWith("uievents")) {
                        UIEvents events = (UIEvents)ois.readObject();
                        if (events != null && events.size() > 0) {
                            appReturnValue = (dao.importUIEvents(events, false) ? events.size() : ServerPushTask.SERVER_UIEVENT_IMPORT_FAILED);
                            AspiraSettings.getAspiraLogger().log(Level.INFO,"Server imported UI Events: " + appReturnValue);
                        } else {
                            appReturnValue = ServerPushTask.SERVER_NO_UIEVENTS;
                        }
                    }                     
                } else appReturnValue = ServerPushTask.SERVER_STREAM_ERROR;
            } else appReturnValue = ServerPushTask.SERVER_BAD_OBJECT_TYPE;
            if (appReturnValue >= 0) {
                synchronized (lastImportTime) {
                    lastImportTime = new Date();
                }
            }
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
            AspiraSettings.getAspiraLogger().log(Level.INFO,"Server returning value: " + appReturnValue);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            pw = response.getWriter();
            pw.println(""+appReturnValue);
        } catch (Throwable t3) {
            AspiraSettings.getAspiraLogger().log(Level.SEVERE, "Server pushed stacktrace on response: " + t3.getMessage());
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
}
