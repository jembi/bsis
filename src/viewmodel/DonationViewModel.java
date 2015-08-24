package viewmodel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import model.bloodbagtype.BloodBagType;
import model.donation.Donation;
import model.donation.HaemoglobinLevel;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.product.Product;
import model.user.User;
import org.apache.commons.lang3.StringUtils;
import repository.bloodtesting.BloodTypingStatus;
import utils.CustomDateFormatter;

public class DonationViewModel {

  private Donation donation;
  private boolean includeCounselling;

  public DonationViewModel() {
  }

  public DonationViewModel(Donation donation) {
    this(donation, false);
  }

  /**
   * @param donation The donation.
   * @param includeCounselling Whether or not to include the post donation counselling. This should be based on the
   * VIEW_POST_DONATION_COUNSELLING permission.
   */
  public DonationViewModel(Donation donation, boolean includeCounselling) {
    this.donation = donation;
    this.includeCounselling = includeCounselling;
  }

  public void copy(Donation donation) {
    donation.copy(donation);
  }

  public String getDonationDate() {
    if (donation.getDonationDate() == null)
      return "";
    return CustomDateFormatter.getDateString(donation.getDonationDate());
  }

  public boolean equals(Object obj) {
    return donation.equals(obj);
  }

  public Long getId() {
    return donation.getId();
  }

  public String getDonationIdentificationNumber() {
    return donation.getDonationIdentificationNumber();
  }

  @JsonIgnore
  public Donor getDonor() {
    return donation.getDonor();
  }

  public DonationType getDonationType() {
    return donation.getDonationType();
  }

  public PackTypeViewModel getPackType() {
      if (donation.getBloodBagType() == null) {
          return null;
      }
      return new PackTypeViewModel(donation.getBloodBagType());
  }

  public String getNotes() {
    return donation.getNotes();
  }

  public Boolean getIsDeleted() {
    return donation.getIsDeleted();
  }

  public List<Product> getProducts() {
    return donation.getProducts();
  }

  public int hashCode() {
    return donation.hashCode();
  }

  public String getDonorNumber() {
   if (donation.getDonor() == null)
     return "";
   return donation.getDonor().getDonorNumber();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(donation.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(donation.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = donation.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = donation.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getTTIStatus() {
    if (donation.getTTIStatus() == null)
      return "";
    return donation.getTTIStatus().toString();
  }

  public String getDonationBatchNumber() {
    if (donation.getDonationBatch() == null)
      return "";
    return donation.getDonationBatch().getBatchNumber();
  }

  public String getBloodTypingStatus() {
    if (donation.getBloodTypingStatus() == null)
      return "";
    return donation.getBloodTypingStatus().toString();
  }
  
  public String getBloodTypingMatchStatus() {
    if (donation.getBloodTypingMatchStatus() == null)
      return "";
    return donation.getBloodTypingMatchStatus().toString();
  }

  public String getBloodAbo() {
    if (donation.getBloodAbo() == null)
      return "";
    return donation.getBloodAbo().toString();
  }

  public String getBloodRh() {
    if (donation.getBloodRh() == null)
      return "";
    return donation.getBloodRh().toString();
  }

  public String getExtraBloodTypeInformation() {
    if (donation.getExtraBloodTypeInformation() == null)
      return "";
    return donation.getExtraBloodTypeInformation();
  }

  public String getBloodGroup() {
    String bloodTypingStatus = getBloodTypingStatus();
    if (StringUtils.isBlank(bloodTypingStatus) || !bloodTypingStatus.equals(BloodTypingStatus.COMPLETE.toString()))
      return "";
    else
      return getBloodAbo() + getBloodRh();
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

  public BigDecimal getDonorWeight() {
		return donation.getDonorWeight();
	}

  public void setDonorWeight(BigDecimal donorWeight) {
		donation.setDonorWeight(donorWeight);
	}

  public Integer  getDonorPulse() {
		return donation.getDonorPulse();
	}

  public void setDonorPulse(Integer donorPulse) {
		donation.setDonorPulse(donorPulse);
  }

  public Integer getBloodPressureSystolic() {
    return  donation.getBloodPressureSystolic();
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
    
  public String getBleedStartTime() {
    Date bleedStartTime = donation.getBleedStartTime();
    if (bleedStartTime != null) {
        return CustomDateFormatter.getTimeString(bleedStartTime);
    }
    return "";
  }

  public String getBleedEndTime() {
    Date bleedEndTime = donation.getBleedEndTime();
    if (bleedEndTime != null) {
        return CustomDateFormatter.getTimeString(bleedEndTime);
    }
    return "";
  }

  public  LocationViewModel getDonorPanel(){
      return new LocationViewModel(donation.getDonorPanel());
  }

  public PackTypeViewModel getBloodBagType() {
      if (donation.getBloodBagType() == null) {
          return null;
      }
      return new PackTypeViewModel(donation.getBloodBagType());
  }
  
  public PostDonationCounsellingViewModel getPostDonationCounselling() {
      if (includeCounselling && donation.getPostDonationCounselling() != null) {
          return new PostDonationCounsellingViewModel(donation.getPostDonationCounselling());
      }
      return null;
  }

}
