package edu.asupoly.aspira.model;

public class Spirometer implements java.io.Serializable {
    private static final long serialVersionUID = -1399565347093334522L;

    private String __serialId;
    private String __vendor;
    private String __model;
    private String __description;
    private String __assignedToPatient;
    
    public Spirometer(String serialId, String vendor, String model, String description) {
        this(serialId, vendor, model, description, "");
    }

    public Spirometer(String serialId, String vendor, String model, String description, String pid) {
        __serialId = serialId;
        __vendor = vendor;
        __model = model;
        __description = description;
        __assignedToPatient = pid;
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
    
    public void setAssignedToPatient(String pid) {
        __assignedToPatient = pid;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Spirometer && __serialId.equals(((Spirometer)obj).__serialId);
    }
    @Override
    public int hashCode() {
        return __serialId.hashCode();
    }
    @Override
    public String toString() {
        return "Spirometer id: " + __serialId + "\nPatient:" + __assignedToPatient;
    }
}
