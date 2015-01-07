package viewmodel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import model.bloodbagtype.BloodBagType;
import model.collectedsample.CollectedSample;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.product.Product;
import model.user.User;
import org.apache.commons.lang3.StringUtils;
import repository.bloodtesting.BloodTypingStatus;
import utils.CustomDateFormatter;

public class CollectedSampleViewModel {

  private CollectedSample collectedSample;

  public CollectedSampleViewModel() {
  }

  public CollectedSampleViewModel(CollectedSample collection) {
    this.collectedSample = collection;
  }

  public void copy(CollectedSample collection) {
    collection.copy(collection);
  }

  public String getCollectedOn() {
    if (collectedSample.getCollectedOn() == null)
      return "";
    return CustomDateFormatter.getDateString(collectedSample.getCollectedOn());
  }

  public boolean equals(Object obj) {
    return collectedSample.equals(obj);
  }

  public Long getId() {
    return collectedSample.getId();
  }

  public String getCollectionNumber() {
    return collectedSample.getCollectionNumber();
  }

  @JsonIgnore
  public Donor getDonor() {
    return collectedSample.getDonor();
  }

  public DonationType getDonationType() {
    return collectedSample.getDonationType();
  }

  public BloodBagType getPackType() {
    return collectedSample.getBloodBagType();
  }

  public String getNotes() {
    return collectedSample.getNotes();
  }

  public Boolean getIsDeleted() {
    return collectedSample.getIsDeleted();
  }

  public List<Product> getProducts() {
    return collectedSample.getProducts();
  }

  public int hashCode() {
    return collectedSample.hashCode();
  }

  public String getDonorNumber() {
   if (collectedSample.getDonor() == null)
     return "";
   return collectedSample.getDonor().getDonorNumber();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(collectedSample.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(collectedSample.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = collectedSample.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = collectedSample.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getTTIStatus() {
    if (collectedSample.getTTIStatus() == null)
      return "";
    return collectedSample.getTTIStatus().toString();
  }

  public String getCollectionBatchNumber() {
    if (collectedSample.getCollectionBatch() == null)
      return "";
    return collectedSample.getCollectionBatch().getBatchNumber();
  }

  public String getBloodTypingStatus() {
    if (collectedSample.getBloodTypingStatus() == null)
      return "";
    return collectedSample.getBloodTypingStatus().toString();
  }

  public String getBloodAbo() {
    if (collectedSample.getBloodAbo() == null)
      return "";
    return collectedSample.getBloodAbo().toString();
  }

  public String getBloodRh() {
    if (collectedSample.getBloodRh() == null)
      return "";
    return collectedSample.getBloodRh().toString();
  }

  public String getExtraBloodTypeInformation() {
    if (collectedSample.getExtraBloodTypeInformation() == null)
      return "";
    return collectedSample.getExtraBloodTypeInformation();
  }

  public String getBloodGroup() {
    String bloodTypingStatus = getBloodTypingStatus();
    if (StringUtils.isBlank(bloodTypingStatus) || !bloodTypingStatus.equals(BloodTypingStatus.COMPLETE.toString()))
      return "";
    else
      return getBloodAbo() + getBloodRh();
  }

  public BigDecimal getHaemoglobinCount() {
		return collectedSample.getHaemoglobinCount();
	}
	
  public void setHaemoglobinCount(BigDecimal haemoglobinCount) {
		collectedSample.setHaemoglobinCount(haemoglobinCount);
	}
  
  public BigDecimal getDonorWeight() {
		return collectedSample.getDonorWeight();
	}

  public void setDonorWeight(BigDecimal donorWeight) {
		collectedSample.setDonorWeight(donorWeight);
	}

  public Integer  getDonorPulse() {
		return collectedSample.getDonorPulse();
	}

  public void setDonorPulse(Integer donorPulse) {
		collectedSample.setDonorPulse(donorPulse);
  }

  public Integer getBloodPressureSystolic() {
    return  collectedSample.getBloodPressureSystolic();
  }

  public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
    collectedSample.setBloodPressureSystolic(bloodPressureSystolic);
  }

  public Integer getBloodPressureDiastolic() {
    return collectedSample.getBloodPressureDiastolic();
  }

  public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
    collectedSample.setBloodPressureDiastolic(bloodPressureDiastolic);
  }
    
  public String getBleedStartTime() {
    Date bleedStartTime = collectedSample.getBleedStartTime();
    if (bleedStartTime != null) {
        return CustomDateFormatter.getTimeString(bleedStartTime);
    }
    return "";
  }

  public String getBleedEndTime() {
    Date bleedEndTime = collectedSample.getBleedEndTime();
    if (bleedEndTime != null) {
        return CustomDateFormatter.getTimeString(bleedEndTime);
    }
    return "";
  }

  public  LocationViewModel getDonorPanel(){
      return new LocationViewModel(collectedSample.getDonorPanel());
  }

}
