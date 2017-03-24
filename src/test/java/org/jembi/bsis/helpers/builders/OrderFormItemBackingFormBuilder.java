package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;

import java.util.UUID;

public class OrderFormItemBackingFormBuilder {

  private UUID id;
  private ComponentTypeBackingForm componentType;
  private String bloodGroup;
  private int numberOfUnits;

  public OrderFormItemBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public OrderFormItemBackingFormBuilder withBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
    return this;
  }

  public OrderFormItemBackingFormBuilder withNumberOfUnits(int numberOfUnits) {
    this.numberOfUnits = numberOfUnits;
    return this;
  }

  public OrderFormItemBackingFormBuilder withComponentType(ComponentTypeBackingForm componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public OrderFormItemBackingFormBuilder withComponentType(ComponentType componentType) {
    this.componentType = new ComponentTypeBackingForm();
    return this;
  }

  public OrderFormItemBackingForm build() {
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    backingForm.setId(id);
    backingForm.setComponentType(componentType);
    backingForm.setBloodGroup(bloodGroup);
    backingForm.setNumberOfUnits(numberOfUnits);
    return backingForm;
  }

  public static OrderFormItemBackingFormBuilder anOrderFormItemBackingForm() {
    return new OrderFormItemBackingFormBuilder();
  }
}
