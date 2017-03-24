package org.jembi.bsis.viewmodel;

import java.util.UUID;

public class OrderFormItemViewModel extends BaseViewModel<UUID> {
  
  private ComponentTypeViewModel componentType;
  
  private String bloodGroup;
  
  private int numberOfUnits;

  public ComponentTypeViewModel getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeViewModel componentType) {
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
