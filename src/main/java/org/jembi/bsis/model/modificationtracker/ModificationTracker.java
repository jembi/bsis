package org.jembi.bsis.model.modificationtracker;

import java.util.Date;

import org.jembi.bsis.model.user.User;

public interface ModificationTracker {

  public Date getLastUpdated();

  public Date getCreatedDate();

  public User getCreatedBy();

  public User getLastUpdatedBy();

  public void setLastUpdated(Date lastUpdated);

  public void setCreatedDate(Date createdDate);

  public void setCreatedBy(User createdBy);

  public void setLastUpdatedBy(User lastUpdatedBy);
}