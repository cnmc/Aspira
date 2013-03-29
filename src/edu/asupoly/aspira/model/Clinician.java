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
}
