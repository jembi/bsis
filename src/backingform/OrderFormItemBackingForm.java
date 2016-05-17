package backingform;


public class OrderFormItemBackingForm {

  private Long id;
  
  private ComponentTypeBackingForm componentType;
  
  private String bloodAbo;
  
  private String bloodRh;
  
  private int numberOfUnits;
  
  private boolean isDeleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ComponentTypeBackingForm getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeBackingForm componentType) {
    this.componentType = componentType;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public int getNumberOfUnits() {
    return numberOfUnits;
  }

  public void setNumberOfUnits(int numberOfUnits) {
    this.numberOfUnits = numberOfUnits;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
