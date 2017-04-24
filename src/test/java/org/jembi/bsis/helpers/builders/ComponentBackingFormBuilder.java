package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.ComponentBackingForm;

public class ComponentBackingFormBuilder extends AbstractBuilder<ComponentBackingForm> {

  private UUID id;
  private Integer weight;

  public ComponentBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ComponentBackingFormBuilder withWeight(Integer weight) {
    this.weight = weight;
    return this;
  }

  @Override
  public ComponentBackingForm build() {
    ComponentBackingForm backingForm = new ComponentBackingForm();
    backingForm.setId(id);
    backingForm.setWeight(weight);
    return backingForm;
  }

  public static ComponentBackingFormBuilder aComponentBackingForm() {
    return new ComponentBackingFormBuilder();
  }

}
