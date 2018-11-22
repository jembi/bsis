package org.jembi.bsis.backingform;


import java.util.UUID;

public class OrderFormItemBackingForm {

  private UUID id;
  
  private ComponentTypeBackingForm componentType;
  
  private String bloodGroup;
  
  private int numberOfUnits;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ComponentTypeBackingForm getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeBackingForm componentType) {
    this.componentType = componentType;
  }

  public String getBloodGroup() {
    return bloodGroup;
  }

  public void setBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
  }

  public int getNumberOfUnits() {
    return numberOfUnits;
  }

  public void setNumberOfUnits(int numberOfUnits) {
    this.numberOfUnits = numberOfUnits;
  }
}
