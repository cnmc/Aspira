package edu.asupoly.aspira.model;

import java.util.HashSet;

public class Clinician implements java.io.Serializable {
    private static final long serialVersionUID = 3119625564045569126L;
    
    private String __clinicianId;
    private HashSet<String> __patients;
    
    public Clinician(String cid) {
        __clinicianId = cid;
        __patients = new HashSet<String>();
    }
    
    public String getClinicianId() {
        return __clinicianId;
    }
    
    public boolean addPatient(Patient p) {
        if (p != null) return __patients.add(p.getPatientId());
        return false;
    }
    
    public boolean removePatient(Patient p) {
        if (p != null) return __patients.remove(p.getPatientId());
        return false;
    }
    
    public String[] getPatientIds() {
        return __patients.toArray(new String[0]);
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Clinician && __clinicianId.equals(((Clinician)obj).__clinicianId);
    }
    @Override
    public int hashCode() {
        return __clinicianId.hashCode();
    }
    @Override
    public String toString() {
        StringBuffer pstr = new StringBuffer();
        String[] p = getPatientIds();
        if (p != null) {
            for (int i = 0; i < p.length; i++) pstr.append("\n\tpatient id: " + p[i]);
        }
        return "Clinician id: " + __clinicianId + "\nPatients:" + pstr.toString();
    }
}
