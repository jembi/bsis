package org.jembi.bsis.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;

public class DonationExportDTO extends ModificationTrackerExportDTO {

  private String donorNumber;
  private String donationIdentificationNumber;
  private String packType;
  private Date donationDate;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private TTIStatus ttiStatus;
  private Date bleedStartTime;
  private Date bleedEndTime;
  private BigDecimal donorWeight;
  private Integer bloodPressureSystolic;
  private Integer bloodPressureDiastolic;
  private Integer donorPulse;
  private BigDecimal haemoglobinCount;
  private HaemoglobinLevel haemoglobinLevel;
  private AdverseEvent adverseEvent;
  private String bloodAbo;
  private String bloodRh;
  private boolean released;
  private boolean ineligbleDonor;
  private String notes;

  public DonationExportDTO(String donorNumber, String donationIdentificationNumber, Date createdDate, String createdBy,
      Date lastUpdated, String lastUpdatedBy, String packType, Date donationDate, BloodTypingStatus bloodTypingStatus,
      BloodTypingMatchStatus bloodTypingMatchStatus, TTIStatus ttiStatus, Date bleedStartTime, Date bleedEndTime,
      BigDecimal donorWeight, Integer bloodPressureSystolic, Integer bloodPressureDiastolic, Integer donorPulse,
      BigDecimal haemoglobinCount, HaemoglobinLevel haemoglobinLevel, AdverseEvent adverseEvent, String bloodAbo,
      String bloodRh, boolean released, boolean ineligbleDonor, String notes) {
    this.donorNumber = donorNumber;
    this.donationIdentificationNumber = donationIdentificationNumber;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastUpdated = lastUpdated;
    this.lastUpdatedBy = lastUpdatedBy;
    this.packType = packType;
    this.donationDate = donationDate;
    this.bloodTypingStatus = bloodTypingStatus;
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
    this.ttiStatus = ttiStatus;
    this.bleedStartTime = bleedStartTime;
    this.bleedEndTime = bleedEndTime;
    this.donorWeight = donorWeight;
    this.bloodPressureSystolic = bloodPressureSystolic;
    this.bloodPressureDiastolic = bloodPressureDiastolic;
    this.donorPulse = donorPulse;
    this.haemoglobinCount = haemoglobinCount;
    this.haemoglobinLevel = haemoglobinLevel;
    this.adverseEvent = adverseEvent;
    this.bloodAbo = bloodAbo;
    this.bloodRh = bloodRh;
    this.released = released;
    this.ineligbleDonor = ineligbleDonor;
    this.notes = notes;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public String getPackType() {
    return packType;
  }

  public void setPackType(String packType) {
    this.packType = packType;
  }

  public Date getDonationDate() {
    return donationDate;
  }

  public void setDonationDate(Date donationDate) {
    this.donationDate = donationDate;
  }

  public BloodTypingStatus getBloodTypingStatus() {
    return bloodTypingStatus;
  }

  public void setBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
  }

  public BloodTypingMatchStatus getBloodTypingMatchStatus() {
    return bloodTypingMatchStatus;
  }

  public void setBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
  }

  public TTIStatus getTtiStatus() {
    return ttiStatus;
  }

  public void setTtiStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
  }

  public Date getBleedStartTime() {
    return bleedStartTime;
  }

  public void setBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
  }

  public Date getBleedEndTime() {
    return bleedEndTime;
  }

  public void setBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
  }

  public BigDecimal getDonorWeight() {
    return donorWeight;
  }

  public void setDonorWeight(BigDecimal donorWeight) {
    this.donorWeight = donorWeight;
  }

  public Integer getBloodPressureSystolic() {
    return bloodPressureSystolic;
  }

  public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
    this.bloodPressureSystolic = bloodPressureSystolic;
  }

  public Integer getBloodPressureDiastolic() {
    return bloodPressureDiastolic;
  }

  public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
    this.bloodPressureDiastolic = bloodPressureDiastolic;
  }

  public Integer getDonorPulse() {
    return donorPulse;
  }

  public void setDonorPulse(Integer donorPulse) {
    this.donorPulse = donorPulse;
  }

  public BigDecimal getHaemoglobinCount() {
    return haemoglobinCount;
  }

  public void setHaemoglobinCount(BigDecimal haemoglobinCount) {
    this.haemoglobinCount = haemoglobinCount;
  }

  public HaemoglobinLevel getHaemoglobinLevel() {
    return haemoglobinLevel;
  }

  public void setHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
    this.haemoglobinLevel = haemoglobinLevel;
  }

  public AdverseEvent getAdverseEvent() {
    return adverseEvent;
  }

  public void setAdverseEvent(AdverseEvent adverseEvent) {
    this.adverseEvent = adverseEvent;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public boolean isReleased() {
    return released;
  }

  public void setReleased(boolean released) {
    this.released = released;
  }

  public boolean isIneligbleDonor() {
    return ineligbleDonor;
  }

  public void setIneligbleDonor(boolean ineligbleDonor) {
    this.ineligbleDonor = ineligbleDonor;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
