/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 * author djawle
 */

package edu.asupoly.aspira.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Patient {
    
    private String patientId;
    private String familyName;
    private String givenNames;
    private String address;
    private String phoneEmail;
    private DateFormat df = new SimpleDateFormat("yyyy/mm/ddThh:mm-mm:ss");
    private Date birthday;
    private String sex;
    private String patientNotes;
    private String bestValueType;
    private int bestValueTarget;
    private int rateH;
    private int rateL;
    private int valueH;
    private int valueL;
    
    public Patient(String anonId, String fname, String name, String address, String pe, String bday, String sex, String rH, String rL, String vH, String vL, String pN, String btype, String bvalue) throws DeviceLogException
    {
        try{
            this.patientId = anonId;
            this.familyName = fname;
            this.givenNames = name;
            this.address = address;
            this.phoneEmail = pe;
            this.birthday = df.parse(bday);
            this.sex = sex;
            this.patientNotes = pN;
            this.bestValueType = btype;
            this.bestValueTarget = Integer.parseInt(bvalue);
            this.rateH = Integer.parseInt(rH);
            this.rateL = Integer.parseInt(rL);
            this.valueH = Integer.parseInt(vH);
            this.valueL = Integer.parseInt(vL);
        }catch (Throwable th) {
            throw new DeviceLogException(th);
        }
    }
    
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
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
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
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
        return obj instanceof Patient && birthday.equals(((Patient)obj).birthday);
    }
    @Override
    public int hashCode() {
        return birthday.hashCode();
    }
}