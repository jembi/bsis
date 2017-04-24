package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.DiscardReasonBackingForm;

public class DiscardReasonBackingFormBuilder extends AbstractBuilder<DiscardReasonBackingForm> {

  private UUID id;
  private String reason;
  private Boolean isDeleted = Boolean.FALSE;

  public DiscardReasonBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DiscardReasonBackingFormBuilder withReason(String reason) {
    this.reason = reason;
    return this;
  }

  public DiscardReasonBackingFormBuilder thatIsDeleted() {
    this.isDeleted = Boolean.TRUE;
    return this;
  }

  public DiscardReasonBackingFormBuilder thatIsNotDeleted() {
    this.isDeleted = Boolean.FALSE;
    return this;
  }

  @Override
  public DiscardReasonBackingForm build() {
    DiscardReasonBackingForm backingForm = new DiscardReasonBackingForm();
    backingForm.setId(id);
    backingForm.setReason(reason);
    backingForm.setIsDeleted(isDeleted);
    return backingForm;
  }

  public static DiscardReasonBackingFormBuilder aDiscardReasonBackingForm() {
    return new DiscardReasonBackingFormBuilder();
  }

}
