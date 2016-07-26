package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;

public class ComponentTypeBackingFormBuilder extends AbstractBuilder<ComponentTypeBackingForm> {
  
  private Long id;
  private String componentTypeName;
  private String componentTypeCode;
  private Integer expiresAfter;

  public ComponentTypeBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public ComponentTypeBackingFormBuilder withComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
    return this;
  }
  
  public ComponentTypeBackingFormBuilder withComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
    return this;
  }
  
  public ComponentTypeBackingFormBuilder withExpiresAfter(Integer expiresAfter) {
    this.expiresAfter = expiresAfter;
    return this;
  }

  @Override
  public ComponentTypeBackingForm build() {
    ComponentTypeBackingForm backingForm = new ComponentTypeBackingForm();
    backingForm.setId(id);
    backingForm.setComponentTypeName(componentTypeName);
    backingForm.setComponentTypeCode(componentTypeCode);
    backingForm.setExpiresAfter(expiresAfter);
    return backingForm;
  }
  
  public static ComponentTypeBackingFormBuilder aComponentTypeBackingForm() {
    return new ComponentTypeBackingFormBuilder();
  }

}
