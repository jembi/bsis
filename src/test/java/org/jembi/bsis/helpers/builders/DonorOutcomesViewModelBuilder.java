package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.DonorOutcomesViewModel;

public class DonorOutcomesViewModelBuilder extends AbstractBuilder<DonorOutcomesViewModel> {
  
  private String donorNumber;
  private String lastName;
  private String firstName;
  private Gender gender;
  private Date birthDate;
  private Date donationDate;
  private String donationIdentificationNumber;
  private String bloodAbo;
  private String bloodRh;
  private List<BloodTestResultFullViewModel> bloodTestResults;

  public DonorOutcomesViewModelBuilder withDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
    return this;
  }

  public DonorOutcomesViewModelBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public DonorOutcomesViewModelBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public DonorOutcomesViewModelBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public DonorOutcomesViewModelBuilder withBirthDate(Date birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public DonorOutcomesViewModelBuilder withDonationDate(Date donationDate) {
    this.donationDate = donationDate;
    return this;
  }

  public DonorOutcomesViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }
  
  public DonorOutcomesViewModelBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }
  
  public DonorOutcomesViewModelBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public DonorOutcomesViewModelBuilder withBloodTestResults(List<BloodTestResultFullViewModel> bloodTestResults) {
    this.bloodTestResults = bloodTestResults;
    return this;
  }

  @Override
  public DonorOutcomesViewModel build() {
    DonorOutcomesViewModel viewModel = new DonorOutcomesViewModel();
    viewModel.setDonorNumber(donorNumber);
    viewModel.setLastName(lastName);
    viewModel.setFirstName(firstName);
    viewModel.setGender(gender);
    viewModel.setBirthDate(birthDate);
    viewModel.setDonationDate(donationDate);
    viewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    viewModel.setBloodAbo(bloodAbo);
    viewModel.setBloodRh(bloodRh);
    viewModel.setBloodTestResults(bloodTestResults);
    return viewModel;
  }
  
  public static DonorOutcomesViewModelBuilder aDonorOutcomesViewModel() {
    return new DonorOutcomesViewModelBuilder();
  }

}
