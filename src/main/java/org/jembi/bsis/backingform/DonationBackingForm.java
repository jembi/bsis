package org.jembi.bsis.backingform;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.utils.DateTimeSerialiser;
import org.jembi.bsis.utils.PackTypeSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DonationBackingForm {

  @NotNull
  @Valid
  @JsonIgnore
  private Donation donation;

  private String donorNumber;
  private AdverseEventBackingForm adverseEventBackingForm;

  public DonationBackingForm() {
    donation = new Donation();
  }

  public DonationBackingForm(Donation donation) {
    this.donation = donation;
  }

  public Donation getDonation() {
    return donation;
  }

  public Date getDonationDate() {
    return donation.getDonationDate();
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getBleedStartTime() {
    return donation.getBleedStartTime();
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getBleedEndTime() {
    return donation.getBleedEndTime();
  }

  public String getDonationIdentificationNumber() {
    return donation.getDonationIdentificationNumber();
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setDonationDate(Date donationDate) {
    donation.setDonationDate(donationDate);
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setBleedStartTime(Date bleedStartTime) {
    donation.setBleedStartTime(bleedStartTime);
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setBleedEndTime(Date bleedEndTime) {
    donation.setBleedEndTime(bleedEndTime);
  }

  public void setDonation(Donation donation) {
    this.donation = donation;
  }

  public boolean equals(Object obj) {
    return donation.equals(obj);
  }

  public UUID getId() {
    return donation.getId();
  }

  public Donor getDonor() {
    return donation.getDonor();
  }

  public String getDonationType() {
    DonationType donationType = donation.getDonationType();
    if (donationType == null || donationType.getId() == null)
      return null;
    else
      return donationType.getId().toString();
  }

  @JsonSerialize(using = PackTypeSerializer.class)
  public PackType getPackType() {
    return donation.getPackType();
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return donation.getLastUpdated();
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return donation.getCreatedDate();
  }

  @JsonIgnore
  public User getCreatedBy() {
    return donation.getCreatedBy();
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return donation.getLastUpdatedBy();
  }

  public String getNotes() {
    return donation.getNotes();
  }

  public Boolean getIsDeleted() {
    return donation.getIsDeleted();
  }

  public int hashCode() {
    return donation.hashCode();
  }

  public void setId(UUID id) {
    donation.setId(id);
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    donation.setDonationIdentificationNumber(donationIdentificationNumber);
  }

  public void setDonor(Donor donor) {
    donation.setDonor(donor);
  }

  public void setDonationType(DonationTypeBackingForm donationType) {
    if (donationType == null) {
      donation.setDonationType(null);
    } else if (donationType.getId() == null) {
      donation.setDonationType(null);
    } else {
      DonationType dt = new DonationType();
      dt.setId(donationType.getId());
      donation.setDonationType(dt);
    }
  }

  public void setPackType(PackType packType) {
    if (packType == null) {
      donation.setPackType(null);
    } else if (packType.getId() == null) {
      donation.setPackType(null);
    } else {
      PackType bt = new PackType();
      bt.setId(packType.getId());
      bt.setPackType(packType.getPackType());
      donation.setPackType(bt);
    }
  }

  public void setLastUpdated(Date lastUpdated) {
    donation.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    donation.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    donation.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    donation.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setNotes(String notes) {
    donation.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    donation.setIsDeleted(isDeleted);
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public String getDonationBatchNumber() {
    if (donation == null || donation.getDonationBatch() == null ||
        donation.getDonationBatch().getBatchNumber() == null) {
      return "";
    }
    return donation.getDonationBatch().getBatchNumber();
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public void setDonationBatchNumber(String donationBatchNumber) {
    if (StringUtils.isNotBlank(donationBatchNumber)) {
      DonationBatch donationBatch = new DonationBatch();
      donationBatch.setBatchNumber(donationBatchNumber);
      donation.setDonationBatch(donationBatch);
    }
  }

  @JsonIgnore
  public DonationBatch getDonationBatch() {
    return donation.getDonationBatch();
  }

  public void setDonationBatch(DonationBatch donationBatch) {
    donation.setDonationBatch(donationBatch);
  }

  public BigDecimal getDonorWeight() {
    return donation.getDonorWeight();
  }

  public void setDonorWeight(BigDecimal donorWeight) {
    donation.setDonorWeight(donorWeight);
  }

  public BigDecimal getHaemoglobinCount() {
    return donation.getHaemoglobinCount();
  }

  public void setHaemoglobinCount(BigDecimal haemoglobinCount) {
    donation.setHaemoglobinCount(haemoglobinCount);
  }

  public HaemoglobinLevel getHaemoglobinLevel() {
    return donation.getHaemoglobinLevel();
  }

  public void setHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
    donation.setHaemoglobinLevel(haemoglobinLevel);
  }

  public Integer getDonorPulse() {
    return donation.getDonorPulse();
  }

  public void setDonorPulse(Integer donorPulse) {
    donation.setDonorPulse(donorPulse);
  }

  public Integer getBloodPressureSystolic() {
    return donation.getBloodPressureSystolic();
  }

  public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
    donation.setBloodPressureSystolic(bloodPressureSystolic);
  }

  public Integer getBloodPressureDiastolic() {
    return donation.getBloodPressureDiastolic();
  }

  public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
    donation.setBloodPressureDiastolic(bloodPressureDiastolic);
  }

  @JsonIgnore
  public String getTTIStatus() {
    if (donation.getTTIStatus() != null)
      return donation.getTTIStatus().toString();
    else
      return "";
  }

  @JsonIgnore
  public String getBloodTypingStatus() {
    if (donation.getBloodTypingStatus() != null)
      return donation.getBloodTypingStatus().toString();
    else
      return "";
  }

  @JsonIgnore
  public String getBloodTypingMatchStatus() {
    if (donation.getBloodTypingMatchStatus() != null)
      return donation.getBloodTypingMatchStatus().toString();
    else
      return "";
  }

  @JsonIgnore
  public List<Component> getComponents() {
    return donation.getComponents();
  }

  @JsonIgnore
  public String getBloodGroup() {
    if (StringUtils.isBlank(donation.getBloodAbo()) || StringUtils.isBlank(donation.getBloodRh()))
      return "";
    else
      return donation.getBloodAbo() + donation.getBloodRh();
  }

  public String getBloodAbo() {
    if (StringUtils.isBlank(donation.getBloodAbo()) || donation.getBloodAbo() == null) {
      return "";
    } else {
      return donation.getBloodAbo();
    }
  }

  public void setBloodAbo(String bloodAbo) {
    if (StringUtils.isBlank(bloodAbo)) {
      donation.setBloodAbo(null);
    } else {
      donation.setBloodAbo(bloodAbo);
    }
  }

  public String getBloodRh() {
    if (StringUtils.isBlank(donation.getBloodRh()) || donation.getBloodRh() == null) {
      return "";
    } else {
      return donation.getBloodRh();
    }
  }

  public void setBloodRh(String bloodRh) {
    if (StringUtils.isBlank(bloodRh)) {
      donation.setBloodRh(null);
    } else {
      donation.setBloodRh(bloodRh);
    }
  }

  public void setVenue(LocationBackingForm venue) {
    if (venue == null || venue.getId() == null) {
      donation.setVenue(null);
      } else {
      Location location = new Location();
      location.setId(venue.getId());
      donation.setVenue(location);
    }
  }

  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore
  }

  public void setAdverseEvent(AdverseEventBackingForm adverseEventBackingForm) {
    this.adverseEventBackingForm = adverseEventBackingForm;
  }

  public AdverseEventBackingForm getAdverseEvent() {
    return adverseEventBackingForm;
  }

  public boolean isReleased() {
    return donation.isReleased();
  }

  public void setReleased(boolean released) {
    donation.setReleased(released);
  }
}