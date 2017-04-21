package org.jembi.bsis.model.componentmovement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseUUIDEntity;
import org.jembi.bsis.repository.constant.ComponentStatusChangeReasonNamedQueryConstants;

@NamedQueries({
  @NamedQuery(name = ComponentStatusChangeReasonNamedQueryConstants.NAME_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY,
      query = ComponentStatusChangeReasonNamedQueryConstants.QUERY_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY),
  @NamedQuery(name = ComponentStatusChangeReasonNamedQueryConstants.NAME_COUNT_DISCARD_REASON_WITH_ID,
      query = ComponentStatusChangeReasonNamedQueryConstants.QUERY_COUNT_DISCARD_REASON_WITH_ID),
  @NamedQuery(name = ComponentStatusChangeReasonNamedQueryConstants.NAME_FIND_COMPONENT_STATUS_CHANGE_REASON_BY_CATEGORY_AND_TYPE,
      query = ComponentStatusChangeReasonNamedQueryConstants.QUERY_FIND_COMPONENT_STATUS_CHANGE_REASON_BY_CATEGORY_AND_TYPE)
})
@Entity
@Audited
public class ComponentStatusChangeReason extends BaseUUIDEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 100)
  private String statusChangeReason;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private ComponentStatusChangeReasonCategory category;

  @Enumerated(EnumType.STRING)
  @Column(length = 30, nullable = true)
  private ComponentStatusChangeReasonType type;

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
    this.setType(componentStatusChangeReason.getType());
  }

  public ComponentStatusChangeReasonType getType() {
    return type;
  }

  public void setType(ComponentStatusChangeReasonType type) {
    this.type = type;
  }
}
