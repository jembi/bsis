package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.AdverseEventBackingForm;
import org.jembi.bsis.backingform.AdverseEventTypeBackingForm;

public class AdverseEventBackingFormBuilder extends AbstractBuilder<AdverseEventBackingForm> {

  private UUID id;
  private AdverseEventTypeBackingForm type;
  private String comment;

  public AdverseEventBackingFormBuilder withType(AdverseEventTypeBackingForm type) {
    this.type = type;
    return this;
  }

  public AdverseEventBackingFormBuilder withComment(String comment) {
    this.comment = comment;
    return this;
  }

  public AdverseEventBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  @Override
  public AdverseEventBackingForm build() {
    AdverseEventBackingForm adverseEventBackingForm = new AdverseEventBackingForm();
    adverseEventBackingForm.setId(id);
    adverseEventBackingForm.setType(type);
    adverseEventBackingForm.setComment(comment);
    return adverseEventBackingForm;
  }

  public static AdverseEventBackingFormBuilder anAdverseEventBackingForm() {
    return new AdverseEventBackingFormBuilder();
  }

}
