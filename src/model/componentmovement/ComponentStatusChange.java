package model.componentmovement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import constraintvalidator.ComponentExists;
import model.BaseEntity;
import model.component.Component;
import model.component.ComponentStatus;
import model.user.User;

@Entity
@Audited
public class ComponentStatusChange extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @ComponentExists
  @ManyToOne
  private Component component;

  @Temporal(TemporalType.TIMESTAMP)
  private Date statusChangedOn;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private ComponentStatusChangeType statusChangeType;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private ComponentStatus newStatus;

  @ManyToOne
  private User changedBy;

  @Lob
  private String statusChangeReasonText;

  @ManyToOne
  private ComponentStatusChangeReason statusChangeReason;

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

  public ComponentStatusChangeType getStatusChangeType() {
    return statusChangeType;
  }

  public void setStatusChangeType(ComponentStatusChangeType statusChangeType) {
    this.statusChangeType = statusChangeType;
  }
}
