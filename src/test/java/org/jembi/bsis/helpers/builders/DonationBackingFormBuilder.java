package org.jembi.bsis.helpers.builders;

import java.math.BigDecimal;
import java.util.Date;

import org.jembi.bsis.backingform.AdverseEventBackingForm;
import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.packtype.PackType;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DonationBackingFormBuilder extends AbstractBuilder<DonationBackingForm> {

  private static final DateTimeFormatter ISO_FORMAT = ISODateTimeFormat.dateTime();

  private Integer donorPulse;
  private BigDecimal haemoglobinCount;
  private HaemoglobinLevel haemoglobinLevel;
  private Integer bloodPressureSystolic;
  private Integer bloodPressureDiastolic;
  private BigDecimal donorWeight;
  private String notes;
  private PackType packType;
  private Date bleedStartTime;
  private Date bleedEndTime;
  private AdverseEventBackingForm adverseEventBackingForm;

  private String donationBatchNumber;

  private Donor donor;

  private Donation donation;

  public DonationBackingFormBuilder withDonorPulse(Integer donorPulse) {
    this.donorPulse = donorPulse;
    return this;
  }

  public DonationBackingFormBuilder withHaemoglobinCount(BigDecimal haemoglobinCount) {
    this.haemoglobinCount = haemoglobinCount;
    return this;
  }

  public DonationBackingFormBuilder withHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
    this.haemoglobinLevel = haemoglobinLevel;
    return this;
  }

  public DonationBackingFormBuilder withBloodPressureSystolic(Integer bloodPressureSystolic) {
    this.bloodPressureSystolic = bloodPressureSystolic;
    return this;
  }

  public DonationBackingFormBuilder withBloodPressureDiastolic(Integer bloodPressureDiastolic) {
    this.bloodPressureDiastolic = bloodPressureDiastolic;
    return this;
  }

  public DonationBackingFormBuilder withDonorWeight(BigDecimal donorWeight) {
    this.donorWeight = donorWeight;
    return this;
  }

  public DonationBackingFormBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public DonationBackingFormBuilder withPackType(PackType packType) {
    this.packType = packType;
    return this;
  }

  public DonationBackingFormBuilder withBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
    return this;
  }

  public DonationBackingFormBuilder withBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
    return this;
  }

  public DonationBackingFormBuilder withAdverseEvent(AdverseEventBackingForm adverseEventBackingForm) {
    this.adverseEventBackingForm = adverseEventBackingForm;
    return this;
  }

  public DonationBackingFormBuilder withDonationBatchNumber(String donationBatchNumber) {
    this.donationBatchNumber = donationBatchNumber;
    return this;
  }

  public DonationBackingFormBuilder withDonor(Donor donor) {
    this.donor = donor;
    return this;
  }

  public DonationBackingFormBuilder withDonation(Donation donation) {
    this.donation = donation;
    return this;
  }

  @Override
  public DonationBackingForm build() {
    DonationBackingForm donationBackingForm = new DonationBackingForm();
    if (donation != null) {
      donationBackingForm.setDonation(donation);
    }
    donationBackingForm.setDonorPulse(donorPulse);
    donationBackingForm.setHaemoglobinCount(haemoglobinCount);
    donationBackingForm.setHaemoglobinLevel(haemoglobinLevel);
    donationBackingForm.setBloodPressureSystolic(bloodPressureSystolic);
    donationBackingForm.setBloodPressureDiastolic(bloodPressureDiastolic);
    donationBackingForm.setDonorWeight(donorWeight);
    donationBackingForm.setNotes(notes);
    donationBackingForm.setPackType(packType);
    if (bleedStartTime != null) {
      donationBackingForm.setBleedStartTime(bleedStartTime);
    }
    if (bleedEndTime != null) {
      donationBackingForm.setBleedEndTime(bleedEndTime);
    }
    donationBackingForm.setAdverseEvent(adverseEventBackingForm);
    donationBackingForm.setDonor(donor);
    donationBackingForm.setDonationBatchNumber(donationBatchNumber);
    return donationBackingForm;
  }

  public static DonationBackingFormBuilder aDonationBackingForm() {
    return new DonationBackingFormBuilder();
  }

}
