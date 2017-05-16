package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.ComponentPreProcessingBackingForm;

import java.util.Date;
import java.util.UUID;

public class ComponentPreProcessingBackingFormBuilder extends AbstractBuilder<ComponentPreProcessingBackingForm> {

  private UUID id;
  private Integer weight;
  private Date bleedStartTime;
  private Date bleedEndTime;

  public ComponentPreProcessingBackingFormBuilder withBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
    return this;
  }

  public ComponentPreProcessingBackingFormBuilder withBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
    return this;
  }
  
  public ComponentPreProcessingBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public ComponentPreProcessingBackingFormBuilder withWeight(Integer weight) {
    this.weight = weight;
    return this;
  }

  @Override
  public ComponentPreProcessingBackingForm build() {
    ComponentPreProcessingBackingForm backingForm = new ComponentPreProcessingBackingForm();
    backingForm.setId(id);
    backingForm.setWeight(weight);
    backingForm.setBleedStartTime(bleedStartTime);
    backingForm.setBleedEndTime(bleedEndTime);
    return backingForm;
  }

  public static ComponentPreProcessingBackingFormBuilder aComponentBackingForm() {
    return new ComponentPreProcessingBackingFormBuilder();
  }

}
