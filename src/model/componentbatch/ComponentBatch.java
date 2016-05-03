package model.componentbatch;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import model.BaseModificationTrackerEntity;
import model.component.Component;
import model.donationbatch.DonationBatch;
import model.location.Location;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import repository.ComponentBatchNamedQueryConstants;
import constraintvalidator.LocationExists;

@NamedQueries({
  @NamedQuery(name = ComponentBatchNamedQueryConstants.NAME_FIND_COMPONENT_BATCHES_BY_STATUS,
      query = ComponentBatchNamedQueryConstants.QUERY_FIND_COMPONENT_BATCHES_BY_STATUS),
  @NamedQuery(name = ComponentBatchNamedQueryConstants.NAME_FIND_COMPONENT_BATCH_BY_ID,
      query = ComponentBatchNamedQueryConstants.QUERY_FIND_COMPONENT_BATCH_BY_ID)
})
@Entity
@Audited
public class ComponentBatch extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;
  
  @OneToOne
  @LocationExists
  @NotNull
  private Location venue;

  @SuppressWarnings("unchecked")
  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "componentBatch", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @Where(clause = "isDeleted = 0")
  private Set<BloodTransportBox> bloodTransportBoxes = Collections.EMPTY_SET;
  
  @SuppressWarnings("unchecked")
  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "componentBatch", fetch = FetchType.EAGER)
  @Where(clause = "isDeleted = 0")
  private Set<Component> components = Collections.EMPTY_SET;
  
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ComponentBatchStatus status = ComponentBatchStatus.OPEN;
  
  @Column
  private int bloodTransportBoxCount;
  
  @Column
  private Date collectionDate;
  
  @Column
  private Date deliveryDate;
  
  @Column
  private boolean isDeleted = false;
  
  // This should have been a OneToOne relationship but we couldn't manage to solve an error saying "cannot simultaneously fetch multiple bags". 
  // The solution was to use a set of DonationBatch objects instead of just one donationBatch.
  @OneToMany(mappedBy = "componentBatch")
  private Set<DonationBatch> donationBatches = new HashSet<DonationBatch>();

  public ComponentBatch() {
    super();
  }
  
  public Location getVenue() {
    return venue;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
  }

  public ComponentBatchStatus getStatus() {
    return status;
  }

  public void setStatus(ComponentBatchStatus status) {
    this.status = status;
  }

  public Set<BloodTransportBox> getBloodTransportBoxes() {
    return bloodTransportBoxes;
  }

  public void setBloodTransportBoxes(Set<BloodTransportBox> bloodTransportBoxes) {
    this.bloodTransportBoxes = bloodTransportBoxes;
  }

  public Set<Component> getComponents() {
    return components;
  }

  public void setComponents(Set<Component> components) {
    this.components = components;
  }

  public int getBloodTransportBoxCount() {
    return bloodTransportBoxCount;
  }

  public void setBloodTransportBoxCount(int bloodTransportBoxCount) {
    this.bloodTransportBoxCount = bloodTransportBoxCount;
  }

  public Date getCollectionDate() {
    return collectionDate;
  }

  public void setCollectionDate(Date collectionDate) {
    this.collectionDate = collectionDate;
  }

  public Date getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public DonationBatch getDonationBatch() {
    if (donationBatches == null || donationBatches.isEmpty()) {
      return null;
    }
    return donationBatches.iterator().next();
  }

  public void setDonationBatch(DonationBatch donationBatch) {
    donationBatches.add(donationBatch);
  }
}
