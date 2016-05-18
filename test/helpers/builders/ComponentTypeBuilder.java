package helpers.builders;

import java.util.ArrayList;
import java.util.List;

import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;
import model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeBuilder extends AbstractEntityBuilder<ComponentType> {

  private Long id;
  private String componentTypeName;
  private Boolean isDeleted = false;
  private int expiresAfter;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private String componentTypeNameShort;
  private String description;
  private boolean hasBloodGroup = false;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private String preparationInfo;
  private List<ComponentTypeCombination> producedComponentTypeCombinations = new ArrayList<>();

  public ComponentTypeBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentTypeBuilder withComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
    return this;
  }

  public ComponentTypeBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }
  
  public ComponentTypeBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  public ComponentTypeBuilder withExpiresAfter(int expiresAfter) {
    this.expiresAfter = expiresAfter;
    return this;
  }
  
  public ComponentTypeBuilder withExpiresAfterUnits(ComponentTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
    return this;
  }
  
  public ComponentTypeBuilder withDescription(String description) {
    this.description = description;
    return this;
  }
  
  public ComponentTypeBuilder thatHasBloodGroup() {
    this.hasBloodGroup = true;
    return this;
  }
  
  public ComponentTypeBuilder withComponentTypeNameShort(String componentTypeNameShort) {
    this.componentTypeNameShort = componentTypeNameShort;
    return this;
  }
  
  public ComponentTypeBuilder withLowStorageTemperature(Integer lowStorageTemperature) {
    this.lowStorageTemperature = lowStorageTemperature;
    return this;
  }
  
  public ComponentTypeBuilder withHighStorageTemperature(Integer highStorageTemperature) {
    this.highStorageTemperature = highStorageTemperature;
    return this;
  }
  
  public ComponentTypeBuilder withPreparationInfo(String preparationInfo) {
    this.preparationInfo = preparationInfo;
    return this;
  }

  public ComponentTypeBuilder withProducedComponentTypeCombination(ComponentTypeCombination producedComponentTypeCombination) {
    this.producedComponentTypeCombinations.add(producedComponentTypeCombination);
    return this;
  }

  @Override
  public ComponentType build() {
    ComponentType componentType = new ComponentType();
    componentType.setId(id);
    componentType.setComponentTypeName(componentTypeName);
    componentType.setIsDeleted(isDeleted);
    componentType.setExpiresAfter(expiresAfter);
    componentType.setExpiresAfterUnits(expiresAfterUnits);
    componentType.setComponentTypeNameShort(componentTypeNameShort);
    componentType.setDescription(description);
    componentType.setHasBloodGroup(hasBloodGroup);
    componentType.setLowStorageTemperature(lowStorageTemperature);
    componentType.setHighStorageTemperature(highStorageTemperature);
    componentType.setPreparationInfo(preparationInfo);
    componentType.setProducedComponentTypeCombinations(producedComponentTypeCombinations);
    return componentType;
  }

  public static ComponentTypeBuilder aComponentType() {
    return new ComponentTypeBuilder();
  }

}
