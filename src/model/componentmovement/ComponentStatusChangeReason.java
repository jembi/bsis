package model.componentmovement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import model.BaseEntity;
import repository.constant.ComponentStatusChangeReasonNamedQueryConstants;

import org.hibernate.envers.Audited;

@NamedQueries({
  @NamedQuery(name = ComponentStatusChangeReasonNamedQueryConstants.NAME_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY,
      query = ComponentStatusChangeReasonNamedQueryConstants.QUERY_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY),
  @NamedQuery(name = ComponentStatusChangeReasonNamedQueryConstants.NAME_COUNT_DISCARD_REASON_WITH_ID,
      query = ComponentStatusChangeReasonNamedQueryConstants.QUERY_COUNT_DISCARD_REASON_WITH_ID)  
})
@Entity
@Audited
public class ComponentStatusChangeReason extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 100)
  private String statusChangeReason;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private ComponentStatusChangeReasonCategory category;

  private Boolean isDeleted;

  public ComponentStatusChangeReason() {
  }

  public String getStatusChangeReason() {
    return statusChangeReason;
  }

  public void setStatusChangeReason(String statusChangeReason) {
    this.statusChangeReason = statusChangeReason;
  }

  public ComponentStatusChangeReasonCategory getCategory() {
    return category;
  }

  public void setCategory(ComponentStatusChangeReasonCategory category) {
    this.category = category;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void copy(ComponentStatusChangeReason componentStatusChangeReason) {
    this.setId(componentStatusChangeReason.getId());
    this.setCategory(componentStatusChangeReason.getCategory());
    this.setStatusChangeReason(componentStatusChangeReason.getStatusChangeReason());
    this.setIsDeleted(componentStatusChangeReason.getIsDeleted());
  }
}
