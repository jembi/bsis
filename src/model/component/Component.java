package model.component;

import java.util.Date;
import java.util.List;

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

import model.BaseModificationTrackerEntity;
import model.compatibility.CompatibilityTest;
import model.componentmovement.ComponentStatusChange;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.request.Request;
import model.usage.ComponentUsage;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import repository.ComponentNamedQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import constraintvalidator.ComponentTypeExists;
import constraintvalidator.DonationExists;

@NamedQueries({
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_UPDATE_COMPONENT_STATUSES_FOR_DONOR,
        query = ComponentNamedQueryConstants.QUERY_UPDATE_COMPONENT_STATUSES_FOR_DONOR),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_COUNT_CHANGED_COMPONENTS_FOR_DONATION,
        query = ComponentNamedQueryConstants.QUERY_COUNT_CHANGED_COMPONENTS_FOR_DONATION),
    @NamedQuery(name = ComponentNamedQueryConstants.NAME_UPDATE_COMPONENT_STATUSES_FOR_DONATION,
        query = ComponentNamedQueryConstants.QUERY_UPDATE_COMPONENT_STATUSES_FOR_DONATION),
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Component extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

  // A component may not have a corresponding donation. Some components may be
  // imported from another location. In such a case the corresponding donation
  // field is allowed to be null.
  @DonationExists
  @ManyToOne(optional = true, fetch = FetchType.EAGER)
  private Donation donation;

  @ComponentTypeExists
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

  @ManyToOne
  private Request issuedTo;

  @Temporal(TemporalType.TIMESTAMP)
  private Date issuedOn;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private ComponentStatus status;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "testedComponent", fetch = FetchType.LAZY)
  private List<CompatibilityTest> compatibilityTests;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "component", fetch = FetchType.LAZY)
  private List<ComponentStatusChange> statusChanges;

  @Column(length = 3)
  private String subdivisionCode;

  @OneToOne(optional = true)
  private Component parentComponent;

  @OneToOne(mappedBy = "component")
  private ComponentUsage usage;

  @Lob
  private String notes;

  private Boolean isDeleted;

  @Column(length = 20)
  private String componentIdentificationNumber;

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
    this.componentIdentificationNumber = component.componentIdentificationNumber;
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

  public List<CompatibilityTest> getCompatibilityTests() {
    return compatibilityTests;
  }

  public void setCompatibilityTests(List<CompatibilityTest> compatibilityTests) {
    this.compatibilityTests = compatibilityTests;
  }

  public Date getDiscardedOn() {
    return discardedOn;
  }

  public void setDiscardedOn(Date discardedOn) {
    this.discardedOn = discardedOn;
  }

  public Request getIssuedTo() {
    return issuedTo;
  }

  public void setIssuedTo(Request issuedTo) {
    this.issuedTo = issuedTo;
  }

  public Date getIssuedOn() {
    return issuedOn;
  }

  public void setIssuedOn(Date issuedOn) {
    this.issuedOn = issuedOn;
  }

  public List<ComponentStatusChange> getStatusChanges() {
    return statusChanges;
  }

  public void setStatusChanges(List<ComponentStatusChange> statusChanges) {
    this.statusChanges = statusChanges;
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

  public String getComponentIdentificationNumber() {
    return componentIdentificationNumber;
  }

  public void setComponentIdentificationNumber(String componentIdentificationNumber) {
    this.componentIdentificationNumber = componentIdentificationNumber;
  }

}
