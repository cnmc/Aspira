package edu.asupoly.aspira.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.asupoly.aspira.Aspira;
import edu.asupoly.aspira.AspiraSettings;
import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.ParticleReading;

@SuppressWarnings("serial")
public class AspiraChartServlet extends HttpServlet {

    private static Logger LOGGER = AspiraSettings.getAspiraLogger();
    private String __html = null;
    
    public void init(ServletConfig config) throws ServletException {
        // if you forget this your getServletContext() will get a NPE! 
        super.init(config);
        String _filename = config.getInitParameter("htmltemplate");
        if (_filename == null || _filename.length() == 0) {
            throw new ServletException();
        } else {
            ServletContext sc = getServletContext();
            BufferedReader br = new BufferedReader(new InputStreamReader(sc.getResourceAsStream(_filename)));
            // read the file into htmlBuf
            StringBuffer htmlBuf = new StringBuffer();
            try {
                String nextLine = null;
                while ( (nextLine=br.readLine()) != null) {
                    htmlBuf.append(nextLine+"\n");
                }
                br.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new ServletException(e);
            }
            __html = htmlBuf.toString();
        }
    }
    
    /**
     * doGet returns the time of the last successful import for patient patientid
     */
    public final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = null;
        try {
            response.setContentType("text/html");
            out = response.getWriter();
            int numReadings = -1;
            String num = request.getParameter("num");
            String patient = request.getParameter("patient");
            try {
                if (num != null && num.length() > 0)
                    numReadings = Integer.parseInt(num);
            } catch (NumberFormatException nfe) {
                LOGGER.log(Level.WARNING, "num param missing or invalid, defaulting to all");
                numReadings = -1;
            }
            AirQualityReadings aqrs = getAirQualityReadings(patient, numReadings); 
            if (aqrs == null || aqrs.size() == 0) {
                out.println("<b>No readings available for</b> " + patient);
            } else {
                // 1. Iterate through AQRS and create the String arrays
                // 2. Sub in the file for XXX, YYY, and ZZZ
                Iterator<ParticleReading> iter = aqrs.iterator();
                StringBuffer largeArr = new StringBuffer();
                StringBuffer smallArr = new StringBuffer();
                StringBuffer rawBuf   = new StringBuffer();
                int xAxis = 0;
                for (ParticleReading pr = iter.next(); iter.hasNext(); pr = iter.next()) {
                    if (xAxis > 0) {
                        largeArr.append(",");
                        smallArr.append(",");
                    }
                    largeArr.append("["+xAxis+","+pr.getLargeParticleCount()+"]");
                    smallArr.append("["+xAxis+","+pr.getSmallParticleCount()+"]");
                    rawBuf.append("<LI>"+pr.toString()+"</LI>");
                    xAxis++;
                }
                // OK, now sub these buffers into the specified Strings
                String html = __html.replaceFirst("XXX", largeArr.toString());
                html = html.replaceFirst("YYY", smallArr.toString());
                html = html.replaceFirst("ZZZ", rawBuf.toString());
                out.println(html);
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
    
    private AirQualityReadings getAirQualityReadings(String patient, int tailNum) {
        try {
            IAspiraDAO dao = AspiraDAO.getDAO();
            AirQualityReadings sprs = null;
            if (tailNum > 0) {
                sprs = dao.findAirQualityReadingsForPatientTail(patient, tailNum);
            } else {
                sprs = dao.findAirQualityReadingsForPatient(patient);
            }
            return sprs;
        } catch (Throwable ts) {
            LOGGER.log(Level.WARNING, "Unable to retrieve air quality readings " + Aspira.stackToString(ts));
            return null;
        }
    }
}
