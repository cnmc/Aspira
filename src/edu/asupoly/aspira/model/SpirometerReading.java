/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 * author djawle
 */
package edu.asupoly.aspira.model;

import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpirometerReading implements java.io.Serializable, Comparable<SpirometerReading> {
    private static final long serialVersionUID = 9002395112333017198L;

    public static final int DEFAULT_NO_GROUP_ASSIGNED = -1;

    private String deviceId;
    private String pid;
    private int    measureID;
    private Date   measureDate;
    private boolean manual;
    private int    pefValue;
    private float  fev1Value;
    private int    error;
    private int    bestValue;
    private int    groupId;
    private Boolean hasSymptoms;
    
    @Override
    public String toString() {
        return "SpirometerReading(deviceId, patientId, measureId, date, manual, pef, fev1, error, bv, group) (" + deviceId + ", " +
                pid + ", " + measureID + ", " + measureDate + ", " + (manual ? "TRUE" : "FALSE") + ", " + pefValue + ", " +
                fev1Value + ", " + error + ", " + bestValue + ", " + groupId + ")";
    }
    @Override
    public int compareTo(SpirometerReading other) {
        return measureDate.compareTo(other.measureDate);
    }

    public String getDeviceId() {
        return deviceId;
    }
    public String getPatientId() {
        return pid;
    }
    public int getGroupId() {
        return groupId;
    }
    public int getMeasureID() {
        return measureID;
    }
    public Date getMeasureDate() {
        return measureDate;
    }
    public boolean getManual() {
        return manual;
    }
    public int getPEFValue() {
        return pefValue;
    }
    public float getFEV1Value() {
        return fev1Value;
    }
    public int getError() {
        return error;
    }
    public int getBestValue() {
        return bestValue;
    }
    public Boolean getHasSymptoms() {
        return hasSymptoms;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SpirometerReading && measureDate.equals(((SpirometerReading)obj).measureDate);
    }

    @Override
    public int hashCode() {
        return measureDate.hashCode();
    }

    public SpirometerReading(String deviceId, String id, String mdate, String mid, boolean manual, 
            String pef, String fev, String err, String bvalue, Boolean hasSymptoms) throws DeviceLogException {
        try{
            this.deviceId = deviceId;
            this.pid = id;
            this.measureID = Integer.parseInt(mid);
            StringTokenizer st = new StringTokenizer(mdate, "T", false);     
            mdate = st.nextToken();
            String time = st.nextToken();
            StringTokenizer _t = new StringTokenizer(time, "-", false);
            time = _t.nextToken();
            mdate = mdate + " " + time;
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            this.measureDate = df.parse(mdate);
            this.measureID = Integer.valueOf(mid.trim()).intValue();
            this.manual = manual;  // hardwire this to false as this constructor only exists for device readings
            pefValue = Float.valueOf(pef.trim()).intValue();  // yes parse as float but return cast as int
            fev1Value = Float.valueOf(fev.trim()).floatValue();
            error = Integer.parseInt(err);
            bestValue = Integer.parseInt(bvalue);
            this.hasSymptoms = hasSymptoms;  // could be null
            this.groupId = DEFAULT_NO_GROUP_ASSIGNED;
        }
        catch(Throwable th)
        {
            Logger.getLogger(SpirometerReading.class.getName()).log(Level.SEVERE, "Unable to create SpirometerReading", th);
            throw new DeviceLogException(th);
        }
    }

    public SpirometerReading(String deviceId, String id, Date mdate, int mid, boolean manual,
            int pef, float fev, int err, int bvalue, Boolean hasSymptoms, int groupid)  {

        this.deviceId = deviceId;
        this.pid = id;
        this.measureID = mid;
        this.measureDate = mdate; 
        this.manual = manual;
        pefValue = pef;
        fev1Value = fev;
        error = err;
        bestValue = bvalue;
        this.hasSymptoms = hasSymptoms;
        this.groupId = groupid;
    }
}