package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;

public class ComponentTypeBackingFormBuilder extends AbstractBuilder<ComponentTypeBackingForm> {
  
  private Long id;

  public ComponentTypeBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  @Override
  public ComponentTypeBackingForm build() {
    ComponentTypeBackingForm backingForm = new ComponentTypeBackingForm();
    backingForm.setId(id);
    return backingForm;
  }
  
  public static ComponentTypeBackingFormBuilder aComponentTypeBackingForm() {
    return new ComponentTypeBackingFormBuilder();
  }

}
