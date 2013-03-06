/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files 
 */
package edu.asupoly.aspira.model;

// XXX Need to use proper datatypes and to make serializable
// XXX Need to override at least equals and hashCode
// XXX Why is there no date and time on the reading?
public class SpirometerReading {
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getMeasureID() {
        return measureID;
    }
    public void setMeasureID(String measureID) {
        this.measureID = measureID;
    }
    public String getMeasureDate() {
        return measureDate;
    }
    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }
    public String getPEFValue() {
        return pefValue;
    }
    public void setPEFValue(String pefValue) {
        this.pefValue = pefValue;
    }
    public String getFEV1Value() {
        return fev1Value;
    }
    public void setFEV1Value(String fev1Value) {
        this.fev1Value = fev1Value;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getBestValue() {
        return bestValue;
    }
    public void setBestValue(String bestValue) {
        this.bestValue = bestValue;
    }
    
    public SpirometerReading() {}
    
    private String pid;
    private String measureID;
    private String measureDate;
    private String pefValue;
    private String fev1Value;
    private String error;
    private String bestValue;
}
