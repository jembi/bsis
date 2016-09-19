package org.jembi.bsis.viewmodel;

public class ComponentTypeViewModel extends BaseViewModel{

 // private Long id;
  private String componentTypeName;
  private String componentTypeCode;
  private String description;
  private boolean containsPlasma;

  public String getComponentTypeName() {
    return componentTypeName;
  }

  public String getComponentTypeCode() {
    return componentTypeCode;
  }

  public String getDescription() {
    return description;
  }

  public void setComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
  }

  public void setComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isContainsPlasma() {
    return containsPlasma;
  }

  public void setContainsPlasma(boolean containsPlasma) {
    this.containsPlasma = containsPlasma;
  }
 
}
