package org.jembi.bsis.model.component;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.jembi.bsis.constraintvalidator.ComponentStatusConsistency;
import org.jembi.bsis.model.BaseModificationTrackerEntity;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.usage.ComponentUsage;
import org.jembi.bsis.repository.ComponentNamedQueryConstants;
import org.jembi.bsis.repository.InventoryNamedQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@NamedQueries({
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_UPDATE_COMPONENT_STATUSES_FOR_DONOR,
        query = ComponentNamedQueryConstants.QUERY_UPDATE_COMPONENT_STATUSES_FOR_DONOR),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_COUNT_CHANGED_COMPONENTS_FOR_DONATION,
        query = ComponentNamedQueryConstants.QUERY_COUNT_CHANGED_COMPONENTS_FOR_DONATION),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_UPDATE_COMPONENT_STATUSES_FOR_DONATION,
        query = ComponentNamedQueryConstants.QUERY_UPDATE_COMPONENT_STATUSES_FOR_DONATION),
    @NamedQuery(name = InventoryNamedQueryConstants.NAME_FIND_STOCK_LEVELS_FOR_LOCATION,
        query = InventoryNamedQueryConstants.QUERY_FIND_STOCK_LEVELS_FOR_LOCATION),
    @NamedQuery(name = InventoryNamedQueryConstants.NAME_FIND_STOCK_LEVELS,
        query = InventoryNamedQueryConstants.QUERY_FIND_STOCK_LEVELS),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_FIND_COMPONENTS_BY_DIN,
        query = ComponentNamedQueryConstants.QUERY_FIND_COMPONENTS_BY_DIN),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_FIND_COMPONENT_BY_CODE_AND_DIN,
        query = ComponentNamedQueryConstants.QUERY_FIND_COMPONENT_BY_CODE_AND_DIN),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_FIND_COMPONENT_BY_CODE_AND_DIN_IN_STOCK,
        query = ComponentNamedQueryConstants.QUERY_FIND_COMPONENT_BY_CODE_AND_DIN_IN_STOCK),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_COUNT_COMPONENT_WITH_ID,
        query = ComponentNamedQueryConstants.QUERY_COUNT_COMPONENT_WITH_ID),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_FIND_CHILD_COMPONENTS,
        query = ComponentNamedQueryConstants.QUERY_FIND_CHILD_COMPONENTS)
})
@Entity
@Audited
@ComponentStatusConsistency
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Component extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(optional = true, fetch = FetchType.EAGER)
  private Donation donation;

  @ManyToOne
  private ComponentType componentType;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

  @Temporal(TemporalType.TIMESTAMP)
  @Index(name = "component_expiresOn_index")
  private Date expiresOn;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "DATETIME")
  private Date discardedOn;

  @Temporal(TemporalType.TIMESTAMP)
  private Date issuedOn;

  @Enumerated(EnumType.STRING)
  @Column(length = 30, nullable = false)
  private ComponentStatus status;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "component", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @Where(clause = "isDeleted = 0")
  @SortNatural
  private SortedSet<ComponentStatusChange> statusChanges;

  @Column(length = 3)
  private String subdivisionCode;

  @OneToOne(optional = true)
  private Component parentComponent;

  @OneToOne(mappedBy = "component")
  private ComponentUsage usage;
  
  @ManyToOne
  private ComponentBatch componentBatch;

  @Lob
  private String notes;

  private Boolean isDeleted;

  @Column(length = 20)
  private String componentCode;
  
  @Column(length = 30, nullable = false)
  @Enumerated(EnumType.STRING)
  private InventoryStatus inventoryStatus = InventoryStatus.NOT_IN_STOCK;
  
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private Location location;
  
  private Integer weight;

  public Component() {
    super();
  }

  public void copy(Component component) {
    assert (this.getId().equals(component.getId()));
    this.donation = component.donation;
    this.componentType = component.componentType;
    this.createdOn = component.createdOn;
    this.expiresOn = component.expiresOn;
    this.notes = component.notes;
    this.componentCode = component.componentCode;
    this.location = component.location;
    this.inventoryStatus = component.inventoryStatus;
  }

  public Donation getDonation() {
    return donation;
  }

  public ComponentType getComponentType() {
    return componentType;
  }

  public Date getExpiresOn() {
    return expiresOn;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setDonation(Donation donation) {
    this.donation = donation;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  public void setExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public String getDonationIdentificationNumber() {
    if (donation == null)
      return null;
    return donation.getDonationIdentificationNumber();
  }

  public ComponentStatus getStatus() {
    return status;
  }

  public void setStatus(ComponentStatus status) {
    this.status = status;
  }

  public Date getDiscardedOn() {
    return discardedOn;
  }

  public void setDiscardedOn(Date discardedOn) {
    this.discardedOn = discardedOn;
  }

  public Date getIssuedOn() {
    return issuedOn;
  }

  public void setIssuedOn(Date issuedOn) {
    this.issuedOn = issuedOn;
  }

  public SortedSet<ComponentStatusChange> getStatusChanges() {
    return statusChanges;
  }

  public void setStatusChanges(SortedSet<ComponentStatusChange> statusChanges) {
    this.statusChanges = statusChanges;
  }

  public void addStatusChange(ComponentStatusChange statusChange) {
    if (statusChanges == null) {
      statusChanges = new TreeSet<>();
    }
    statusChanges.add(statusChange);
  }

  public ComponentUsage getUsage() {
    return usage;
  }

  public void setUsage(ComponentUsage usage) {
    this.usage = usage;
  }

  public String getSubdivisionCode() {
    return subdivisionCode;
  }

  public void setSubdivisionCode(String subdivisionCode) {
    this.subdivisionCode = subdivisionCode;
  }

  public Component getParentComponent() {
    return parentComponent;
  }

  public void setParentComponent(Component parentComponent) {
    this.parentComponent = parentComponent;
  }

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  public ComponentBatch getComponentBatch() {
    return componentBatch;
  }

  public void setComponentBatch(ComponentBatch componentBatch) {
    this.componentBatch = componentBatch;
  }

  public InventoryStatus getInventoryStatus() {
    return inventoryStatus;
  }

  public void setInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }
}
