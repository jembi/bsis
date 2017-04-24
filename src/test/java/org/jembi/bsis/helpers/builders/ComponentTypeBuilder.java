package org.jembi.bsis.helpers.builders;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeBuilder extends AbstractEntityBuilder<ComponentType> {
  
  // static counter that is used to create a unique default name and code
  private static int UNIQUE_INCREMENT = 0;

  private UUID id;
  private String componentTypeName = "Component Type " + ++UNIQUE_INCREMENT;
  private Boolean isDeleted = false;
  private int expiresAfter;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private String componentTypeCode = String.format("%05d", UNIQUE_INCREMENT);
  private String description;
  private boolean hasBloodGroup = false;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private Integer lowTransportTemperature;
  private Integer highTransportTemperature;
  private String preparationInfo;
  private String transportInfo;
  private String storageInfo;
  private boolean canBeIssued = true;
  private Set<ComponentTypeCombination> producedComponentTypeCombinations;
  private boolean containsPlasma = false;
  private Integer maxBleedTime;
  private Integer maxTimeSinceDonation;
  private Double gravity;

  public ComponentTypeBuilder withId(UUID id) {
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
  
  public ComponentTypeBuilder withComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
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

  public ComponentTypeBuilder withTransportInfo(String transportInfo) {
    this.transportInfo = transportInfo;
    return this;
  }

  public ComponentTypeBuilder withStorageInfo(String storageInfo) {
    this.storageInfo = storageInfo;
    return this;
  }
  
  public ComponentTypeBuilder thatCanBeIssued() {
    this.canBeIssued = true;
    return this;
  }  
  
  public ComponentTypeBuilder thatCanNotBeIssued() {
    this.canBeIssued = false;
    return this;
  }  

  public ComponentTypeBuilder withMaxBleedTime(Integer maxBleedTime) {
    this.maxBleedTime = maxBleedTime;
    return this;
  }
  
  public ComponentTypeBuilder withGravity(Double gravity) {
    this.gravity = gravity;
    return this;
  }

  public ComponentTypeBuilder withMaxTimeSinceDonation(Integer maxTimeSinceDonation) {
    this.maxTimeSinceDonation = maxTimeSinceDonation;
    return this;
  }

  public ComponentTypeBuilder withProducedComponentTypeCombination(ComponentTypeCombination producedComponentTypeCombination) {
    if (this.producedComponentTypeCombinations == null) {
      this.producedComponentTypeCombinations = new HashSet<>();
    }
    this.producedComponentTypeCombinations.add(producedComponentTypeCombination);
    return this;
  }
  
  public ComponentTypeBuilder thatContainsPlasma() {
    this.containsPlasma = true;;
    return this;
  }
  
  public ComponentTypeBuilder withLowTransportTemperature(Integer lowTransportTemperature) {
    this.lowTransportTemperature = lowTransportTemperature;
    return this;
  }
  
  public ComponentTypeBuilder withHighTransportTemperature(Integer highTransportTemperature) {
    this.highTransportTemperature = highTransportTemperature;
    return this;
  }

  public ComponentTypeBuilder thatDoesntContainsPlasma() {
    this.containsPlasma = false;;
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
    componentType.setComponentTypeCode(componentTypeCode);
    componentType.setDescription(description);
    componentType.setHasBloodGroup(hasBloodGroup);
    componentType.setLowStorageTemperature(lowStorageTemperature);
    componentType.setHighStorageTemperature(highStorageTemperature);
    componentType.setPreparationInfo(preparationInfo);
    componentType.setProducedComponentTypeCombinations(producedComponentTypeCombinations);
    componentType.setTransportInfo(transportInfo);
    componentType.setStorageInfo(storageInfo);
    componentType.setCanBeIssued(canBeIssued);
    componentType.setContainsPlasma(containsPlasma);
    componentType.setMaxBleedTime(maxBleedTime);
    componentType.setMaxTimeSinceDonation(maxTimeSinceDonation);
    componentType.setLowTransportTemperature(lowTransportTemperature);
    componentType.setHighTransportTemperature(highTransportTemperature);
    componentType.setGravity(gravity);
    return componentType;
  }

  public static ComponentTypeBuilder aComponentType() {
    return new ComponentTypeBuilder();
  }

}
