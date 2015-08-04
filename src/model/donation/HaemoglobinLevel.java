package model.donation;

public enum HaemoglobinLevel {
    
    LOW("Low"),
    NORMAL("Normal"),
    HIGH("High");
    
    private String label;

    private HaemoglobinLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
