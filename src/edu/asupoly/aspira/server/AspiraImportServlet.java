package edu.asupoly.aspira.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.UIEvents;

@SuppressWarnings("serial")
public class AspiraImportServlet extends HttpServlet {

    private static Date lastImportTime = null;

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
        int appReturnValue = AspiraDAO.PUSH_UNSET;
        try {
            String objectType = request.getPathInfo();            
            if (objectType != null && objectType.length() > 0) {
                if (objectType.startsWith("/")) objectType = objectType.substring(1);
                sis = request.getInputStream();
                if (sis != null) {
                    ois = new ObjectInputStream(sis);                    
                    IAspiraDAO dao = AspiraDAO.getDAO();                    
                    if (objectType.startsWith("airqualityreadings")) {
                        AirQualityReadings aqrs = (AirQualityReadings)ois.readObject();
                        if (aqrs != null && aqrs.size() > 0) {                            
                            appReturnValue = (dao.importAirQualityReadings(aqrs, false) ? aqrs.size() : AspiraDAO.SERVER_AQ_IMPORT_FAILED);                                                        
                        } else {
                            appReturnValue = AspiraDAO.SERVER_NO_AQ_READINGS;
                        }
                    } 
                    else if (objectType.startsWith("spirometerreadings")) {
                        SpirometerReadings sprs = (SpirometerReadings)ois.readObject();
                        if (sprs != null && sprs.size() > 0) {                            
                            appReturnValue = (dao.importSpirometerReadings(sprs, false) ? sprs.size() : AspiraDAO.SERVER_SPIROMETER_IMPORT_FAILED);
                        } else {
                            appReturnValue = AspiraDAO.SERVER_NO_SPIROMETER_READINGS;
                        }
                    } 
                    else if (objectType.startsWith("uievents")) {
                        UIEvents events = (UIEvents)ois.readObject();
                        if (events != null && events.size() > 0) {
                            appReturnValue = (dao.importUIEvents(events, false) ? events.size() : AspiraDAO.SERVER_UIEVENT_IMPORT_FAILED); 
                        } else {
                            appReturnValue = AspiraDAO.SERVER_NO_UIEVENTS;
                        }
                    }                     
                } else appReturnValue = AspiraDAO.SERVER_STREAM_ERROR;
            } else appReturnValue = AspiraDAO.SERVER_BAD_OBJECT_TYPE;
            if (appReturnValue >= 0) {
                synchronized (lastImportTime) {
                    lastImportTime = new Date();
                }
            }
        } catch (StreamCorruptedException sce) {
            sce.printStackTrace();
            appReturnValue = AspiraDAO.SERVER_STREAM_CORRUPTED_EXCEPTION;
        } catch (IOException ie) {
            ie.printStackTrace();
            appReturnValue = AspiraDAO.SERVER_IO_EXCEPTION;
        } catch (SecurityException se) {
            se.printStackTrace();
            appReturnValue = AspiraDAO.SERVER_SECURITY_EXCEPTION;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            appReturnValue = AspiraDAO.SERVER_NULL_POINTER_EXCEPTION;
        } catch (Throwable t) {
            t.printStackTrace();
            appReturnValue = AspiraDAO.SERVER_UNKNOWN_ERROR;
        } 
        PrintWriter pw = null;
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            pw = response.getWriter();
            pw.println(""+appReturnValue);            
        } catch (Throwable t3) {
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
