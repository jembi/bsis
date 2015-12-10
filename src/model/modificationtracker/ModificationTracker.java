package model.modificationtracker;

import model.user.User;

import java.util.Date;

public interface ModificationTracker {

  public Date getLastUpdated();

  public void setLastUpdated(Date lastUpdated);

  public Date getCreatedDate();

  public void setCreatedDate(Date createdDate);

  public User getCreatedBy();

  public void setCreatedBy(User createdBy);

  public User getLastUpdatedBy();

  public void setLastUpdatedBy(User lastUpdatedBy);
}