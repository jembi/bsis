package model.componenttype;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import model.BaseEntity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class ComponentType extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length=50)
  private String componentTypeName;

  @Column(length=30)
  private String componentTypeNameShort;

  private Integer expiresAfter;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private ComponentTypeTimeUnits expiresAfterUnits;

  @NotAudited
  @ManyToMany(mappedBy="componentTypes", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ComponentTypeCombination> componentTypeCombinations;
  
  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @ManyToMany(fetch=FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ComponentTypeCombination> producedComponentTypeCombinations;

  /**
   * TODO: Not used for now. Some component types like Cryoprecipitate may not require
   * blood group match before issuing. Usecase not clear yet.
   */
  private Boolean hasBloodGroup;

  /**
   * Pedi Component Type for this component type.
   * can be null. This allows us to determine the target type after splitting.
   */
  @OneToOne(optional=true)
  private ComponentType pediComponentType;

  @Lob
  private String description;

  private Boolean isDeleted;
  
  private Integer lowStorageTemperature;
  
  private Integer highStorageTemperature;
  
  private Integer lowTransportTemperature;
  
  private Integer highTransportTemperature;
  
  private String preparationInfo;

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

  public String getComponentTypeNameShort() {
    return componentTypeNameShort;
  }

  public void setComponentTypeNameShort(String componentTypeShortName) {
    this.componentTypeNameShort = componentTypeShortName;
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
    case HOURS: factor = 60;
                break;
    case DAYS:  factor = 24 * 60;
                break;
    case YEARS: factor = 365 * 24 * 60;
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
  
  public List<ComponentTypeCombination> getProducedComponentTypeCombinations() {
    return producedComponentTypeCombinations;
  }

  public void setProducedComponentTypeCombinations(List<ComponentTypeCombination> producedComponentTypeCombinations) {
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
    
    public void copy(ComponentType componentType){
    	this.componentTypeName = componentType.getComponentTypeName();
    	this.componentTypeNameShort = componentType.getComponentTypeNameShort();
    	this.expiresAfter = componentType.getExpiresAfter();
    	this.expiresAfterUnits = componentType.getExpiresAfterUnits();
    	this.description = componentType.getDescription();
    	this.hasBloodGroup = componentType.getHasBloodGroup();
    	this.componentTypeCombinations = componentType.getComponentTypeCombinations();
    	this.producedComponentTypeCombinations = componentType.getProducedComponentTypeCombinations();
    	this.highStorageTemperature =  componentType.getHighStorageTemperature();
    	this.lowStorageTemperature =  componentType.getLowStorageTemperature();
    	this.lowTransportTemperature = componentType.getLowTransportTemperature();
    	this.highTransportTemperature =  componentType.getHighTransportTemperature();
    	this.preparationInfo = componentType.getPreparationInfo();
    }
    
  
}
