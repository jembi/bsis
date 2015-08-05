package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.bloodbagtype.BloodBagType;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donation.HaemoglobinLevel;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.product.Product;
import model.location.Location;
import model.user.User;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import utils.CustomDateFormatter;

public class DonationBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  @JsonIgnore
  private Donation donation;

  private List<String> centers;
  private List<String> sites;
  private String donationDate;
  private String bleedStartTime;
  private String bleedEndTime;
  private String donorNumber;

  // setting this to false is required as the use parameters from batch
  // may be hidden by the user in which case we will get a null pointer
  // exception
  private Boolean useParametersFromBatch = false;

  public DonationBackingForm() {
    donation = new Donation();
  }

  public DonationBackingForm(Donation donation) {
    this.donation = donation;
  }

  public void copy(Donation donation) {
    donation.copy(donation);
  }

  public Donation getDonation() {
    return donation;
  }

  public List<String> getCenters() {
    return centers;
  }

  public List<String> getSites() {
    return sites;
  }

  public String getDonationDate() {
    if (donationDate != null)
      return donationDate;
    if (donation == null)
      return "";
    return CustomDateFormatter.getDateTimeString(donation.getDonationDate());
  }
  
  public String getBleedStartTime() {
    if (bleedStartTime != null)
      return bleedStartTime;
    if (donation == null)
      return "";
    return CustomDateFormatter.getDateTimeString(donation.getBleedStartTime());
  }
  
  public String getBleedEndTime() {
    if (bleedEndTime != null)
      return bleedEndTime;
    if (donation == null)
      return "";
    return CustomDateFormatter.getDateTimeString(donation.getBleedEndTime());
  }

  public String getDonationIdentificationNumber() {
    return donation.getDonationIdentificationNumber();
  }

  public void setDonationDate(String donationDate) {
    this.donationDate = donationDate;
    try {
      donation.setDonationDate(CustomDateFormatter.getDateFromString(donationDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      donation.setDonationDate(null);
    }
  }
  
  public void setBleedStartTime(String bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
    try {
      donation.setBleedStartTime(CustomDateFormatter.getDateTimeFromString(bleedStartTime));
    } catch (ParseException ex) {
      ex.printStackTrace();
      donation.setBleedStartTime(null);
    }
  }
  
  public void setBleedEndTime(String bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
    try {
      donation.setBleedEndTime(CustomDateFormatter.getDateTimeFromString(bleedEndTime));
    } catch (ParseException ex) {
      ex.printStackTrace();
      donation.setBleedEndTime(null);
    }
  }
	  
  public void setDonation(Donation donation) {
    this.donation = donation;
  }

  public void setCenters(List<String> centers) {
    this.centers = centers;
  }

  public void setSites(List<String> sites) {
    this.sites = sites;
  }

  public boolean equals(Object obj) {
    return donation.equals(obj);
  }

  public Long getId() {
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

  public String getPackType() {
    BloodBagType packType = donation.getBloodBagType();
    if (packType == null || packType.getId() == null)
      return null;
    else
      return packType.getId().toString();
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

  public void setId(Long id) {
    donation.setId(id);
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    donation.setDonationIdentificationNumber(donationIdentificationNumber);
  }

  public void setDonor(Donor donor) {
    donation.setDonor(donor);
  }
  
 
  public void setDonationType(DonationType donationType){
  	if (donationType == null){
  		donation.setDonationType(null);
  	}
  	else if (donationType.getId() == null){
  		donation.setDonationType(null);
  	}
  	else{
  		DonationType dt = new DonationType();
  		dt.setId(donationType.getId());
  		donation.setDonationType(dt);
  	}
  }

  public void setPackType(BloodBagType packType){
  	if (packType == null){
  		donation.setBloodBagType(null);
  	}
  	else if (packType.getId() == null){
  		donation.setBloodBagType(null);
  	}
  	else{
  		BloodBagType bt = new BloodBagType();
  		bt.setId(packType.getId());
  		donation.setBloodBagType(bt);
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

  public void generateDonationIdentificationNumber() {
    String uniqueDonationNumber;
    uniqueDonationNumber = "C-" +
                        RandomStringUtils.randomNumeric(ID_LENGTH).toUpperCase();
    donation.setDonationIdentificationNumber(uniqueDonationNumber);
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public String getDonationBatchNumber() {
    if (donation == null || donation.getDonationBatch() == null ||
        donation.getDonationBatch().getBatchNumber() == null
       )
      return "";
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
  public String getDonorIdHidden() {
    if (donation == null)
      return null;
    Donor donor = donation.getDonor();
    if (donor == null || donor.getId() == null)
      return null;
    return donor.getId().toString();
  }

  @JsonIgnore
  public void setDonorIdHidden(String donorId) {
    if (donorId == null || donorId=="") {
      donation.setDonor(null);
    }
    else {
      
      try {
        Donor d = new Donor();
        d.setId(Long.parseLong(donorId));
        donation.setDonor(d);
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        donation.setDonor(null);
      }
    }
  }

  @JsonIgnore
  public DonationBatch getDonationBatch() {
    return donation.getDonationBatch();
  }

  public void setDonationBatch(DonationBatch donationBatch) {
    donation.setDonationBatch(donationBatch);
  }

  public Boolean getUseParametersFromBatch() {
    return useParametersFromBatch;
  }

  public void setUseParametersFromBatch(Boolean useParametersFromBatch) {
    this.useParametersFromBatch = useParametersFromBatch;
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
    
    public String getExtraBloodTypeInformation() {
        return donation.getExtraBloodTypeInformation();
    }

    public void setExtraBloodTypeInformation(String extraBloodTypeInformation) {
        donation.setExtraBloodTypeInformation(extraBloodTypeInformation);
    }
    
    @JsonIgnore
    public String getTTIStatus() {
	    if(donation.getTTIStatus()!=null)
	    	return donation.getTTIStatus().toString();
	    else
	        return "";
	}
    
    @JsonIgnore
    public String getBloodTypingStatus() {
        if(donation.getBloodTypingStatus()!=null)
	    	return donation.getBloodTypingStatus().toString();
	    else
	        return "";
    }
    
    @JsonIgnore
    public String getBloodTypingMatchStatus() {
        if(donation.getBloodTypingMatchStatus()!=null)
	    	return donation.getBloodTypingMatchStatus().toString();
	    else
	        return "";
    }
    
    @JsonIgnore
    public List<Product> getProducts() {
        return donation.getProducts();
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
    
    public void setDonorPanel(Location donorPanel){
        if(donorPanel == null || donorPanel.getId() == null){
            donation.setDonorPanel(null);
        }else{
            donation.setDonorPanel(donorPanel);
        }
            
    }
    
    public void setBloodBagType(BloodBagType bloodBagType) {
        donation.setBloodBagType(bloodBagType);
    }

}