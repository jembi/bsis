package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.backingform.RecordComponentBackingForm;

public class RecordComponentBackingFormBuilder extends RecordComponentBackingForm {
  
  private UUID parentComponentId;
  private ComponentTypeCombinationBackingForm componentTypeCombination;
  private Date processedOn;

  public RecordComponentBackingFormBuilder withParentComponentId(UUID parentComponentId) {
    this.parentComponentId = parentComponentId;
    return this;
  }
  
  public RecordComponentBackingFormBuilder withComponentTypeCombination(
      ComponentTypeCombinationBackingForm componentTypeCombination) {
    this.componentTypeCombination = componentTypeCombination;
    return this;
  }
  
  public RecordComponentBackingFormBuilder withProcessedOn(Date processedOn) {
    this.processedOn = processedOn;
    return this;
  }

  public RecordComponentBackingForm build() {
    RecordComponentBackingForm backingForm = new RecordComponentBackingForm();
    backingForm.setParentComponentId(parentComponentId);
    backingForm.setComponentTypeCombination(componentTypeCombination);
    backingForm.setProcessedOn(processedOn);
    return backingForm;
  }
  
  public static RecordComponentBackingFormBuilder aRecordComponentBackingForm() {
    return new RecordComponentBackingFormBuilder();
  }

}
