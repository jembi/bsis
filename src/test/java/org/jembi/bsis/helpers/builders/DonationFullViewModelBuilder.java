package org.jembi.bsis.helpers.builders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;

public class DonationFullViewModelBuilder extends AbstractBuilder<DonationFullViewModel> {

  private UUID id;
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
  private LocationViewModel venue;
  private boolean released;

  public DonationFullViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DonationFullViewModelBuilder withPermission(String key, Boolean value) {
    if (permissions == null) {
      permissions = new HashMap<>();
    }
    permissions.put(key, value);
    return this;
  }

  public DonationFullViewModelBuilder withAdverseEvent(AdverseEventViewModel adverseEvent) {
    this.adverseEvent = adverseEvent;
    return this;
  }

  public DonationFullViewModelBuilder withPackType(PackTypeFullViewModel packType) {
    this.packType = packType;
    return this;
  }

  public DonationFullViewModelBuilder withDonationDate(Date donationDate) {
    this.donationDate = donationDate;
    return this;
  }

  public DonationFullViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public DonationFullViewModelBuilder withDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
    return this;
  }

  public DonationFullViewModelBuilder withDonationType(DonationTypeViewModel donationType) {
    this.donationType = donationType;
    return this;
  }

  public DonationFullViewModelBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public DonationFullViewModelBuilder withTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
    return this;
  }

  public DonationFullViewModelBuilder withDonationBatchNumber(String donationBatchNumber) {
    this.donationBatchNumber = donationBatchNumber;
    return this;
  }

  public DonationFullViewModelBuilder withBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
    return this;
  }

  public DonationFullViewModelBuilder withBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
    return this;
  }

  public DonationFullViewModelBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public DonationFullViewModelBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public DonationFullViewModelBuilder withHaemoglobinCount(BigDecimal haemoglobinCount) {
    this.haemoglobinCount = haemoglobinCount;
    return this;
  }

  public DonationFullViewModelBuilder withHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
    this.haemoglobinLevel = haemoglobinLevel;
    return this;
  }

  public DonationFullViewModelBuilder withDonorWeight(BigDecimal donorWeight) {
    this.donorWeight = donorWeight;
    return this;
  }

  public DonationFullViewModelBuilder withDonorPulse(Integer donorPulse) {
    this.donorPulse = donorPulse;
    return this;
  }

  public DonationFullViewModelBuilder withBloodPressureSystolic(Integer bloodPressureSystolic) {
    this.bloodPressureSystolic = bloodPressureSystolic;
    return this;
  }

  public DonationFullViewModelBuilder withBloodPressureDiastolic(Integer bloodPressureDiastolic) {
    this.bloodPressureDiastolic = bloodPressureDiastolic;
    return this;
  }

  public DonationFullViewModelBuilder withBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
    return this;
  }

  public DonationFullViewModelBuilder withBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
    return this;
  }

  public DonationFullViewModelBuilder withVenue(LocationViewModel venue) {
    this.venue = venue;
    return this;
  }

  public DonationFullViewModelBuilder thatIsReleased() {
    this.released = true;
    return this;
  }

  @Override
  public DonationFullViewModel build() {
    DonationFullViewModel donationFullViewModel = new DonationFullViewModel();
    donationFullViewModel.setId(id);
    donationFullViewModel.setPermissions(permissions);
    donationFullViewModel.setAdverseEvent(adverseEvent);
    donationFullViewModel.setPackType(packType);
    donationFullViewModel.setDonationDate(donationDate);
    donationFullViewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    donationFullViewModel.setDonorNumber(donorNumber);
    donationFullViewModel.setDonationType(donationType);
    donationFullViewModel.setNotes(notes);
    donationFullViewModel.setTTIStatus(ttiStatus);
    donationFullViewModel.setDonationBatchNumber(donationBatchNumber);
    donationFullViewModel.setBloodTypingStatus(bloodTypingStatus);
    donationFullViewModel.setBloodTypingMatchStatus(bloodTypingMatchStatus);
    donationFullViewModel.setBloodAbo(bloodAbo);
    donationFullViewModel.setBloodRh(bloodRh);
    donationFullViewModel.setHaemoglobinCount(haemoglobinCount);
    donationFullViewModel.setHaemoglobinLevel(haemoglobinLevel);
    donationFullViewModel.setDonorWeight(donorWeight);
    donationFullViewModel.setDonorPulse(donorPulse);
    donationFullViewModel.setBloodPressureSystolic(bloodPressureSystolic);
    donationFullViewModel.setBloodPressureDiastolic(bloodPressureDiastolic);
    donationFullViewModel.setBleedStartTime(bleedStartTime);
    donationFullViewModel.setBleedEndTime(bleedEndTime);
    donationFullViewModel.setVenue(venue);
    donationFullViewModel.setReleased(released);
    return donationFullViewModel;
  }

  public static DonationFullViewModelBuilder aDonationFullViewModel() {
    return new DonationFullViewModelBuilder();
  }

}
