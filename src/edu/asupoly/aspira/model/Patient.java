/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files 
 */

// XXX Need to remove all PII from here
// XXX Need to convert to proper datatypes
package edu.asupoly.aspira.model;

public class Patient {
    
    public Patient(long anonId) {
        patientId = anonId;
    }
    
    public long getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    public String getGivenNames() {
        return givenNames;
    }
    public void setGivenNames(String givenNames) {
        this.givenNames = givenNames;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneEmail() {
        return phoneEmail;
    }
    public void setPhoneEmail(String phoneEmail) {
        this.phoneEmail = phoneEmail;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
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
    public String getBestValueTarget() {
        return bestValueTarget;
    }
    public void setBestValueTarget(String bestValueTarget) {
        this.bestValueTarget = bestValueTarget;
    }
    public String getRateH() {
        return rateH;
    }
    public void setRateH(String rateH) {
        this.rateH = rateH;
    }
    public String getRateL() {
        return rateL;
    }
    public void setRateL(String rateL) {
        this.rateL = rateL;
    }
    public String getValueH() {
        return valueH;
    }
    public void setValueH(String valueH) {
        this.valueH = valueH;
    }
    public String getValueL() {
        return valueL;
    }
    public void setValueL(String valueL) {
        this.valueL = valueL;
    }

    private long patientId;
    private String familyName;
    private String givenNames;
    private String address;
    private String phoneEmail;
    private String birthday;
    private String sex;
    private String patientNotes;
    private String bestValueType;
    private String bestValueTarget;
    private String rateH;
    private String rateL;
    private String valueH;
    private String valueL;    
}


