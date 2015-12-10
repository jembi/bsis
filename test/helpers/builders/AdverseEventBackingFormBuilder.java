package helpers.builders;

import backingform.AdverseEventBackingForm;
import backingform.AdverseEventTypeBackingForm;

public class AdverseEventBackingFormBuilder extends AbstractBuilder<AdverseEventBackingForm> {

  private AdverseEventTypeBackingForm type;
  private String comment;

  public static AdverseEventBackingFormBuilder anAdverseEventBackingForm() {
    return new AdverseEventBackingFormBuilder();
  }

  public AdverseEventBackingFormBuilder withType(AdverseEventTypeBackingForm type) {
    this.type = type;
    return this;
  }

  public AdverseEventBackingFormBuilder withComment(String comment) {
    this.comment = comment;
    return this;
  }

  @Override
  public AdverseEventBackingForm build() {
    AdverseEventBackingForm adverseEventBackingForm = new AdverseEventBackingForm();
    adverseEventBackingForm.setType(type);
    adverseEventBackingForm.setComment(comment);
    return adverseEventBackingForm;
  }

}
