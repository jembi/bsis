package model.counselling;

public enum CounsellingStatus {

    FLAG_FOR_COUNSELLING(0, "Flagged For Counselling"),
    RECEIVED_COUNSELLING(1, "Received Counselling"),
    REFUSED_COUNSELLING(2, "Refused Counselling"),
    DID_NOT_RECEIVE_COUNSELLING(3, "Did Not Receive Counselling");
    
    private int id;
    private String label;

    private CounsellingStatus(int id, String label) {
        this.id = id;
        this.label = label;
    }
    
    public int getId() {
        return id;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static CounsellingStatus findById(int id) {
        for (CounsellingStatus value : CounsellingStatus.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }
}
