package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PackTypeViewModel;

public class DonationViewModelBuilder extends AbstractBuilder<DonationViewModel> {

  private UUID id;
  private PackTypeViewModel packType;
  private Date donationDate;
  private String donationIdentificationNumber;
  private String donorNumber;
  private DonationTypeViewModel donationType;
  private TTIStatus ttiStatus;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private String bloodAbo;
  private String bloodRh;
  private LocationViewModel venue;
  private boolean released;

  public DonationViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DonationViewModelBuilder withPackType(PackTypeViewModel packType) {
    this.packType = packType;
    return this;
  }

  public DonationViewModelBuilder withDonationDate(Date donationDate) {
    this.donationDate = donationDate;
    return this;
  }

  public DonationViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public DonationViewModelBuilder withDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
    return this;
  }

  public DonationViewModelBuilder withDonationType(DonationTypeViewModel donationType) {
    this.donationType = donationType;
    return this;
  }

  public DonationViewModelBuilder withTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
    return this;
  }

  public DonationViewModelBuilder withBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
    return this;
  }

  public DonationViewModelBuilder withBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
    return this;
  }

  public DonationViewModelBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public DonationViewModelBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public DonationViewModelBuilder withVenue(LocationViewModel venue) {
    this.venue = venue;
    return this;
  }

  public DonationViewModelBuilder thatIsReleased() {
    this.released = true;
    return this;
  }

  @Override
  public DonationViewModel build() {
    DonationViewModel donationViewModel = new DonationViewModel();
    donationViewModel.setId(id);
    donationViewModel.setPackType(packType);
    donationViewModel.setDonationDate(donationDate);
    donationViewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    donationViewModel.setDonorNumber(donorNumber);
    donationViewModel.setDonationType(donationType);
    donationViewModel.setTTIStatus(ttiStatus);
    donationViewModel.setBloodTypingStatus(bloodTypingStatus);
    donationViewModel.setBloodTypingMatchStatus(bloodTypingMatchStatus);
    donationViewModel.setBloodAbo(bloodAbo);
    donationViewModel.setBloodRh(bloodRh);
    donationViewModel.setVenue(venue);
    donationViewModel.setReleased(released);
    return donationViewModel;
  }

  public static DonationViewModelBuilder aDonationViewModel() {
    return new DonationViewModelBuilder();
  }
}
