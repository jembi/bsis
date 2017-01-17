package org.jembi.bsis.model.donationtype;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerEntity;

@Entity
@Audited
public class DonationType extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 50)
  private String donationType;

  private Boolean isDeleted;

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getDonationType() {
    return donationType;
  }

  public void setDonationType(String donorType) {
    this.donationType = donorType;
  }
}