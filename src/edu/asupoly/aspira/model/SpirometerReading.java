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
    private boolean hasSymptoms;
    
    @Override
    public String toString() {
        return "SpirometerReading(deviceId, patientId, measureId, date, manual, pef, fev1, error, bv, group) (" + deviceId + ", " +
                pid + ", " + measureID + ", " + measureDate + ", " + (manual ? "TRUE" : "FALSE") + ", " + pefValue + ", " +
                fev1Value + ", " + error + ", " + bestValue + ", " + (hasSymptoms ? "TRUE" : "FALSE") + groupId + ")";
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
    public boolean getHasSymptoms() {
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

    public SpirometerReading(String deviceId, String id, Date mdate, String mid, boolean manual, 
            String pef, String fev, String err, String bvalue, Boolean hasSymptoms) throws DeviceLogException {
        
        this(deviceId, id, mdate, Integer.valueOf(mid.trim()).intValue(), manual, Float.valueOf(pef.trim()).intValue(),
                Float.valueOf(fev.trim()).floatValue(), Integer.parseInt(err), Integer.parseInt(bvalue), 
                hasSymptoms, DEFAULT_NO_GROUP_ASSIGNED);
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
        if (hasSymptoms == null) hasSymptoms = false;
        this.hasSymptoms = hasSymptoms;
        this.groupId = groupid;
    }
}