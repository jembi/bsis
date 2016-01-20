package model.modificationtracker;

import model.user.User;

import java.util.Date;

public interface ModificationTracker {

  Date getLastUpdated();

  void setLastUpdated(Date lastUpdated);

  Date getCreatedDate();

  void setCreatedDate(Date createdDate);

  User getCreatedBy();

  void setCreatedBy(User createdBy);

  User getLastUpdatedBy();

  void setLastUpdatedBy(User lastUpdatedBy);
}