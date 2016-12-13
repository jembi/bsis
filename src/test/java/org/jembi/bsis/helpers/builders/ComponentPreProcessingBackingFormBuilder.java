package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.ComponentPreProcessingBackingForm;

public class ComponentPreProcessingBackingFormBuilder extends AbstractBuilder<ComponentPreProcessingBackingForm> {

  private Long id;
  private Integer weight;
  
  public ComponentPreProcessingBackingFormBuilder withId(Long id) {
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
    return backingForm;
  }

  public static ComponentPreProcessingBackingFormBuilder aComponentBackingForm() {
    return new ComponentPreProcessingBackingFormBuilder();
  }

}
