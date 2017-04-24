package org.jembi.bsis.model.componenttype;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.repository.ComponentTypeQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@NamedQueries({
  @NamedQuery(name = ComponentTypeQueryConstants.NAME_VERIFY_COMPONENT_TYPE_WITH_ID_EXISTS,
      query = ComponentTypeQueryConstants.QUERY_VERIFY_COMPONENT_TYPE_WITH_ID_EXISTS),
  @NamedQuery(name = ComponentTypeQueryConstants.NAME_FIND_COMPONENT_TYPE_BY_CODE,
      query = ComponentTypeQueryConstants.QUERY_FIND_COMPONENT_TYPE_BY_CODE),
  @NamedQuery(name = ComponentTypeQueryConstants.NAME_GET_COMPONENT_TYPES_THAT_CAN_BE_ISSUED,
      query = ComponentTypeQueryConstants.QUERY_GET_COMPONENT_TYPES_THAT_CAN_BE_ISSUED),
  @NamedQuery(name = ComponentTypeQueryConstants.NAME_VERIFY_UNIQUE_COMPONENT_TYPE_NAME,
      query = ComponentTypeQueryConstants.QUERY_VERIFY_UNIQUE_COMPONENT_TYPE_NAME),    
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class ComponentType extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 50, nullable = false, unique = true)
  private String componentTypeName;

  @Column(length = 30, nullable = false, unique = true)
  private String componentTypeCode;

  @Column(nullable = false)
  private Integer expiresAfter;
  
  private Integer maxBleedTime;
  
  private Integer maxTimeSinceDonation;

  @Enumerated(EnumType.STRING)
  @Column(length = 30, nullable = false)
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;

  @NotAudited
  @ManyToMany(mappedBy = "componentTypes", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @Where(clause = "isDeleted = 0")
  private List<ComponentTypeCombination> componentTypeCombinations;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @ManyToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @Where(clause = "isDeleted = 0")
  private Set<ComponentTypeCombination> producedComponentTypeCombinations;

  /**
   * TODO: Not used for now. Some component types like Cryoprecipitate may not require blood group
   * match before issuing. Usecase not clear yet.
   */
  private Boolean hasBloodGroup;

  /**
   * Pedi Component Type for this component type. can be null. This allows us to determine the
   * target type after splitting.
   */
  @OneToOne(optional = true)
  private ComponentType pediComponentType;

  @Lob
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

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getComponentTypeName() {
    return componentTypeName;
  }

  public void setComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getComponentTypeCode() {
    return componentTypeCode;
  }

  public void setComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
  }

  public ComponentTypeTimeUnits getExpiresAfterUnits() {
    return expiresAfterUnits;
  }

  public void setExpiresAfterUnits(ComponentTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
  }

  public Integer getExpiresAfter() {
    return expiresAfter;
  }

  public void setExpiresAfter(Integer expiresAfter) {
    this.expiresAfter = expiresAfter;
  }

  public Boolean getHasBloodGroup() {
    return hasBloodGroup;
  }

  public void setHasBloodGroup(Boolean hasBloodGroup) {
    this.hasBloodGroup = hasBloodGroup;
  }

  @JsonIgnore
  public int getExpiryIntervalMinutes() {
    int factor = 1;
    switch (expiresAfterUnits) {
      case HOURS:
        factor = 60;
        break;
      case DAYS:
        factor = 24 * 60;
        break;
      case YEARS:
        factor = 365 * 24 * 60;
        break;
    }
    return expiresAfter * factor;
  }

  public List<ComponentTypeCombination> getComponentTypeCombinations() {
    return componentTypeCombinations;
  }

  public void setComponentTypeCombinations(List<ComponentTypeCombination> componentTypeCombinations) {
    this.componentTypeCombinations = componentTypeCombinations;
  }

  public Set<ComponentTypeCombination> getProducedComponentTypeCombinations() {
    return producedComponentTypeCombinations;
  }

  public void setProducedComponentTypeCombinations(Set<ComponentTypeCombination> producedComponentTypeCombinations) {
    this.producedComponentTypeCombinations = producedComponentTypeCombinations;
  }

  public ComponentType getPediComponentType() {
    return pediComponentType;
  }

  public void setPediComponentType(ComponentType pediComponentType) {
    this.pediComponentType = pediComponentType;
  }

  public Integer getLowStorageTemperature() {
    return lowStorageTemperature;
  }

  public void setLowStorageTemperature(Integer lowStorageTemperature) {
    this.lowStorageTemperature = lowStorageTemperature;
  }

  public Integer getHighStorageTemperature() {
    return highStorageTemperature;
  }

  public void setHighStorageTemperature(Integer highStorageTemperature) {
    this.highStorageTemperature = highStorageTemperature;
  }

  public Integer getLowTransportTemperature() {
    return lowTransportTemperature;
  }

  public void setLowTransportTemperature(Integer lowTransportTemperature) {
    this.lowTransportTemperature = lowTransportTemperature;
  }

  public Integer getHighTransportTemperature() {
    return highTransportTemperature;
  }

  public void setHighTransportTemperature(Integer highTransportTemperature) {
    this.highTransportTemperature = highTransportTemperature;
  }

  public String getPreparationInfo() {
    return preparationInfo;
  }

  public void setPreparationInfo(String preparationInfo) {
    this.preparationInfo = preparationInfo;
  }

  public String getTransportInfo() {
    return transportInfo;
  }

  public void setTransportInfo(String transportInfo) {
    this.transportInfo = transportInfo;
  }

  public String getStorageInfo() {
    return storageInfo;
  }

  public void setStorageInfo(String storageInfo) {
    this.storageInfo = storageInfo;
  }

  public boolean getCanBeIssued() {
    return canBeIssued;
  }

  public void setCanBeIssued(boolean canBeIssued) {
    this.canBeIssued = canBeIssued;
  }

  public boolean getContainsPlasma() {
    return containsPlasma;
  }

  public void setContainsPlasma(boolean containsPlasma) {
    this.containsPlasma = containsPlasma;
  }

  public Double getGravity() {
    return gravity;
  }

  public void setGravity(Double gravity) {
    this.gravity = gravity;
  }

  public void copy(ComponentType componentType) {
    this.componentTypeName = componentType.getComponentTypeName();
    this.componentTypeCode = componentType.getComponentTypeCode();
    this.expiresAfter = componentType.getExpiresAfter();
    this.expiresAfterUnits = componentType.getExpiresAfterUnits();
    this.description = componentType.getDescription();
    this.hasBloodGroup = componentType.getHasBloodGroup();
    this.componentTypeCombinations = componentType.getComponentTypeCombinations();
    this.producedComponentTypeCombinations = componentType.getProducedComponentTypeCombinations();
    this.highStorageTemperature = componentType.getHighStorageTemperature();
    this.lowStorageTemperature = componentType.getLowStorageTemperature();
    this.lowTransportTemperature = componentType.getLowTransportTemperature();
    this.highTransportTemperature = componentType.getHighTransportTemperature();
    this.preparationInfo = componentType.getPreparationInfo();
    this.transportInfo = componentType.getTransportInfo();
    this.storageInfo = componentType.getStorageInfo();
    this.canBeIssued = componentType.getCanBeIssued();
    this.isDeleted = componentType.getIsDeleted();
    this.containsPlasma = componentType.getContainsPlasma();
    this.maxBleedTime = componentType.getMaxBleedTime();
    this.maxTimeSinceDonation = componentType.getMaxTimeSinceDonation();
    this.gravity = componentType.getGravity();
  }

  /**
   * Returns the max bleed time in minutes
   * @return
   */
  public Integer getMaxBleedTime() {
    return maxBleedTime;
  }

  /**
   *  Sets the max bleed time (in minutes)
   * @param maxBleedTime
   */
  public void setMaxBleedTime(Integer maxBleedTime) {
    this.maxBleedTime = maxBleedTime;
  }

  /**
   * Returns the max time since donation in hours
   * @return
   */
  public Integer getMaxTimeSinceDonation() {
    return maxTimeSinceDonation;
  }

  /**
   * Sets the  max time since donation (in hours)
   * @param maxTimeSinceDonation
   */
  public void setMaxTimeSinceDonation(Integer maxTimeSinceDonation) {
    this.maxTimeSinceDonation = maxTimeSinceDonation;
  }

}
