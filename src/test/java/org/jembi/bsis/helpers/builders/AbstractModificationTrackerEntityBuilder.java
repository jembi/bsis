package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.model.BaseModificationTrackerEntity;
import org.jembi.bsis.model.user.User;

public abstract class AbstractModificationTrackerEntityBuilder<T extends BaseModificationTrackerEntity>
    extends AbstractEntityBuilder<T> {

  // row midificationt tracking fields
  private Date createdDate = null;
  private Date lastUpdatedDate = null;
  private User createdBy = null;
  private User lastUpdatedBy = null;

  public AbstractModificationTrackerEntityBuilder<T> withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public AbstractModificationTrackerEntityBuilder<T> withLastUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
    return this;
  }

  public AbstractModificationTrackerEntityBuilder<T> withCreatedBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public AbstractModificationTrackerEntityBuilder<T> withLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
    return this;
  }

  public T buildWithTrackingFields() {
    T entity = build();
    entity.setCreatedDate(createdDate);
    entity.setLastUpdated(lastUpdatedDate);
    entity.setCreatedBy(createdBy);
    entity.setLastUpdatedBy(lastUpdatedBy);
    return entity;
  }

}
