package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.viewmodel.DuplicateDonorViewModel;

public class DuplicateDonorViewModelBuilder extends AbstractBuilder<DuplicateDonorViewModel> {

  private String groupKey;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date birthDate;
  private long count;

  public DuplicateDonorViewModelBuilder withGroupKey(String groupKey) {
    this.groupKey = groupKey;
    return this;
  }

  public DuplicateDonorViewModelBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public DuplicateDonorViewModelBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public DuplicateDonorViewModelBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public DuplicateDonorViewModelBuilder withBirthDate(Date birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public DuplicateDonorViewModelBuilder withCount(Long count) {
    this.count = count;
    return this;
  }

  @Override
  public DuplicateDonorViewModel build() {
    DuplicateDonorViewModel duplicateDonorViewModel = new DuplicateDonorViewModel();
    duplicateDonorViewModel.setBirthDate(birthDate);
    duplicateDonorViewModel.setCount(count);
    duplicateDonorViewModel.setFirstName(firstName);
    duplicateDonorViewModel.setLastName(lastName);
    duplicateDonorViewModel.setGender(gender);
    duplicateDonorViewModel.setGroupKey(groupKey);
    return duplicateDonorViewModel;
  }

  public static DuplicateDonorViewModelBuilder aDuplicateDonorViewModel() {
    return new DuplicateDonorViewModelBuilder();
  }

}
