package org.jembi.bsis.model.componentmovement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseUUIDEntity;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.ComponentNamedQueryConstants;

import static org.jembi.bsis.repository.ComponentNamedQueryConstants.NAME_FIND_SUMMARY_FOR_DISCARDED_COMPONENTS_BY_PROCESSING_SITE;
import static org.jembi.bsis.repository.ComponentNamedQueryConstants.QUERY_FIND_SUMMARY_FOR_DISCARDED_COMPONENTS_BY_PROCESSING_SITE;
import static org.jembi.bsis.repository.component.ComponentStatusChangeRepository.NAME_FIND_REASONS_IN_CATEGORY_FOR_COMPONENT_ORDERED_BY_DATE_DESC;
import static org.jembi.bsis.repository.component.ComponentStatusChangeRepository.QUERY_FIND_REASONS_IN_CATEGORY_FOR_COMPONENT_ORDERED_BY_DATE_DESC;

@NamedQueries({
    @NamedQuery(name = NAME_FIND_SUMMARY_FOR_DISCARDED_COMPONENTS_BY_PROCESSING_SITE,
        query = QUERY_FIND_SUMMARY_FOR_DISCARDED_COMPONENTS_BY_PROCESSING_SITE),
    @NamedQuery(name = NAME_FIND_REASONS_IN_CATEGORY_FOR_COMPONENT_ORDERED_BY_DATE_DESC,
        query = QUERY_FIND_REASONS_IN_CATEGORY_FOR_COMPONENT_ORDERED_BY_DATE_DESC)})
@Entity
@Audited
public class ComponentStatusChange extends BaseUUIDEntity implements Comparable<ComponentStatusChange> {

  private static final long serialVersionUID = 1L;

  @ManyToOne(optional = false)
  private Component component;

  @Temporal(TemporalType.TIMESTAMP)
  private Date statusChangedOn;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private ComponentStatus newStatus;

  @ManyToOne
  private User changedBy;

  @Lob
  private String statusChangeReasonText;

  @ManyToOne(optional = false)
  private ComponentStatusChangeReason statusChangeReason;

  boolean isDeleted = false;

  public ComponentStatusChange() {
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

  public Date getStatusChangedOn() {
    return statusChangedOn;
  }

  public void setStatusChangedOn(Date statusChangedOn) {
    this.statusChangedOn = statusChangedOn;
  }

  public ComponentStatus getNewStatus() {
    return newStatus;
  }

  public void setNewStatus(ComponentStatus newStatus) {
    this.newStatus = newStatus;
  }

  public User getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(User changedBy) {
    this.changedBy = changedBy;
  }

  public String getStatusChangeReasonText() {
    return statusChangeReasonText;
  }

  public void setStatusChangeReasonText(String statusChangeReasonText) {
    this.statusChangeReasonText = statusChangeReasonText;
  }

  public User getIssuedBy() {
    return this.changedBy;
  }

  public void setIssuedBy(User issuedBy) {
    this.changedBy = issuedBy;
  }

  public Date getIssuedOn() {
    return this.statusChangedOn;
  }

  public void setIssuedOn(Date issuedOn) {
    this.statusChangedOn = issuedOn;
  }

  public ComponentStatusChangeReason getStatusChangeReason() {
    return statusChangeReason;
  }

  public void setStatusChangeReason(ComponentStatusChangeReason discardReason) {
    this.statusChangeReason = discardReason;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @Override
  public int compareTo(ComponentStatusChange other) {
    if (this.getStatusChangedOn() == null) {
      return -1;
    }
    if (other.getStatusChangedOn() == null) {
      return 1;
    }
    return this.getStatusChangedOn().compareTo(other.getStatusChangedOn());
  }
}
