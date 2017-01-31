package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.DonationTypeViewModel;

public class DonationTypeViewModelBuilder extends AbstractBuilder<DonationTypeViewModel> {

  private Long id;
  private String type;
  private Boolean isDeleted;

  public DonationTypeViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public DonationTypeViewModelBuilder withType(String type) {
    this.type = type;
    return this;
  }

  public DonationTypeViewModelBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }

  public DonationTypeViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public DonationTypeViewModel build() {
    DonationTypeViewModel donationType = new DonationTypeViewModel();
    donationType.setId(id);
    donationType.setType(type);
    donationType.setIsDeleted(isDeleted);
    return donationType;
  }

  public static DonationTypeViewModelBuilder aDonationTypeViewModel() {
    return new DonationTypeViewModelBuilder();
  }
}
