 /*
 * ASPIRA Project
 * This program does parsing
 * of UIEvent file
 * author djawle
 */
package edu.asupoly.aspira.model;

import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UIEvent {

    private String patientId;
    private Date date;
    private String event;

    public UIEvent (String id, String _date, String event) throws DeviceLogException {
        try {
            this.patientId = id;
            this.date = formatDate(_date);      
            this.event = event;
        } catch (Throwable th) {
            throw new DeviceLogException(th);
        }
    }
    
    private Date formatDate(String date)
    {
        //
        // Originally logs have date of this format
        // Sat Mar 30 2013 05:11:22 MST
        // We need mm-dd-yy hh:mm:ss
        //
        StringTokenizer st = new StringTokenizer(date, " ", false);
        String mm = st.nextToken();
        mm = getMonth(st.nextToken());
        String dd = st.nextToken();
        String yy = st.nextToken();
        String time = st.nextToken();
        
        String _dt = mm + '-' + dd + '-' + yy + ' ' + time;
        DateFormat df = new SimpleDateFormat("mm-dd-yyyy hh:mm:ss");
         Date dt = null;      
        try {
            dt = df.parse(_dt);
        } catch (ParseException ex) {
            Logger.getLogger(UIEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dt;
    }
    
    public String getMonth(String mm)
    {
        String _month = null;
        
        if(mm != null)
        {
            if(mm.equals("Jan"))
                    _month = "01";
            else if (mm.equals("Feb"))
                    _month = "02";
            else if (mm.equals("Mar"))
                _month = "03";
            else if (mm.equals("Apr"))
                _month = "04";
            else if (mm.equals("May"))
                    _month = "05";
            else if (mm.equals("Jun"))
                _month = "06";
            else if (mm.equals("Jul"))
                _month = "07";
            else if (mm.equals("Aug"))
                    _month = "08";
            else if (mm.equals("Sep"))
                _month = "09";
            else if (mm.equals("Oct"))
                _month = "10";
            else if (mm.equals("Nov"))
                    _month = "11";
            else if (mm.equals("Dec"))
                _month = "12";
            }
        
        return _month;
    }
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date dt) {
        this.date = dt;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UIEvent && date.equals(((UIEvent) obj).date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}