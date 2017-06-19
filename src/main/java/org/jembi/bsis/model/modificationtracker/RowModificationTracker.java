package org.jembi.bsis.model.modificationtracker;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jembi.bsis.model.user.User;

/**
 * Hibernate will not be able to generate schema for embedded modificationTracker if we do not
 * implement the interface.
 *
 * @author iamrohitbanga
 */
@Embeddable
public class RowModificationTracker implements ModificationTracker, Serializable {

  private static final long serialVersionUID = 8988186756639849992L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP")
  private Date lastUpdated;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP")
  private Date createdDate;

  @ManyToOne
  private User createdBy;

  @ManyToOne
  private User lastUpdatedBy;

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public User getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }
}
