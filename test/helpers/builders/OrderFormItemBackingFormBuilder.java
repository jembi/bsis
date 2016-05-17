package helpers.builders;

import model.componenttype.ComponentType;
import backingform.ComponentTypeBackingForm;
import backingform.OrderFormItemBackingForm;

public class OrderFormItemBackingFormBuilder {

  private Long id;
  private ComponentTypeBackingForm componentType;
  private String bloodAbo;
  private String bloodRh;
  private int numberOfUnits;
  private boolean isDeleted = false;

  public OrderFormItemBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public OrderFormItemBackingFormBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }
  
  public OrderFormItemBackingFormBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
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
    this.componentType.setComponentType(componentType);
    return this;
  }
  
  public OrderFormItemBackingFormBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public OrderFormItemBackingForm build() {
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    backingForm.setId(id);
    backingForm.setComponentType(componentType);
    backingForm.setBloodAbo(bloodAbo);
    backingForm.setBloodRh(bloodRh);
    backingForm.setNumberOfUnits(numberOfUnits);
    backingForm.setIsDeleted(isDeleted);
    return backingForm;
  }

  public static OrderFormItemBackingFormBuilder anOrderFormItemBackingForm() {
    return new OrderFormItemBackingFormBuilder();
  }
}
