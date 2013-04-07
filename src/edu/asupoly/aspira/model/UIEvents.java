package edu.asupoly.aspira.model;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/*
 * UIEvents represents the event captured
 *  on tablet for a patient
 * The implementation is a SortedMap where the key is a
 * tuple <PatientId, Date> and the stored object
 * is a UIEvent.
 */
public class UIEvents {
    
    private class EventTuple implements  Comparable<EventTuple> {
       
        String patientId;
        Date   eventDate;
        
        EventTuple(String pid, Date d) {
            patientId = pid;
            eventDate = d;
        }
        
        @Override
        public int compareTo(EventTuple other) {
            return eventDate.compareTo(other.eventDate);
        }
    }
    private TreeMap<EventTuple, UIEvent> __events;
    
    private String __patientId;
    private EventTuple __forQuerying;
   
    public UIEvents(String patientId) {
        __patientId = patientId;
        __forQuerying = new EventTuple( __patientId, null);
        __events = new TreeMap<EventTuple, UIEvent>();
    }

    /**
     * Get the UIEvent at particular time
     * @param d
     * @return a UIEvent or null if no event occurred 
     */
    public UIEvent getEventAt(Date d) {
        if (d == null) return null;
        __forQuerying.eventDate = d;
        return __events.get(__forQuerying);
    }
    
    /**
     * Get all Events before a given minute
     * @param d
     * @param inclusive true if you want the given minute included in the result
     * @return an Iterator that preserves the sorted ordering of dates in ascending order
     */
    public UIEvents getUIEventsBefore(Date d, boolean inclusive) {
        if (d == null) return null;
        __forQuerying.eventDate = d;
        SortedMap<EventTuple, UIEvent> sm = __events.headMap(__forQuerying, inclusive);
        if (sm != null) {
            return __constructEvents(sm.values().iterator());  
         }
        return null;
    }
    
    public UIEvents getUIEventsAfter(Date d, boolean inclusive) {
        if (d == null) return null;
        __forQuerying.eventDate = d;
        SortedMap<EventTuple, UIEvent> sm = __events.tailMap(__forQuerying, inclusive);
        if (sm != null) {
            return __constructEvents(sm.values().iterator());
        }
        return null;
    }    
    
    public UIEvents getUIEventsBetween(Date start, boolean inclstart,
                                                   Date end,   boolean inclend) {
        if (start == null || end == null) return null;
        if (__events != null) {
            __forQuerying.eventDate = start;
            EventTuple forQ2 = new EventTuple(__forQuerying.patientId, end);
            return __constructEvents(__events.subMap(__forQuerying, inclstart, forQ2, inclend).values().iterator());
        }
        return null;
    }

    public UIEvent getFirstEvent() {
        UIEvent rval = null;
        if (__events != null) {
            Map.Entry<EventTuple, UIEvent> firstEntry = __events.firstEntry();
            rval = firstEntry.getValue();
        }
        return rval;
    }
    
    public UIEvent getLastUIEvent() {
        UIEvent rval = null;
        if (__events != null) {
            Map.Entry<EventTuple, UIEvent> lastEntry = __events.lastEntry();
            rval = lastEntry.getValue();
        }
        return rval;
    }
    
    private UIEvents __constructEvents(Iterator<UIEvent> event) {
        UIEvents _events = null;
        if (event != null) {
            _events = new UIEvents(__patientId);
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
            return __events.values().iterator();
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((__patientId == null) ? 0 : __patientId.hashCode());
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
        if (__patientId == null) {
            if (other.__patientId != null)
                return false;
        } else if (!__patientId.equals(other.__patientId))
            return false; 
        
        if (__events == null) {
            if (other.__events != null)
                return false;
        } else {
            Iterator<UIEvent> thisIter  = __events.values().iterator();
            Iterator<UIEvent> otherIter = other.__events.values().iterator();
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
    
    public String getPatientId()    { return __patientId; }
    
    /*
     * This is a bit loose as we haven't done anything to verify
     * the UIEvent is from the same patient
     */
    public boolean addEvent(UIEvent e) {
        UIEvent oldEvent = __events.put(new EventTuple(__patientId, 
                                                              e.getDate()), e);
        if (oldEvent != null) {
            //problem, something was already there, put it back
            __events.put(new EventTuple(__patientId, oldEvent.getDate()), oldEvent);
            return false;
        }
        return true;
    }
    
    /*
     * This is useful for merging maps but note the "other" maps events could overwrite
     * your own.
     */
    public boolean addEvents(UIEvents other) {
        if (other.__patientId.equals(__patientId)) {
            __events.putAll(other.__events);
            return true;
        }
        return false;
    }
    
    // Presumably gaps by the minute but we could add a flag
    // Might do better returning pairs of start/end of gap
    public Date[] getGaps() {
        
        // TODO Do we need this for UIEvent?
        
        return null;
    }
    // Overlap means intersection here, which we need to know before
    // pushing a collection to the underlying database
    public UIEvents getOverlap(UIEvents other) {
        
        // TODO : Do we need this? 
        
        return null;
    }
    
    public int size() {
        if (__events == null)  return 0;
        return __events.size();
    }
    
    // We may need additional operations like Union or Minus when we
    // write the persistence code.
}
