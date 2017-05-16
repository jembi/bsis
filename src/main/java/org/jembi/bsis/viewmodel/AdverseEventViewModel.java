package org.jembi.bsis.viewmodel;

import java.util.UUID;

public class AdverseEventViewModel {

  private UUID id;
  private AdverseEventTypeViewModel type;
  private String comment;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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
