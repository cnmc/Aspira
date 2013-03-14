/*
 * ASPIRA Project
 * This program does parsing
 * of Dylos log files
 */

package edu.asupoly.aspira.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.asupoly.aspira.dmp.devicelogs.DeviceLogException;

public class ParticleReading implements java.io.Serializable  {
    /**
     *
     */
    private static final long serialVersionUID = 9002395112333017198L;
    
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
    private Date dateTime;
    private int smallParticleCount;
    private int largeParticleCount;
    
    public ParticleReading(String d, String t, String s, String l) throws DeviceLogException {
        try {
            dateTime = ParticleReading.formatter.parse(d + " " + t);
            smallParticleCount = Integer.parseInt(s);
            largeParticleCount = Integer.parseInt(l);
        } catch (Throwable th) {
            throw new DeviceLogException(th);
        }
    }
    
    public Date getDateTime() {
        return dateTime;
    }
    public int getSmallParticleCount() {
        return smallParticleCount;
    }
    public int getLargeParticleCount() {
        return largeParticleCount;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ParticleReading && dateTime.equals(((ParticleReading)obj).dateTime);
    }
    
    @Override
    public int hashCode() {
        return dateTime.hashCode();
    }
    
    @Override
    public String toString() {
        return dateTime.toString() + " " + getSmallParticleCount() + " " + getLargeParticleCount();
    }
}