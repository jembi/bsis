package org.jembi.bsis.model;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.validation.Valid;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.modificationtracker.ModificationTracker;
import org.jembi.bsis.model.modificationtracker.RowModificationTracker;
import org.jembi.bsis.model.user.User;

/**
 * Extension of the BaseEntity class that adds tracking information to every instance of the
 * entity.
 *
 * @see org.jembi.bsis.model.modificationtracker.RowModificationTracker
 */
@MappedSuperclass
@Audited
@SuppressWarnings("serial")
public abstract class BaseModificationTrackerEntity extends BaseEntity implements ModificationTracker {

  @Valid
  @Embedded
  private RowModificationTracker modificationTracker;

  public BaseModificationTrackerEntity() {
    modificationTracker = new RowModificationTracker();
  }

  @Override
  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  @Override
  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  @Override
  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  @Override
  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  @Override
  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  @Override
  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  @Override
  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  @Override
  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }

  public RowModificationTracker getModificationTracker() {
    return modificationTracker;
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    this.modificationTracker = modificationTracker;
  }
}