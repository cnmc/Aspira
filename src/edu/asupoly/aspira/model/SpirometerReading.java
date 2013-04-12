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

public class SpirometerReading implements java.io.Serializable, Comparable<SpirometerReading> {
    private static final long serialVersionUID = 9002395112333017198L;
    
    private String deviceId;
    private String pid;
    private int    measureID;
    private Date   measureDate;
    private boolean manual;
    private int    pefValue;
    private float  fev1Value;
    private int    error;
    private int    bestValue;
    
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
    @Override
    public boolean equals(Object obj) {
        return obj instanceof SpirometerReading && measureDate.equals(((SpirometerReading)obj).measureDate);
    }
    
    @Override
    public int hashCode() {
        return measureDate.hashCode();
    }
    
    public SpirometerReading(String deviceId, String id, String mdate, String mid, String pef, String fev, String err,String bvalue) throws DeviceLogException {
        try{ 
            this.pid = id + '\0';
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
            this.manual = false;  // hardwire this to false as this constructor only exists for device readings
            pefValue = Integer.valueOf(pef.trim()).intValue();
            fev1Value = Float.valueOf(fev.trim()).floatValue();
            error = Integer.parseInt(err);
            bestValue = Integer.parseInt(bvalue);
        }
        catch(Throwable th)
        {
             throw new DeviceLogException(th);
        }
    }
    
    public SpirometerReading(String deviceId, String id, Date mdate, int mid, boolean manual,
            int pef, float fev, int err, int bvalue)  {
        
        this.pid = id + '\0';
        this.measureID = mid;
        this.measureDate = mdate; 
        this.manual = manual;
        pefValue = pef;
        fev1Value = fev;
        error = err;
        bestValue = bvalue;
    }
}