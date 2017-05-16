package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.user.User;

public class DonationTypeBuilder extends AbstractEntityBuilder<DonationType> {

  private UUID id;
  private String name;
  private Boolean isDeleted;
  private Date createdDate;
  private User createdBy;
  private Date lastUpdated;
  private User lastUpdatedBy;

  public DonationTypeBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DonationTypeBuilder withName(String name) {
    this.name = name;
    return this;
  }
  
  public DonationTypeBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }
  
  public DonationTypeBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public DonationTypeBuilder withCreatedBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public DonationTypeBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public DonationTypeBuilder withLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
    return this;
  }

  public DonationTypeBuilder withLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  @Override
  public DonationType build() {
    DonationType donationType = new DonationType();
    donationType.setId(id);
    donationType.setDonationType(name);
    donationType.setIsDeleted(isDeleted);
    donationType.setCreatedBy(createdBy);
    donationType.setCreatedDate(createdDate);
    donationType.setLastUpdated(lastUpdated);
    donationType.setLastUpdatedBy(lastUpdatedBy);
    return donationType;
  }

  public static DonationTypeBuilder aDonationType() {
    return new DonationTypeBuilder();
  }

}
