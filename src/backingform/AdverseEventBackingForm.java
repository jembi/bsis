package backingform;

import javax.validation.constraints.NotNull;

public class AdverseEventBackingForm {

  private Long id;
  private AdverseEventTypeBackingForm typeBackingForm;
  private String comment;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AdverseEventTypeBackingForm getType() {
    return typeBackingForm;
  }

  @NotNull
  public void setType(AdverseEventTypeBackingForm typeBackingForm) {
    this.typeBackingForm = typeBackingForm;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
