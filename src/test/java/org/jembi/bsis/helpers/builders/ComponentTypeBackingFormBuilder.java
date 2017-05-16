package org.jembi.bsis.helpers.builders;

import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeBackingFormBuilder extends AbstractBuilder<ComponentTypeBackingForm> {
  
  private UUID id;
  private String componentTypeName;
  private String componentTypeCode;
  private Integer expiresAfter;
  private Integer maxBleedTime;
  private Integer maxTimeSinceDonation;
  private ComponentTypeTimeUnits expiresAfterUnit;
  private Boolean hasBloodGroup;
  private String description;
  private boolean isDeleted = false;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private Integer lowTransportTemperature;
  private Integer highTransportTemperature;
  private String preparationInfo;
  private String transportInfo;
  private String storageInfo;
  private boolean canBeIssued = true;
  private boolean containsPlasma = true;
  private Double gravity;
  private Set<ComponentTypeCombination> producedComponentTypeCombinations;
  
  public ComponentTypeBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public ComponentTypeBackingFormBuilder withProducedComponentTypeCombinations(Set<ComponentTypeCombination> producedComponentTypeCombinations) {
    this.producedComponentTypeCombinations = producedComponentTypeCombinations;
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

  public ComponentTypeBackingFormBuilder withMaxBleedTime(Integer maxBleedTime) {
    this.maxBleedTime = maxBleedTime;
    return this;
  }

  public ComponentTypeBackingFormBuilder withMaxTimeSinceDonation(Integer maxTimeSinceDonation) {
    this.maxTimeSinceDonation = maxTimeSinceDonation;
    return this;
  }

  public ComponentTypeBackingFormBuilder withExpiresAfterUnit(ComponentTypeTimeUnits expiresAfterUnit) {
    this.expiresAfterUnit = expiresAfterUnit;
    return this;
  }

  public ComponentTypeBackingFormBuilder thatHasBloodGroup() {
    this.hasBloodGroup = true;
    return this;
  }

  public ComponentTypeBackingFormBuilder thatHasNotBloodGroup() {
    this.hasBloodGroup = false;
    return this;
  }
  
  public ComponentTypeBackingFormBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public ComponentTypeBackingFormBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public ComponentTypeBackingFormBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }
  
  public ComponentTypeBackingFormBuilder withLowStorageTemperature(Integer lowStorageTemperature) {
    this.lowStorageTemperature = lowStorageTemperature;
    return this;
  }

  public ComponentTypeBackingFormBuilder withHighStorageTemperature(Integer highStorageTemperature) {
    this.highStorageTemperature = highStorageTemperature;
    return this;
  }

  public ComponentTypeBackingFormBuilder withLowTransportTemperature(Integer lowTransportTemperature) {
    this.lowTransportTemperature = lowTransportTemperature;
    return this;
  }

  public ComponentTypeBackingFormBuilder withHighTransportTemperature(Integer highTransportTemperature) {
    this.highTransportTemperature = highTransportTemperature;
    return this;
  }

  public ComponentTypeBackingFormBuilder withPreparationInfo(String preparationInfo) {
    this.preparationInfo = preparationInfo;
    return this;
  }

  public ComponentTypeBackingFormBuilder withTransportInfo(String transportInfo) {
    this.transportInfo = transportInfo;
    return this;
  }

  public ComponentTypeBackingFormBuilder withStorageInfo(String storageInfo) {
    this.storageInfo = storageInfo;
    return this;
  }
  
  public ComponentTypeBackingFormBuilder withGravity(Double gravity){
    this.gravity = gravity;
    return this;
  }

  public ComponentTypeBackingFormBuilder thatCanBeIssued() {
    this.canBeIssued = true;
    return this;
  }

  public ComponentTypeBackingFormBuilder thatCanNotBeIssued() {
    this.canBeIssued = false;
    return this;
  }
  
  public ComponentTypeBackingFormBuilder thatContainsPlasma() {
    this.containsPlasma = true;
    return this;
  }

  public ComponentTypeBackingFormBuilder thatDoesNotContainPlasma() {
    this.containsPlasma = false;
    return this;
  }
  
  @Override
  public ComponentTypeBackingForm build() {
    ComponentTypeBackingForm backingForm = new ComponentTypeBackingForm();
    backingForm.setId(id);
    backingForm.setComponentTypeName(componentTypeName);
    backingForm.setComponentTypeCode(componentTypeCode);
    backingForm.setExpiresAfter(expiresAfter);
    backingForm.setMaxBleedTime(maxBleedTime);
    backingForm.setMaxTimeSinceDonation(maxTimeSinceDonation);
    backingForm.setExpiresAfterUnits(expiresAfterUnit);
    backingForm.setHasBloodGroup(hasBloodGroup);
    backingForm.setDescription(description);
    backingForm.setIsDeleted(isDeleted);
    backingForm.setLowStorageTemperature(lowStorageTemperature);
    backingForm.setHighStorageTemperature(highStorageTemperature);
    backingForm.setLowTransportTemperature(lowTransportTemperature);
    backingForm.setHighTransportTemperature(highTransportTemperature);
    backingForm.setPreparationInfo(preparationInfo);
    backingForm.setTransportInfo(transportInfo);
    backingForm.setStorageInfo(storageInfo);
    backingForm.setCanBeIssued(canBeIssued);
    backingForm.setContainsPlasma(containsPlasma);
    backingForm.setProducedComponentTypeCombinations(producedComponentTypeCombinations);
    backingForm.setGravity(gravity);
    return backingForm;
  }
  
  public static ComponentTypeBackingFormBuilder aComponentTypeBackingForm() {
    return new ComponentTypeBackingFormBuilder();
  }

}
