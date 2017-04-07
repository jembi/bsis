package org.jembi.bsis.viewmodel;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.utils.CustomDateFormatter;

public class MobileClinicLookUpDonorViewModel {

  private MobileClinicDonorDTO mobileClinicDonorDTO;

  private Boolean eligibility;

  public MobileClinicLookUpDonorViewModel() {
    mobileClinicDonorDTO = new MobileClinicDonorDTO();
  }

  public MobileClinicLookUpDonorViewModel(MobileClinicDonorDTO mobileClinicDonorDTO) {
    this.mobileClinicDonorDTO = mobileClinicDonorDTO;
  }

  public UUID getId() {
    return mobileClinicDonorDTO.getId();
  }

  public void setId(UUID id) {
    mobileClinicDonorDTO.setId(id);
  }

  public String getDonorNumber() {
    return mobileClinicDonorDTO.getDonorNumber();
  }

  public String getFirstName() {
    return mobileClinicDonorDTO.getFirstName();
  }

  public String getLastName() {
    return mobileClinicDonorDTO.getLastName();
  }

  public String getGender() {
    Gender gender = mobileClinicDonorDTO.getGender();
    return gender == null ? "" : gender.name();
  }

  public String getBirthDate() {
    return CustomDateFormatter.getDateString(mobileClinicDonorDTO.getBirthDate());
  }

  public String getBloodType() {
    String bloodAbo = mobileClinicDonorDTO.getBloodAbo();
    String bloodRh = mobileClinicDonorDTO.getBloodRh();
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