package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;

public class MobileClinicDonorExportBuilder extends AbstractBuilder<MobileClinicExportDonorViewModel> { 
  
  private Long id;
  private String donorNumber;
  private String firstName;
  private String lastName;
  private String gender;
  private String bloodType;
  private DonorStatus donorStatus;
  private String birthDate;
  private LocationFullViewModel venue;
  private Boolean deleted;
  private Boolean eligibility;
  
  public MobileClinicDonorExportBuilder withId(Long id){
    this.id = id;
    return  this;
  }
  
  public MobileClinicDonorExportBuilder withDonorNumber(String donorNumber){
    this.donorNumber = donorNumber;
    return this;
  }
  
  public MobileClinicDonorExportBuilder withFirstName(String firstName){
    this.firstName = firstName;
    return this;
  }
  
  public MobileClinicDonorExportBuilder withLastName(String lastName){
    this.lastName = lastName;
    return this;
  }
  
  public MobileClinicDonorExportBuilder withGender(String gender){
    this.gender = gender;
    return this;
  }
  
  public MobileClinicDonorExportBuilder withBloodType(String bloodType){
    this.bloodType = bloodType;
    return this;
  }
  
  public MobileClinicDonorExportBuilder withDonorStatus(DonorStatus donorStatus){
    this.donorStatus = donorStatus;
    return this;
  }
  
  public MobileClinicDonorExportBuilder withBirthDate(String birthDate){
    this.birthDate = birthDate;
    return this;
  }
  
  public MobileClinicDonorExportBuilder withVenue(LocationFullViewModel venue){
    this.venue = venue;
    return this;
  }
  
  
  public MobileClinicDonorExportBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public MobileClinicDonorExportBuilder thatIsNotDeleted() {
    deleted = false;
    return this;
  }
  
  public MobileClinicDonorExportBuilder thatIsEligibility() {
    eligibility = true;
    return this;
  }

  public MobileClinicDonorExportBuilder thatIsNotEligibility() {
    eligibility  = false;
    return this;
  }
  
  @Override
  public MobileClinicExportDonorViewModel build() {
    MobileClinicExportDonorViewModel donorDTO = new MobileClinicExportDonorViewModel();
    donorDTO.setBirthDate(birthDate);
    donorDTO.setBloodType(bloodType);
    donorDTO.setDonorNumber(donorNumber);
    donorDTO.setDonorStatus(donorStatus);
    donorDTO.setEligibility(eligibility);
    donorDTO.setFirstName(firstName);
    donorDTO.setGender(gender);
    donorDTO.setId(id);
    donorDTO.setIsDeleted(deleted);
    donorDTO.setLastName(lastName);
    donorDTO.setVenue(venue);    
    return donorDTO;
    
  }

  public static MobileClinicDonorExportBuilder aMobileClinicDonorExport() {
    return new MobileClinicDonorExportBuilder();
  }
}