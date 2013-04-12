package edu.asupoly.aspira.model;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * SpirometerReadings represents the spirometer readings
 * taken by a given device for a given Patient.
 * The implementation is a SortedMap where the key is a
 * tuple <DeviceId, PatientId, measureDateTime> and the stored object
 * is a SpirometerReading.
 */
public class SpirometerReadings implements java.io.Serializable {
    private static final long serialVersionUID = 3310136226572039162L;

    private TreeSet<SpirometerReading> __readings;
    
    public SpirometerReadings() {
        __readings = new TreeSet<SpirometerReading>();
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
        SpirometerReadings other = (SpirometerReadings) obj;

        if (__readings == null) {
            if (other.__readings != null)
                return false;
        } else {
            Iterator<SpirometerReading> thisIter  = __readings.iterator();
            Iterator<SpirometerReading> otherIter = other.__readings.iterator();
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

    public SpirometerReadings getSpirometerReadingsForPatient(String patientId) {
        if (patientId == null) return null;
        
        SpirometerReading sr = null;
        SpirometerReadings spr = new SpirometerReadings();
        Iterator<SpirometerReading> iter = __readings.iterator();
        while (iter.hasNext()) {
            sr = iter.next();
            if (patientId.equals(sr.getPatientId())) {
                spr.addReading(sr);
            }
        }
        return spr;   
    }
    
    /**
     * Get the Spirometer reading at a specific minute
     * @param d
     * @return a SpirometerReading or null if no reading at that minute
     */
    public SpirometerReading getSpirometerReadingAt(Date d) {
        if (d == null) return null;

        SpirometerReading sr = null;
        Iterator<SpirometerReading> iterator = __readings.iterator();
        while (iterator.hasNext()) {
            sr = iterator.next();
            if (sr.getMeasureDate() == d) {
                return sr;
            }
        }
        return null;
    }
    
    /**
     * Get all Spirometer readings before a given minute
     * @param d
     * @return an Iterator that preserves the sorted ordering of dates in ascending order
     */
    public SpirometerReadings getSpirometerReadingsBefore(Date d) {
        SortedSet<SpirometerReading> sm = __readings.headSet(new SpirometerReading(null, null, d, 0, true, 0, 0.0f, 0, 0, 0), true);
        if (sm != null) {
            return __constructSPR(sm.iterator());
        }
        return null;
    }
     
    /**
     * Get all Spirometer readings after a given minute
     * @param d
     * @return an Iterator that preserves the sorted ordering of dates in ascending order
     */
    public SpirometerReadings getSpirometerReadingsAfter(Date d) {
        SortedSet<SpirometerReading> sm = __readings.tailSet(new SpirometerReading(null, null, d, 0, true, 0, 0.0f, 0, 0, 0), true);
        if (sm != null) {
            return __constructSPR(sm.iterator());
        }
        return null;
    }    
    
    /**
     * Get all Spirometer readings between 2 given minutes
     * @param start the begin date and time, inclusive
     * @param end the ending date and time, inclusive
     * @return an Iterator that preserves the sorted ordering of dates in ascending order
     */
    public Iterator<SpirometerReading> getSpirometerReadingsBetween(Date start, Date end) {
        if (__readings != null) {
            SortedSet<SpirometerReading> res = 
                    __readings.subSet(new SpirometerReading(null, null, start, 0, true, 0, 0.0f, 0, 0, 0), true, 
                                      new SpirometerReading(null, null, end, 0, true, 0, 0.0f, 0, 0, 0), true);
            if (res != null) {
                return res.iterator();
            }
        }
        return null;
    }
    
    /**
     * Gets all values as a iterator in ascending order
     * @return an Iterator with all values in ascending order or null
     */
    public Iterator<SpirometerReading> iterator() {
        if (__readings != null) {
            return __readings.iterator();
        }
        return null;
    }
    
    /*
     * This is a bit loose as we haven't done anything to verify
     * the Spirometer Reading is from the same device and patient
     */
    public boolean addReading(SpirometerReading sp) {
        return __readings.add(sp);
    }

    /*
     * This is useful for merging maps but note the "other" maps readings could overwrite
     * your own.
     */
    public boolean addReadings(SpirometerReadings other) {
        return __readings.addAll(other.__readings);
    }

    private SpirometerReadings __constructSPR(Iterator<SpirometerReading> ispr) {
        SpirometerReadings spr = null;
        if (ispr != null) {
            spr = new SpirometerReadings();
            while (ispr.hasNext()) {
                spr.addReading(ispr.next());
            }
        }
        return spr;
    }
     
     public SpirometerReading getFirstReading() {
        SpirometerReading rval = null;
        if (__readings != null) {
            rval = __readings.first();
        }
        return rval;
    }
     
     public SpirometerReading getLastReading() {
         SpirometerReading rval = null;
         if (__readings != null) {
             rval = __readings.last();
         }
         return rval;
     }
     
    public int size() {
        if (__readings == null)  return 0;
        return __readings.size();
    }
    
    // We may need additional operations like Union or Minus when we
    // write the persistence code.
}
