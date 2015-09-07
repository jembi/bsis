package viewmodel;

public class AdverseEventTypeViewModel {
    
    private long id;
    private String name;
    private String description;
    private boolean isDeleted;
    
    public AdverseEventTypeViewModel() {
        // Default constructor
    }
    
    public AdverseEventTypeViewModel(long id, String name, String description, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
