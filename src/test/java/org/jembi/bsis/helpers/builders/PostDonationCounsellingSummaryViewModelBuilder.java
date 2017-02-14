package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingSummaryViewModel;

public class PostDonationCounsellingSummaryViewModelBuilder
    extends AbstractBuilder<PostDonationCounsellingSummaryViewModel> {

  private long id;
  private String counselled;
  private String referred;
  private Date counsellingDate;
  private String donorNumber;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date birthDate;
  private String bloodAbo;
  private String bloodRh;
  private String donationIdentificationNumber;
  private Date donationDate;
  private LocationViewModel venue;
  
  public PostDonationCounsellingSummaryViewModelBuilder withId(long id) {
    this.id = id;
    return this;
  }
  
  public PostDonationCounsellingSummaryViewModelBuilder withCounselled(String counselled) {
    this.counselled = counselled;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withReferred(String referred) {
    this.referred = referred;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withBirthDate(Date birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withbloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withDonationIdentificationNumber(
      String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public PostDonationCounsellingSummaryViewModelBuilder withDonationDate(Date donationDate) {
    this.donationDate = donationDate;
    return this;
  }
  
  public PostDonationCounsellingSummaryViewModelBuilder withVenue(LocationViewModel venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public PostDonationCounsellingSummaryViewModel build() {
    PostDonationCounsellingSummaryViewModel viewModel = new PostDonationCounsellingSummaryViewModel();
    viewModel.setId(id);
    viewModel.setCounsellingDate(counsellingDate);
    viewModel.setBirthDate(birthDate);
    viewModel.setBloodAbo(bloodAbo);
    viewModel.setBloodRh(bloodRh);
    viewModel.setCounselled(counselled);
    viewModel.setReferred(referred);
    viewModel.setDonationDate(donationDate);
    viewModel.setFirstName(firstName);
    viewModel.setLastName(lastName);
    viewModel.setGender(gender);
    viewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    viewModel.setVenue(venue);
    viewModel.setDonorNumber(donorNumber);
    return viewModel;
  }

  public static PostDonationCounsellingSummaryViewModelBuilder aPostDonationCounsellingSummaryViewModel() {
    return new PostDonationCounsellingSummaryViewModelBuilder();
  }

}
