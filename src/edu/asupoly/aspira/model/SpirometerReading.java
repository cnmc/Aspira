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

public class SpirometerReading implements java.io.Serializable {
    private static final long serialVersionUID = 9002395112333017198L;
    
    private String deviceId;
    private String pid;
    private int    measureID;
    private Date   measureDate;
    private int    pefValue;
    private float  fev1Value;
    private int    error;
    private int    bestValue;
    
    public String getDeviceId() {
        return deviceId;
    }
    public String getPid() {
        return pid;
    }
    public int getMeasureID() {
        return measureID;
    }
    public Date getMeasureDate() {
        return measureDate;
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
            this.deviceId = deviceId;
            this.pid = id + '\0';
            this.measureID = Integer.parseInt(mid);
             StringTokenizer st = new StringTokenizer(mdate, "T", false);     
             mdate = st.nextToken() + " " +  st.nextToken();
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss-MM:SS");
            this.measureDate = df.parse(mdate);           
            pefValue = Integer.parseInt(pef);
            fev1Value = Float.valueOf(fev.trim()).floatValue();
            error = Integer.parseInt(err);
            bestValue = Integer.parseInt(bvalue);
        }
        catch(Throwable th)
        {
             throw new DeviceLogException(th);
        }
    }
}