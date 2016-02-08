package viewmodel;

public class AdverseEventViewModel {

  private Long id;
  private AdverseEventTypeViewModel type;
  private String comment;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AdverseEventTypeViewModel getType() {
    return type;
  }

  public void setType(AdverseEventTypeViewModel type) {
    this.type = type;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

}
