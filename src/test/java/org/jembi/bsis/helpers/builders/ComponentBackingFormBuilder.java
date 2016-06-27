package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.ComponentBackingForm;

public class ComponentBackingFormBuilder extends AbstractBuilder<ComponentBackingForm> {

  private Long id;
  
  public ComponentBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  @Override
  public ComponentBackingForm build() {
    ComponentBackingForm backingForm = new ComponentBackingForm();
    backingForm.setId(id);
    return backingForm;
  }

  public static ComponentBackingFormBuilder aComponentBackingForm() {
    return new ComponentBackingFormBuilder();
  }

}
