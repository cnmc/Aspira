package edu.asupoly.aspira.model;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * UIEvents represents the event captured
 *  on tablet for a patient
 * The implementation is a SortedMap where the key is a
 * tuple <PatientId, Date> and the stored object
 * is a UIEvent.
 */
public class UIEvents implements java.io.Serializable {
    private static final long serialVersionUID = -5318047622804053850L;
    
    private TreeSet<UIEvent> __events;
   
    public UIEvents() {
        __events = new TreeSet<UIEvent>();
    }
    
    public UIEvents getUIEventsForPatient(String patientId) {
        if (patientId == null) return null;
        
        UIEvent e = null;
        UIEvents events = new UIEvents();
        Iterator<UIEvent> iter = __events.iterator();
        while (iter.hasNext()) {
            e = iter.next();
            if (patientId.equals(e.getPatientId())) {
                events.addEvent(e);
            }
        }
        return events;   
    }
    
    /**
     * Get all Events before a given minute
     * @param d
     * @param inclusive true if you want the given minute included in the result
     * @return an Iterator that preserves the sorted ordering of dates in ascending order
     */
    public UIEvents getUIEventsBefore(Date d, boolean inclusive) {
        if (d == null) return null;
 
        SortedSet<UIEvent> sm = __events.headSet(new UIEvent("","","","","","",d,0), inclusive);
        if (sm != null) {
            return __constructEvents(sm.iterator());  
         }
        return null;
    }
    
    public UIEvents getUIEventsAfter(Date d, boolean inclusive) {
        if (d == null) return null;
        
        SortedSet<UIEvent> sm = __events.tailSet(new UIEvent("","","","","","",d,0), inclusive);
        if (sm != null) {
            return __constructEvents(sm.iterator());  
         }
        return null;
    }    
    
    public Iterator<UIEvent> getUIEventsBetween(Date start, boolean inclstart,
                                       Date end,   boolean inclend) {
        SortedSet<UIEvent> res  = null;
        
        if (start != null && end != null && __events != null) { 
            res = __events.subSet(new UIEvent("","","","","","",start,0), inclstart, 
                                  new UIEvent("","","","","","",end,0), inclend);
            return res.iterator();
        } else return null;    
    }

    public UIEvent getFirstEvent() {
        UIEvent rval = null;
        if (__events != null) {
            rval = __events.first();
        }
        return rval;
    }
    
    public UIEvent getLastEvent() {
        UIEvent rval = null;
        if (__events != null) {
            rval = __events.last();
        }
        return rval;
    }
    
    private UIEvents __constructEvents(Iterator<UIEvent> event) {
        UIEvents _events = null;
        if (event != null) {
            _events = new UIEvents();
            while (event.hasNext()) {
                _events.addEvent(event.next());
            }
        }
        return _events;
    }
    
    /**
     * Gets all values as a iterator in ascending order
     * @return an Iterator with all values in ascending order or null
     */
    public Iterator<UIEvent> iterator() {
        if (__events != null) {
            return __events.iterator();
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((__events == null) ? 0 : __events.hashCode());
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
        UIEvents other = (UIEvents) obj;
        
        if (__events == null) {
            if (other.__events != null)
                return false;
        } else {
            Iterator<UIEvent> thisIter  = __events.iterator();
            Iterator<UIEvent> otherIter = other.__events.iterator();
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
     * the UIEvent is from the same patient
     */
    public boolean addEvent(UIEvent e) {
        if (e == null) return false;
        return __events.add(e);
    }
    
    /*
     * This is useful for merging maps but note the "other" maps events could overwrite
     * your own.
     */
    public boolean addEvents(UIEvents other) {
        if (other == null || other.__events == null) return false;
        return __events.addAll(other.__events);
    }
    
    public int size() {
        if (__events == null)  return 0;
        return __events.size();
    }
    
    // We may need additional operations like Union or Minus when we
    // write the persistence code.
}
