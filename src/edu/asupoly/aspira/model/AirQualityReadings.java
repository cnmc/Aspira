package edu.asupoly.aspira.model;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * AirQualityReadings represents the air quality readings
 * taken by a given device for a given Patient.
 * The implementation is a SortedMap where the key is a
 * tuple <DeviceId, PatientId, Date> and the stored object
 * is a ParticleReading.
 */
public class AirQualityReadings implements java.io.Serializable {
    private static final long serialVersionUID = -1349373435898119224L;
    
    private TreeSet<ParticleReading> __readings;
    
    /*
     * To create one you have to know the device taking the readings
     * and the id of the patient to which the device is assigned
     */
    public AirQualityReadings() {
        __readings = new TreeSet<ParticleReading>();
    }

    /**
     * Get the AirQuality particle reading at a specific minute
     * @param d
     * @return a ParticleReading or null if no reading at that minute
     */
    public ParticleReading getAirQualityAt(Date d) {
        if (d == null) return null;
        
        ParticleReading pr = null;
        Iterator<ParticleReading> iterator = __readings.iterator();
        while (iterator.hasNext()) {
            pr = iterator.next();
            if (pr.getDateTime() == d) {
                return pr;
            }
        }
        return null;
    }
    
    /**
     * Get all Air Quality particle readings before a given minute
     * @param d
     * @param inclusive true if you want the given minute included in the result
     * @return an Iterator that preserves the sorted ordering of dates in ascending order
     */
    public AirQualityReadings getAirQualityBefore(Date d, boolean inclusive) {
        if (d == null) return null;
        
        SortedSet<ParticleReading> sm = __readings.headSet(new ParticleReading(null, null, d, 0, 0), inclusive);
        if (sm != null) {
            return __constructAQR(sm.iterator());  
         }
        return null;
    }
    
    public AirQualityReadings getAirQualityAfter(Date d, boolean inclusive) {
        if (d == null) return null;
        
        SortedSet<ParticleReading> sm = __readings.tailSet(new ParticleReading(null, null, d, 0, 0), inclusive);
        if (sm != null) {
            return __constructAQR(sm.iterator());
        }
        return null;
    }    
    
    public AirQualityReadings getAirQualityBetween(Date start, boolean inclstart,
                                                   Date end,   boolean inclend) {
        if (start == null || end == null) return null;
        if (__readings != null) {
            ParticleReading startPR = new ParticleReading(null, null, start, 0, 0);
            ParticleReading endPR   = new ParticleReading(null, null, end, 0, 0);
            return __constructAQR(__readings.subSet(startPR, inclstart, endPR, inclend).iterator());
        }
        return null;
    }

    public ParticleReading getFirstReading() {
        ParticleReading rval = null;
        if (__readings != null) {
            rval = __readings.first();
        }
        return rval;
    }
    
    public ParticleReading getLastReading() {
        ParticleReading rval = null;
        if (__readings != null) {
            rval = __readings.last();
        }
        return rval;
    }
    
    public AirQualityReadings getFirstNReadings(int n) {
        if (n <= 0) return null;
        
        AirQualityReadings rval = new AirQualityReadings();
        Iterator<ParticleReading> iter = __readings.iterator();
        while (iter.hasNext() && n > 0) {
            rval.addReading(iter.next());
            n--;
        }
        return rval;
    }
 
    public AirQualityReadings getLastNReadings(int n) {
        if (n <= 0) return null;
        
        AirQualityReadings rval = new AirQualityReadings();
        int size = __readings.size() - n;
        Iterator<ParticleReading> iter = __readings.iterator();
        // skip first n, none if n > number of readings we have
        while (iter.hasNext() && size > 0) {
            iter.next();
            size--;
        }
        while (iter.hasNext() && n > 0) {
            rval.addReading(iter.next());
            n--;
        }      
        return rval;
    }
    
    public AirQualityReadings getAirQualityReadingsForPatient(String patientId) {
        if (patientId == null) return null;
        
        ParticleReading pr = null;
        AirQualityReadings aqr = new AirQualityReadings();
        Iterator<ParticleReading> iter = __readings.iterator();
        while (iter.hasNext()) {
            pr = iter.next();
            if (patientId.equals(pr.getPatientId())) {
                aqr.addReading(pr);
            }
        }
        return aqr;   
    }
    
    private AirQualityReadings __constructAQR(Iterator<ParticleReading> ipr) {
        AirQualityReadings aqr = null;
        if (ipr != null) {
            aqr = new AirQualityReadings();
            while (ipr.hasNext()) {
                aqr.addReading(ipr.next());
            }
        }
        return aqr;
    }
    
    /**
     * Gets all values as a iterator in ascending order
     * @return an Iterator with all values in ascending order or null
     */
    public Iterator<ParticleReading> iterator() {
        if (__readings != null) {
            return __readings.iterator();
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        if (__readings == null) {
            if (other.__readings != null)
                return false;
        } else {
            Iterator<ParticleReading> thisIter  = __readings.iterator();
            Iterator<ParticleReading> otherIter = other.__readings.iterator();
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
    
    /*
     * This is a bit loose as we haven't done anything to verify
     * the Spirometer Reading is from the same device and patient
     */
    public boolean addReading(ParticleReading pr) {
        return __readings.add(pr);
    }
    
    /*
     * This is useful for merging maps but note the "other" maps readings could overwrite
     * your own.
     */
    public boolean addReadings(AirQualityReadings other) {       
        return __readings.addAll(other.__readings);
    }
    
    public int size() {
        if (__readings == null)  return 0;
        return __readings.size();
    }
    
    // We may need additional operations like Union or Minus when we
    // write the persistence code.
}
