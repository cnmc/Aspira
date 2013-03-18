package edu.asupoly.aspira.model;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/*
 * AirQualityReadings represents the air quality readings
 * taken by a given device for a given Patient.
 * The implementation is a SortedMap where the key is a
 * tuple <DeviceId, PatientId, Date> and the stored object
 * is a ParticleReading.
 */
public class AirQualityReadings {

    private class KeyTuple implements Comparable<KeyTuple> {
        String deviceId;
        String patientId;
        Date   readingDate;
        
        KeyTuple(String did, String pid, Date d) {
            deviceId  = did;
            patientId = pid;
            readingDate = d;
        }
        
        @Override
        public int compareTo(KeyTuple other) {
            return readingDate.compareTo(other.readingDate);
        }
    }
    private SortedMap<KeyTuple, SpirometerReading> __readings;
    
    private String __deviceId;
    private String __patientId;
    
    /*
     * To create one you have to know the device taking the readings
     * and the id of the patient to which the device is assigned
     */
    public AirQualityReadings(String deviceId, String patientId) {
        __deviceId = deviceId;
        __patientId = patientId;
        
        __readings = new TreeMap<KeyTuple, SpirometerReading>();
    }

    public String getDeviceId()     { return __deviceId; }
    public String getPatientId()    { return __patientId; }
    
    /*
     * This is a bit loose as we haven't done anything to verify
     * the Spirometer Reading is from the same device and patient
     */
    public boolean addReading(SpirometerReading sr) {
        SpirometerReading oldSR = __readings.put(new KeyTuple(__deviceId, __patientId, 
                                                              sr.getMeasureDateTime()), sr);
        if (oldSR != null) {
            //problem, something was already there, put it back
            __readings.put(new KeyTuple(__deviceId, __patientId, oldSR.getMeasureDateTime()), oldSR);
            return false;
        }
        return true;
    }
    
    /*
     * This is useful for merging maps but note the "other" maps readings could overwrite
     * your own.
     */
    public boolean addReadings(AirQualityReadings other) {
        if (other.__deviceId.equals(__deviceId) && other.__patientId.equals(__patientId)) {
            __readings.putAll(other.__readings);
            return true;
        }
        return false;
    }
    
    // KGDJ: Now here are a bunch of methods I'd like implemented:
    public AirQualityReadings getReadingsForDate(Date d) {
        return null;
    }
    public AirQualityReadings getReadingsForDateRange(Date d1, Date d2) {
        return null;
    }
    // Presumably gaps by the minute but we could add a flag
    // Might do better returning pairs of start/end of gap
    public Date[] getGaps() {
        return null;
    }
    // Overlap means intersection here, which we need to know before
    // pushing a collection to the underlying database
    public AirQualityReadings getOverlap(AirQualityReadings other) {
        return null;
    }
    // We may need additional operations like Union or Minus when we
    // write the persistence code.
}
