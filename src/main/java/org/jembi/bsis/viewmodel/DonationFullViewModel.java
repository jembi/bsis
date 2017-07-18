package org.jembi.bsis.viewmodel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.utils.CustomDateFormatter;

public class DonationFullViewModel extends BaseViewModel<UUID> {

  private Date donationDate;
  private String donationIdentificationNumber;
  private DonationTypeViewModel donationType;
  private PackTypeFullViewModel packType;
  private String notes;
  private String donorNumber;
  private Date lastUpdated;
  private Date createdDate;
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
  private Map<String, Boolean> permissions;
  private AdverseEventViewModel adverseEvent;

  public DonationFullViewModel() {
  }
  
  public void setDonationDate(Date donationDate) {
    this.donationDate = donationDate;
  }
  
  public String getDonationDate() {
    return CustomDateFormatter.getDateTimeString(donationDate);
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }
  
  public void setDonationType(DonationTypeViewModel donationType) {
    this.donationType = donationType;
  }

  public DonationTypeViewModel getDonationType() {
    return donationType;
  }

  public void setPackType(PackTypeFullViewModel packType) {
    this.packType = packType;
  }
  
  public PackTypeFullViewModel getPackType() {
    return packType;
  }
  
  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(lastUpdated);
  }
  
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(createdDate);
  }
  
  public void setTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
  }

  public String getTTIStatus() {
    if (ttiStatus == null) {
      return "";
    }
    return ttiStatus.toString();
  }
  
  public void setBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
  }

  public String getBloodTypingStatus() {
    if (bloodTypingStatus == null) {
      return "";
    }
    return bloodTypingStatus.toString();
  }
  
  public void setBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
  }

  public String getBloodTypingMatchStatus() {
    if (bloodTypingMatchStatus == null) {
      return "";
    }
    return bloodTypingMatchStatus.toString();
  }
  
  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodAbo() {
    if (bloodAbo == null) {
      return "";
    }
    return bloodAbo;
  }
  
  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public String getBloodRh() {
    if (bloodRh == null) {
      return "";
    }
    return bloodRh;
  }

  public String getBloodGroup() {
    String bloodTypingStatus = getBloodTypingStatus();
    if (StringUtils.isBlank(bloodTypingStatus) || !bloodTypingStatus.equals(BloodTypingStatus.COMPLETE.toString()))
      return "";
    else
      return getBloodAbo() + getBloodRh();
  }
  
  public void setHaemoglobinCount(BigDecimal haemoglobinCount) {
    this.haemoglobinCount = haemoglobinCount;
  }

  public BigDecimal getHaemoglobinCount() {
    return haemoglobinCount;
  }

  public BigDecimal getDonorWeight() {
    return donorWeight;
  }

  public void setDonorWeight(BigDecimal donorWeight) {
    this.donorWeight = donorWeight;
  }

  public Integer getDonorPulse() {
    return donorPulse;
  }

  public void setDonorPulse(Integer donorPulse) {
    this.donorPulse = donorPulse;
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
  
  public void setBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
  }

  public String getBleedStartTime() {
    return CustomDateFormatter.getTimeString(bleedStartTime);
  }

  public void setBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
  }
  
  public String getBleedEndTime() {
    return CustomDateFormatter.getTimeString(bleedEndTime);
  }
  
  public void setVenue(LocationViewModel venue) {
    this.venue = venue;
  }

  public LocationViewModel getVenue() {
    return venue;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public String getDonationBatchNumber() {
    return donationBatchNumber;
  }

  public void setDonationBatchNumber(String donationBatchNumber) {
    this.donationBatchNumber = donationBatchNumber;
  }

  public HaemoglobinLevel getHaemoglobinLevel() {
    return haemoglobinLevel;
  }

  public void setHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
    this.haemoglobinLevel = haemoglobinLevel;
  }

  public boolean isReleased() {
    return released;
  }

  public void setReleased(boolean released) {
    this.released = released;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public AdverseEventViewModel getAdverseEvent() {
    return adverseEvent;
  }

  public void setAdverseEvent(AdverseEventViewModel adverseEvent) {
    this.adverseEvent = adverseEvent;
  } 
}
