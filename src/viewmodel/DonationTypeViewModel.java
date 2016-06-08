package viewmodel;

import model.donationtype.DonationType;

public class DonationTypeViewModel extends BaseViewModel {

  private DonationType donationType;

  public DonationTypeViewModel(DonationType donationType) {
    this.donationType = donationType;
  }

  @Override
  public Long getId() {
    return donationType.getId();
  }

  public String getType() {
    return donationType.getDonationType();
  }

  public Boolean getIsDeleted() {
    return donationType.getIsDeleted();
  }
}
