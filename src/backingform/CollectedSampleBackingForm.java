package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import model.bloodbagtype.BloodBagType;
import model.collectedsample.CollectedSample;
import model.collectionbatch.CollectionBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.product.Product;
import model.location.Location;
import model.user.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import utils.CustomDateFormatter;

public class CollectedSampleBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  @JsonIgnore
  private CollectedSample collectedSample;

  private List<String> centers;
  private List<String> sites;
  private String collectedOn;
  private String bleedStartTime;
  private String bleedEndTime;
  private String donorNumber;

  // setting this to false is required as the use parameters from batch
  // may be hidden by the user in which case we will get a null pointer
  // exception
  private Boolean useParametersFromBatch = false;

  public CollectedSampleBackingForm() {
    collectedSample = new CollectedSample();
  }

  public CollectedSampleBackingForm(CollectedSample collection) {
    this.collectedSample = collection;
  }

  public void copy(CollectedSample collection) {
    collection.copy(collection);
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }

  public List<String> getCenters() {
    return centers;
  }

  public List<String> getSites() {
    return sites;
  }

  public String getCollectedOn() {
    if (collectedOn != null)
      return collectedOn;
    if (collectedSample == null)
      return "";
    return CustomDateFormatter.getDateTimeString(collectedSample.getCollectedOn());
  }
  
  public String getBleedStartTime() {
    if (bleedStartTime != null)
      return bleedStartTime;
    if (collectedSample == null)
      return "";
    return CustomDateFormatter.getDateTimeString(collectedSample.getBleedStartTime());
  }
  
  public String getBleedEndTime() {
    if (bleedEndTime != null)
      return bleedEndTime;
    if (collectedSample == null)
      return "";
    return CustomDateFormatter.getDateTimeString(collectedSample.getBleedEndTime());
  }

  public String getCollectionNumber() {
    return collectedSample.getCollectionNumber();
  }

  public void setCollectedOn(String collectedOn) {
    this.collectedOn = collectedOn;
    try {
      collectedSample.setCollectedOn(CustomDateFormatter.getDateFromString(collectedOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      collectedSample.setCollectedOn(null);
    }
  }
  
  public void setBleedStartTime(String bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
    try {
      collectedSample.setBleedStartTime(CustomDateFormatter.getTimeFromString(bleedStartTime));
    } catch (ParseException ex) {
      ex.printStackTrace();
      collectedSample.setBleedStartTime(null);
    }
  }
  
  public void setBleedEndTime(String bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
    try {
      collectedSample.setBleedEndTime(CustomDateFormatter.getTimeFromString(bleedEndTime));
    } catch (ParseException ex) {
      ex.printStackTrace();
      collectedSample.setBleedEndTime(null);
    }
  }
	  
  public void setCollection(CollectedSample collection) {
    this.collectedSample = collection;
  }

  public void setCenters(List<String> centers) {
    this.centers = centers;
  }

  public void setSites(List<String> sites) {
    this.sites = sites;
  }

  public boolean equals(Object obj) {
    return collectedSample.equals(obj);
  }

  public Long getId() {
    return collectedSample.getId();
  }

  public Donor getDonor() {
    return collectedSample.getDonor();
  }


  public String getDonationType() {
    DonationType donationType = collectedSample.getDonationType();
    if (donationType == null || donationType.getId() == null)
      return null;
    else
      return donationType.getId().toString();
  }

  public String getPackType() {
    BloodBagType packType = collectedSample.getBloodBagType();
    if (packType == null || packType.getId() == null)
      return null;
    else
      return packType.getId().toString();
  }
  
  @JsonIgnore
  public Date getLastUpdated() {
    return collectedSample.getLastUpdated();
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return collectedSample.getCreatedDate();
  }

  @JsonIgnore
  public User getCreatedBy() {
    return collectedSample.getCreatedBy();
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return collectedSample.getLastUpdatedBy();
  }

  public String getNotes() {
    return collectedSample.getNotes();
  }

  public Boolean getIsDeleted() {
    return collectedSample.getIsDeleted();
  }

  public int hashCode() {
    return collectedSample.hashCode();
  }

  public void setId(Long id) {
    collectedSample.setId(id);
  }

  public void setCollectionNumber(String collectionNumber) {
    collectedSample.setCollectionNumber(collectionNumber);
  }

  public void setDonor(Donor donor) {
    collectedSample.setDonor(donor);
  }
  
 
  public void setDonationType(DonationType donationType){
  	if (donationType == null){
  		collectedSample.setDonationType(null);
  	}
  	else if (donationType.getId() == null){
  		collectedSample.setDonationType(null);
  	}
  	else{
  		DonationType dt = new DonationType();
  		dt.setId(donationType.getId());
  		collectedSample.setDonationType(dt);
  	}
  }

  public void setPackType(BloodBagType packType){
  	if (packType == null){
  		collectedSample.setBloodBagType(null);
  	}
  	else if (packType.getId() == null){
  		collectedSample.setBloodBagType(null);
  	}
  	else{
  		BloodBagType bt = new BloodBagType();
  		bt.setId(packType.getId());
  		collectedSample.setBloodBagType(bt);
  	}
  }
  
  public void setLastUpdated(Date lastUpdated) {
    collectedSample.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    collectedSample.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    collectedSample.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    collectedSample.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setNotes(String notes) {
    collectedSample.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    collectedSample.setIsDeleted(isDeleted);
  }

  public void generateCollectionNumber() {
    String uniqueCollectedSampleNumber;
    uniqueCollectedSampleNumber = "C-" +
                        RandomStringUtils.randomNumeric(ID_LENGTH).toUpperCase();
    collectedSample.setCollectionNumber(uniqueCollectedSampleNumber);
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public String getCollectionBatchNumber() {
    if (collectedSample == null || collectedSample.getCollectionBatch() == null ||
        collectedSample.getCollectionBatch().getBatchNumber() == null
       )
      return "";
    return collectedSample.getCollectionBatch().getBatchNumber();
  }

  public void setDonorNumber(String donorNumber) {
	this.donorNumber = donorNumber;
  }
  
  public void setCollectionBatchNumber(String collectionBatchNumber) {
    if (StringUtils.isNotBlank(collectionBatchNumber)) {
      CollectionBatch collectionBatch = new CollectionBatch();
      collectionBatch.setBatchNumber(collectionBatchNumber);
      collectedSample.setCollectionBatch(collectionBatch);
    }
  }

  @JsonIgnore
  public String getDonorIdHidden() {
    if (collectedSample == null)
      return null;
    Donor donor = collectedSample.getDonor();
    if (donor == null || donor.getId() == null)
      return null;
    return donor.getId().toString();
  }

  @JsonIgnore
  public void setDonorIdHidden(String donorId) {
    if (donorId == null || donorId=="") {
      collectedSample.setDonor(null);
    }
    else {
      
      try {
        Donor d = new Donor();
        d.setId(Long.parseLong(donorId));
        collectedSample.setDonor(d);
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        collectedSample.setDonor(null);
      }
    }
  }

  @JsonIgnore
  public CollectionBatch getCollectionBatch() {
    return collectedSample.getCollectionBatch();
  }

  public void setCollectionBatch(CollectionBatch collectionBatch) {
    collectedSample.setCollectionBatch(collectionBatch);
  }

  public Boolean getUseParametersFromBatch() {
    return useParametersFromBatch;
  }

  public void setUseParametersFromBatch(Boolean useParametersFromBatch) {
    this.useParametersFromBatch = useParametersFromBatch;
  }

    public BigDecimal getDonorWeight() {
        return collectedSample.getDonorWeight();
    }

    public void setDonorWeight(BigDecimal donorWeight) {
        collectedSample.setDonorWeight(donorWeight);
    }

    public BigDecimal getHaemoglobinCount() {
        return collectedSample.getHaemoglobinCount();
    }

    public void setHaemoglobinCount(BigDecimal haemoglobinCount) {
        collectedSample.setHaemoglobinCount(haemoglobinCount);
    }

    public Integer getDonorPulse() {
        return collectedSample.getDonorPulse();
    }
    
    public void setDonorPulse(Integer donorPulse) {
        collectedSample.setDonorPulse(donorPulse);
    }
    
    public Integer getBloodPressureSystolic() {
        return collectedSample.getBloodPressureSystolic();
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
    
    public String getExtraBloodTypeInformation() {
        return collectedSample.getExtraBloodTypeInformation();
    }

    public void setExtraBloodTypeInformation(String extraBloodTypeInformation) {
        collectedSample.setExtraBloodTypeInformation(extraBloodTypeInformation);
    }
    
    @JsonIgnore
    public String getTTIStatus() {
	    if(collectedSample.getTTIStatus()!=null)
	    	return collectedSample.getTTIStatus().toString();
	    else
	        return "";
	}
    
    @JsonIgnore
    public String getBloodTypingStatus() {
        if(collectedSample.getBloodTypingStatus()!=null)
	    	return collectedSample.getBloodTypingStatus().toString();
	    else
	        return "";
    }
    
    @JsonIgnore
    public List<Product> getProducts() {
        return collectedSample.getProducts();
    }
    
    @JsonIgnore
    public String getBloodGroup() {
	    if (StringUtils.isBlank(collectedSample.getBloodAbo()) || StringUtils.isBlank(collectedSample.getBloodRh()))
	      return "";
	    else
	      return collectedSample.getBloodAbo() + collectedSample.getBloodRh();
	}
  
    public String getBloodAbo() {
        if (StringUtils.isBlank(collectedSample.getBloodAbo()) || collectedSample.getBloodAbo() == null) {
            return "";
        } else {
            return collectedSample.getBloodAbo();
        }
    }

    public void setBloodAbo(String bloodAbo) {
        if (StringUtils.isBlank(bloodAbo)) {
        	collectedSample.setBloodAbo(null);
        } else {
        	collectedSample.setBloodAbo(bloodAbo);
        }
    }

    public String getBloodRh() {
        if (StringUtils.isBlank(collectedSample.getBloodRh()) || collectedSample.getBloodRh() == null) {
            return "";
        } else {
            return collectedSample.getBloodRh();
        }
    }

    public void setBloodRh(String bloodRh) {
        if (StringUtils.isBlank(bloodRh)) {
        	collectedSample.setBloodRh(null);
        } else {
        	collectedSample.setBloodRh(bloodRh);
        }
    }
    
    public void setDonorPanel(Location donorPanel){
        if(donorPanel == null || donorPanel.getId() == null){
            collectedSample.setDonorPanel(null);
        }else{
            collectedSample.setDonorPanel(donorPanel);
        }
            
    }

}