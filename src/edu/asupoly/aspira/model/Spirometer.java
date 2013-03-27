package edu.asupoly.aspira.model;

public class Spirometer implements java.io.Serializable {
    private static final long serialVersionUID = -1399565347093334522L;

    private String __serialId;
    private String __vendor;
    private String __model;
    private String __description;
    private String __assignedToPatient;
    
    public Spirometer(String serialId, String vendor, String model, String description) {
        this(serialId, vendor, model, description, null);
    }

    public Spirometer(String serialId, String vendor, String model, String description, Patient p) {
        __serialId = serialId;
        __vendor = vendor;
        __model = model;
        __description = description;
        __assignedToPatient = p.getPatientId();
    }
    
    public String getSerialId() {
        return __serialId;
    }

    public String getVendor() {
        return __vendor;
    }

    public String getModel() {
        return __model;
    }

    public String getDescription() {
        return __description;
    }

    public String getAssignedToPatient() {
        return __assignedToPatient;
    }
}
