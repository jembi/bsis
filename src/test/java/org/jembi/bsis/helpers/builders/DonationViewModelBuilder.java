package org.jembi.bsis.helpers.builders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;

public class DonationViewModelBuilder extends AbstractBuilder<DonationViewModel> {

  private Long id;
  private Map<String, Boolean> permissions;
  private AdverseEventViewModel adverseEvent;
  private PackTypeFullViewModel packType;
  private Date donationDate;
  private String donationIdentificationNumber;
  private String donorNumber;
  private DonationTypeViewModel donationType;
  private String notes;
  private TTIStatus ttiStatus;
  private String donationBatchNumber;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private String bloodAbo;
  private String bloodRh;
  private BigDecimal haemoglobinCount;
  private HaemoglobinLevel haemoglobinLevel;
  private BigDecimal donorWeight;
  private Integer donorPulse;
  private Integer bloodPressureSystolic;
  private Integer bloodPressureDiastolic;
  private Date bleedStartTime;
  private Date bleedEndTime;
  private LocationFullViewModel venue;
  private boolean released;

  public DonationViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public DonationViewModelBuilder withPermission(String key, Boolean value) {
    if (permissions == null) {
      permissions = new HashMap<>();
    }
    permissions.put(key, value);
    return this;
  }

  public DonationViewModelBuilder withAdverseEvent(AdverseEventViewModel adverseEvent) {
    this.adverseEvent = adverseEvent;
    return this;
  }

  public DonationViewModelBuilder withPackType(PackTypeFullViewModel packType) {
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

  public DonationViewModelBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public DonationViewModelBuilder withTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
    return this;
  }

  public DonationViewModelBuilder withDonationBatchNumber(String donationBatchNumber) {
    this.donationBatchNumber = donationBatchNumber;
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

  public DonationViewModelBuilder withHaemoglobinCount(BigDecimal haemoglobinCount) {
    this.haemoglobinCount = haemoglobinCount;
    return this;
  }

  public DonationViewModelBuilder withHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
    this.haemoglobinLevel = haemoglobinLevel;
    return this;
  }

  public DonationViewModelBuilder withDonorWeight(BigDecimal donorWeight) {
    this.donorWeight = donorWeight;
    return this;
  }

  public DonationViewModelBuilder withDonorPulse(Integer donorPulse) {
    this.donorPulse = donorPulse;
    return this;
  }

  public DonationViewModelBuilder withBloodPressureSystolic(Integer bloodPressureSystolic) {
    this.bloodPressureSystolic = bloodPressureSystolic;
    return this;
  }

  public DonationViewModelBuilder withBloodPressureDiastolic(Integer bloodPressureDiastolic) {
    this.bloodPressureDiastolic = bloodPressureDiastolic;
    return this;
  }

  public DonationViewModelBuilder withBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
    return this;
  }

  public DonationViewModelBuilder withBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
    return this;
  }

  public DonationViewModelBuilder withVenue(LocationFullViewModel venue) {
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
    donationViewModel.setPermissions(permissions);
    donationViewModel.setAdverseEvent(adverseEvent);
    donationViewModel.setPackType(packType);
    donationViewModel.setDonationDate(donationDate);
    donationViewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    donationViewModel.setDonorNumber(donorNumber);
    donationViewModel.setDonationType(donationType);
    donationViewModel.setNotes(notes);
    donationViewModel.setTTIStatus(ttiStatus);
    donationViewModel.setDonationBatchNumber(donationBatchNumber);
    donationViewModel.setBloodTypingStatus(bloodTypingStatus);
    donationViewModel.setBloodTypingMatchStatus(bloodTypingMatchStatus);
    donationViewModel.setBloodAbo(bloodAbo);
    donationViewModel.setBloodRh(bloodRh);
    donationViewModel.setHaemoglobinCount(haemoglobinCount);
    donationViewModel.setHaemoglobinLevel(haemoglobinLevel);
    donationViewModel.setDonorWeight(donorWeight);
    donationViewModel.setDonorPulse(donorPulse);
    donationViewModel.setBloodPressureSystolic(bloodPressureSystolic);
    donationViewModel.setBloodPressureDiastolic(bloodPressureDiastolic);
    donationViewModel.setBleedStartTime(bleedStartTime);
    donationViewModel.setBleedEndTime(bleedEndTime);
    donationViewModel.setVenue(venue);
    donationViewModel.setReleased(released);
    return donationViewModel;
  }

  public static DonationViewModelBuilder aDonationViewModel() {
    return new DonationViewModelBuilder();
  }

}
