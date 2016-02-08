package viewmodel;

public class AdverseEventTypeViewModel {

  private Long id;
  private String name;
  private String description;
  private boolean isDeleted;

  public AdverseEventTypeViewModel() {
    // Default constructor
  }

  public AdverseEventTypeViewModel(Long id, String name, String description, boolean isDeleted) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.isDeleted = isDeleted;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
