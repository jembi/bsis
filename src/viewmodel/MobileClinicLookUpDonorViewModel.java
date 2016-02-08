package viewmodel;

import model.donor.MobileClinicDonor;
import model.util.Gender;

import org.apache.commons.lang3.StringUtils;

import utils.CustomDateFormatter;

public class MobileClinicLookUpDonorViewModel {

  private MobileClinicDonor mobileClinicDonor;

  private Boolean eligibility;

  public MobileClinicLookUpDonorViewModel() {
    mobileClinicDonor = new MobileClinicDonor();
  }

  public MobileClinicLookUpDonorViewModel(MobileClinicDonor mobileClinicDonor) {
    this.mobileClinicDonor = mobileClinicDonor;
  }

  public long getId() {
    return mobileClinicDonor.getId();
  }

  public void setId(long id) {
    mobileClinicDonor.setId(id);
  }

  public String getDonorNumber() {
    return mobileClinicDonor.getDonorNumber();
  }

  public String getFirstName() {
    return mobileClinicDonor.getFirstName();
  }

  public String getLastName() {
    return mobileClinicDonor.getLastName();
  }

  public String getGender() {
    Gender gender = mobileClinicDonor.getGender();
    return gender == null ? "" : gender.name();
  }

  public String getBirthDate() {
    return CustomDateFormatter.getDateString(mobileClinicDonor.getBirthDate());
  }

  public String getBloodType() {
    String bloodAbo = mobileClinicDonor.getBloodAbo();
    String bloodRh = mobileClinicDonor.getBloodRh();
    if (StringUtils.isBlank(bloodAbo) || StringUtils.isBlank(bloodRh))
      return "";
    else
      return bloodAbo + bloodRh;
  }

  public Boolean getEligibility() {
    return eligibility;
  }

  public void setEligibility(Boolean eligibility) {
    this.eligibility = eligibility;
  }
}