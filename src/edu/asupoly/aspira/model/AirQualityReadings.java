package edu.asupoly.aspira.model;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/*
 * AirQualityReadings represents the air quality readings
 * taken by a given device for a given Patient.
 * The implementation is a SortedMap where the key is a
 * tuple <DeviceId, PatientId, Date> and the stored object
 * is a ParticleReading.
 */
public class AirQualityReadings implements java.io.Serializable {
    private static final long serialVersionUID = -1349373435898119224L;
    
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
    private TreeMap<KeyTuple, ParticleReading> __readings;
    
    private String __deviceId;
    private String __patientId;
    private KeyTuple __forQuerying;
    
    /*
     * To create one you have to know the device taking the readings
     * and the id of the patient to which the device is assigned
     */
    public AirQualityReadings(String deviceId, String patientId) {
        __deviceId = deviceId;
        __patientId = patientId;
        __forQuerying = new KeyTuple(__deviceId, __patientId, null);
        __readings = new TreeMap<KeyTuple, ParticleReading>();
    }

    /**
     * Get the AirQuality particle reading at a specific minute
     * @param d
     * @return a ParticleReading or null if no reading at that minute
     */
    public ParticleReading getAirQualityAt(Date d) {
        __forQuerying.readingDate = d;
        return __readings.get(__forQuerying);
    }
    
    /**
     * Get all Air Quality particle readings before a given minute
     * @param d
     * @param inclusive true if you want the given minute included in the result
     * @return an Iterator that preserves the sorted ordering of dates in ascending order
     */
    public Iterator<ParticleReading> getAirQualityBefore(Date d, boolean inclusive) {
        __forQuerying.readingDate = d;
        SortedMap<KeyTuple, ParticleReading> sm = __readings.headMap(__forQuerying, inclusive);
        if (sm != null) {
            return sm.values().iterator();
        }
        return null;
    }
    
    public Iterator<ParticleReading> getAirQualityAfter(Date d, boolean inclusive) {
        __forQuerying.readingDate = d;
        SortedMap<KeyTuple, ParticleReading> sm = __readings.tailMap(__forQuerying, inclusive);
        if (sm != null) {
            return sm.values().iterator();
        }
        return null;
    }    
    
    public Iterator<ParticleReading> getAirQualityBetween(Date start, boolean inclstart,
                                                          Date end,   boolean inclend) {
        if (__readings != null) {
            __forQuerying.readingDate = start;
            KeyTuple forQ2 = new KeyTuple(__forQuerying.deviceId, __forQuerying.patientId, end);
            return __readings.subMap(__forQuerying, inclstart, forQ2, inclend).values().iterator();
        }
        return null;
    }
    /**
     * Gets all values as a iterator in ascending order
     * @return an Iterator with all values in ascending order or null
     */
    public Iterator<ParticleReading> iterator() {
        if (__readings != null) {
            return __readings.values().iterator();
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((__deviceId == null) ? 0 : __deviceId.hashCode());
        result = prime * result
                + ((__patientId == null) ? 0 : __patientId.hashCode());
        result = prime * result
                + ((__readings == null) ? 0 : __readings.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AirQualityReadings other = (AirQualityReadings) obj;
        if (__deviceId == null) {
            if (other.__deviceId != null)
                return false;
        } else if (!__deviceId.equals(other.__deviceId))
            return false;
        if (__patientId == null) {
            if (other.__patientId != null)
                return false;
        } else if (!__patientId.equals(other.__patientId))
            return false; 
        
        if (__readings == null) {
            if (other.__readings != null)
                return false;
        } else {
            Iterator<ParticleReading> thisIter  = __readings.values().iterator();
            Iterator<ParticleReading> otherIter = other.__readings.values().iterator();
            boolean equals = true;
            for (;
                 equals && thisIter.hasNext() && otherIter.hasNext(); 
                ) 
            {
                equals = equals && (thisIter.next().equals(otherIter.next()));
            }
            if (!equals) return false;
        }
        return true;
    }

    public String getDeviceId()     { return __deviceId; }
    public String getPatientId()    { return __patientId; }
    
    /*
     * This is a bit loose as we haven't done anything to verify
     * the Spirometer Reading is from the same device and patient
     */
    public boolean addReading(ParticleReading pr) {
        ParticleReading oldPR = __readings.put(new KeyTuple(__deviceId, __patientId, 
                                                              pr.getDateTime()), pr);
        if (oldPR != null) {
            //problem, something was already there, put it back
            __readings.put(new KeyTuple(__deviceId, __patientId, oldPR.getDateTime()), oldPR);
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
    
    public int size() {
        if (__readings == null)  return 0;
        return __readings.size();
    }
    
    // We may need additional operations like Union or Minus when we
    // write the persistence code.
}
