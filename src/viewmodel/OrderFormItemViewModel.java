package viewmodel;


public class OrderFormItemViewModel {
  
  private Long id;
  
  private ComponentTypeViewModel componentType;
  
  private String bloodAbo;
  
  private String bloodRh;
  
  private int numberOfUnits;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ComponentTypeViewModel getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeViewModel componentType) {
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
}
