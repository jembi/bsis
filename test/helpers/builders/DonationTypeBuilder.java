package helpers.builders;

import model.donationtype.DonationType;

public class DonationTypeBuilder extends AbstractEntityBuilder<DonationType> {

  private Long id;
  private String name;
  private Boolean isDeleted;

  public DonationTypeBuilder withId(Long id) {
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

  @Override
  public DonationType build() {
    DonationType donationType = new DonationType();
    donationType.setId(id);
    donationType.setDonationType(name);
    donationType.setIsDeleted(isDeleted);
    return donationType;
  }

  public static DonationTypeBuilder aDonationType() {
    return new DonationTypeBuilder();
  }

}
