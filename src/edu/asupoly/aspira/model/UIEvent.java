 /*
 * ASPIRA Project
 * This program does parsing
 * of UIEvent file
 * author djawle
 */
package edu.asupoly.aspira.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UIEvent implements java.io.Serializable, Comparable<UIEvent> {
    private static final long serialVersionUID = 3259668122833834878L;

    public static final int DEFAULT_NO_GROUP_ASSIGNED = -1;
    
    private String deviceId;
    private String patientId;
    private String version;     // software build version
    private String eventType;   // later this ought to be an enum
    private String eventTarget; // identifies the UI widget uniquely
    private String eventValue;  // actual value may be numeric depending on type
    private Date date;
    private int groupId;

    @Override
    public int compareTo(UIEvent other) {
        return date.compareTo(other.date);
    }
    
    public UIEvent (String did, String pid, String version, String eType,
            String eTarget, String eValue, String date) {
        this(did, pid, version, eType, eTarget, eValue, formatDate(date), DEFAULT_NO_GROUP_ASSIGNED);
    }
    
    public UIEvent (String did, String pid, String version, String eType,
            String eTarget, String eValue, Date date, int gid) {

        this.deviceId    = did;
        this.patientId   = pid;
        this.version     = version;
        this.eventType   = eType;
        this.eventTarget = eTarget;
        this.eventValue  = eValue;
        this.date        = date;
        this.groupId     = gid;
    }
    
    private static Date formatDate(String date)
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
    
    public static String getMonth(String mm)
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

    public Date getDate() {
        return date;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getVersion() {
        return version;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventTarget() {
        return eventTarget;
    }

    public String getEventValue() {
        return eventValue;
    }
    
    public int getGroupId() {
        return groupId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UIEvent) {
            UIEvent other = (UIEvent)obj;
            return date.equals(other.date) &&
                patientId.equals(other.patientId) &&
                deviceId.equals(other.deviceId) &&
                eventType.equals(other.eventType) &&
                eventTarget.equals(other.eventTarget) &&
                eventValue.equals(other.eventValue) &&
                version.equals(other.version);        
        } else return false;
    }
}