package edu.asupoly.aspira.model;

public class Clinician implements java.io.Serializable {
    private static final long serialVersionUID = 3119625564045569126L;
    
    private String __clinicianId;
    
    public Clinician(String cid) {
        __clinicianId = cid;
    }
    
    public String getClinicianId() {
        return __clinicianId;
    }
}
