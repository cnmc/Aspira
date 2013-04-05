/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 * author djawle
 */

package edu.asupoly.aspira.model;

import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;

/**
 * KGDJ: For now we aren't worrying about the Patient model as we
 * are not using it for anything. We can construct a partial one
 * from the Spirometer reading header, but the clinician's also 
 * want to keep some stuff from their interviews.
 * Main thing is NO PII!
 * @author kevinagary
 *
 */
public class Patient implements java.io.Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8773132732758941072L;
    private String patientId;
    private String sex;
    private String patientNotes;
    private String bestValueType;
    private int bestValueTarget;
    private int rateH;
    private int rateL;
    private int valueH;
    private int valueL;
    
    public Patient(String anonId, String sex, String rH, String rL, 
                   String vH, String vL,  String btype, String bvalue, String pN) throws DeviceLogException
    {
        try {
            this.patientId = anonId;
            this.sex = sex;
            this.patientNotes = pN;
            this.bestValueType = btype;
            this.bestValueTarget = Integer.parseInt(bvalue);
            this.rateH = Integer.parseInt(rH);
            this.rateL = Integer.parseInt(rL);
            this.valueH = Integer.parseInt(vH);
            this.valueL = Integer.parseInt(vL);
        } catch (Throwable th) {
            throw new DeviceLogException(th);
        }
    }
    
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPatientNotes() {
        return patientNotes;
    }
    public void setPatientNotes(String patientNotes) {
        this.patientNotes = patientNotes;
    }
    public String getBestValueType() {
        return bestValueType;
    }
    public void setBestValueType(String bestValueType) {
        this.bestValueType = bestValueType;
    }
    public int getBestValueTarget() {
        return bestValueTarget;
    }
    public void setBestValueTarget(int bestValueTarget) {
        this.bestValueTarget = bestValueTarget;
    }
    public int getRateH() {
        return rateH;
    }
    public void setRateH(int rateH) {
        this.rateH = rateH;
    }
    public int getRateL() {
        return rateL;
    }
    public void setRateL(int rateL) {
        this.rateL = rateL;
    }
    public int getValueH() {
        return valueH;
    }
    public void setValueH(int valueH) {
        this.valueH = valueH;
    }
    public int getValueL() {
        return valueL;
    }
    public void setValueL(int valueL) {
        this.valueL = valueL;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Patient && patientId.equals(((Patient)obj).patientId);
    }
    @Override
    public int hashCode() {
        return patientId.hashCode();
    }
}