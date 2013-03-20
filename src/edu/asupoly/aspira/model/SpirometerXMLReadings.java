package edu.asupoly.aspira.model;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/*
 * SpirometerXMLReadings represents the spirometer readings
 * taken by a given device for a given Patient.
 * The implementation is a SortedMap where the key is a
 * tuple <DeviceId, PatientId, measureDateTime> and the stored object
 * is a SpirometerReading.
 */
public class SpirometerXMLReadings {

       private class ReadingTuple implements Comparable<ReadingTuple> {
        String _deviceId;
        String _patientId;
        Date    _measureDate;
        
        ReadingTuple(String did, String pid, Date dt) {
            _deviceId  = did;
            _patientId = pid;
            _measureDate = dt;
        }
        
        @Override
        public int compareTo(ReadingTuple other) {
            return _measureDate.compareTo(other._measureDate);
        }
    }
       
    private SortedMap<ReadingTuple, SpirometerReading> __readings;
    
    private String __deviceId;
    private String __patientId;
    
     /*
     * To create one you have to know the device taking the readings
     * and the id of the patient to which the device is assigned
     */
    public SpirometerXMLReadings(String deviceId, String patientId) {
        __deviceId = deviceId;
        __patientId = patientId;
        __readings = new TreeMap<ReadingTuple, SpirometerReading>();
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
       SpirometerXMLReadings other = (SpirometerXMLReadings) obj;
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
            Iterator<SpirometerReading> thisIter  = __readings.values().iterator();
            Iterator<SpirometerReading> otherIter = other.__readings.values().iterator();
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
    public boolean addReading(SpirometerReading sp) {
        SpirometerReading _spReading = __readings.put(new ReadingTuple(__deviceId, __patientId, 
                                                              sp.getMeasureDate()), sp);
        if (_spReading != null) {
            //problem, something was already there, put it back
            __readings.put(new ReadingTuple(__deviceId, __patientId, _spReading.getMeasureDate()), _spReading);
            return false;
        }
        return true;
    }
    
        /*
     * This is useful for merging maps but note the "other" maps readings could overwrite
     * your own.
     */
    public boolean addReadings(SpirometerXMLReadings other) {
        if (other.__deviceId.equals(__deviceId) && other.__patientId.equals(__patientId)) {
            __readings.putAll(other.__readings);
            return true;
        }
        return false;
    }
    
    // KGDJ: Now here are a bunch of methods I'd like implemented:
    public SpirometerXMLReadings getReadingsForDate(Date d) {
        return null;
    }
    public SpirometerXMLReadings getReadingsForDateRange(Date d1, Date d2) {
        return null;
    }
    // Presumably gaps by the minute but we could add a flag
    // Might do better returning pairs of start/end of gap
    public Date[] getGaps() {
        return null;
    }
    // Overlap means intersection here, which we need to know before
    // pushing a collection to the underlying database
    public SpirometerXMLReadings getOverlap(SpirometerXMLReadings other) {
        return null;
    }
    // We may need additional operations like Union or Minus when we
    // write the persistence code.
}
