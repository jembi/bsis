package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;

public class MobileClinicExportDonorViewModelBuilder extends AbstractBuilder<MobileClinicExportDonorViewModel> { 
  
  private UUID id;
  private String donorNumber;
  private String firstName;
  private String lastName;
  private String gender;
  private String bloodType;
  private DonorStatus donorStatus;
  private String birthDate;
  private LocationViewModel venue;
  private Boolean deleted;
  private Boolean eligibility;
  
  public MobileClinicExportDonorViewModelBuilder withId(UUID id){
    this.id = id;
    return  this;
  }
  
  public MobileClinicExportDonorViewModelBuilder withDonorNumber(String donorNumber){
    this.donorNumber = donorNumber;
    return this;
  }
  
  public MobileClinicExportDonorViewModelBuilder withFirstName(String firstName){
    this.firstName = firstName;
    return this;
  }
  
  public MobileClinicExportDonorViewModelBuilder withLastName(String lastName){
    this.lastName = lastName;
    return this;
  }
  
  public MobileClinicExportDonorViewModelBuilder withGender(String gender){
    this.gender = gender;
    return this;
  }
  
  public MobileClinicExportDonorViewModelBuilder withBloodType(String bloodType){
    this.bloodType = bloodType;
    return this;
  }
  
  public MobileClinicExportDonorViewModelBuilder withDonorStatus(DonorStatus donorStatus){
    this.donorStatus = donorStatus;
    return this;
  }
  
  public MobileClinicExportDonorViewModelBuilder withBirthDate(String birthDate){
    this.birthDate = birthDate;
    return this;
  }
  
  public MobileClinicExportDonorViewModelBuilder withVenue(LocationViewModel venue){
    this.venue = venue;
    return this;
  }
  
  
  public MobileClinicExportDonorViewModelBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public MobileClinicExportDonorViewModelBuilder thatIsNotDeleted() {
    deleted = false;
    return this;
  }
  
  public MobileClinicExportDonorViewModelBuilder thatIsEligible() {
    eligibility = true;
    return this;
  }

  public MobileClinicExportDonorViewModelBuilder thatIsNotEligble() {
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

  public static MobileClinicExportDonorViewModelBuilder aMobileClinicExportDonorViewModel() {
    return new MobileClinicExportDonorViewModelBuilder();
  }
}