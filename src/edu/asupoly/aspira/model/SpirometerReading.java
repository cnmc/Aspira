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
    private String pid;
    private int    measureID;
    private Date   measureDate;
    private int    pefValue;
    private float  fev1Value;
    private int    error;
    private int    bestValue;
    
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public int getMeasureID() {
        return measureID;
    }
    public void setMeasureID(int measureID) {
        this.measureID = measureID;
    }
    public Date getMeasureDate() {
        return measureDate;
    }
    public void setMeasureDate(Date measureDate) {
        this.measureDate = measureDate;
    }
    public int getPEFValue() {
        return pefValue;
    }
    public void setPEFValue(int pefValue) {
        this.pefValue = pefValue;
    }
    public float getFEV1Value() {
        return fev1Value;
    }
    public void setFEV1Value(float fev1Value) {
        this.fev1Value = fev1Value;
    }
    public int getError() {
        return error;
    }
    public void setError(int error) {
        this.error = error;
    }
    public int getBestValue() {
        return bestValue;
    }
    public void setBestValue(int bestValue) {
        this.bestValue = bestValue;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof SpirometerReading && measureDate.equals(((SpirometerReading)obj).measureDate);
    }
    
    @Override
    public int hashCode() {
        return measureDate.hashCode();
    }
    
    public SpirometerReading(String id, String mdate, String mid, String pef, String fev, String err,String bvalue) throws DeviceLogException {
        try{ 
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