package org.jembi.bsis.helpers.builders;

import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleFullViewModel;

public class BloodTestingRuleFullViewModelBuilder extends AbstractBuilder<BloodTestingRuleFullViewModel> {
  
  private UUID id;
  private String testNameShort;
  private DonationField donationFieldChanged;
  private BloodTestCategory category;
  private String newInformation;
  private String pattern;
  private BloodTestFullViewModel bloodTest;
  private Set<BloodTestViewModel> pendingTests;
  private boolean isDeleted = false;
  
  public BloodTestingRuleFullViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public BloodTestingRuleFullViewModelBuilder withTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
    return this;
  }
  
  public BloodTestingRuleFullViewModelBuilder withDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
    return this;
  }
  
  public BloodTestingRuleFullViewModelBuilder withBloodTestCategory(BloodTestCategory category) {
    this.category = category;
    return this;
  }
  
  public BloodTestingRuleFullViewModelBuilder withNewInformation(String newInformation) {
    this.newInformation = newInformation;
    return this;
  }
  
  public BloodTestingRuleFullViewModelBuilder withPattern(String pattern) {
    this.pattern = pattern;
    return this;
  }
  
  public BloodTestingRuleFullViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  public BloodTestingRuleFullViewModelBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }

  public BloodTestingRuleFullViewModelBuilder withBloodTest(BloodTestFullViewModel bloodTest) {
    this.bloodTest = bloodTest;
    return  this;
  }


  public BloodTestingRuleFullViewModelBuilder withPendingTests(Set<BloodTestViewModel> pendingTests) {
    this.pendingTests = pendingTests;
    return this;
  }
  
  @Override
  public BloodTestingRuleFullViewModel build() {
    BloodTestingRuleFullViewModel bloodTestingRuleFullViewModel = new BloodTestingRuleFullViewModel();
    bloodTestingRuleFullViewModel.setId(id);
    bloodTestingRuleFullViewModel.setTestNameShort(testNameShort);
    bloodTestingRuleFullViewModel.setDonationFieldChanged(donationFieldChanged);
    bloodTestingRuleFullViewModel.setCategory(category);
    bloodTestingRuleFullViewModel.setNewInformation(newInformation);
    bloodTestingRuleFullViewModel.setPattern(pattern);
    bloodTestingRuleFullViewModel.setIsDeleted(isDeleted);
    bloodTestingRuleFullViewModel.setBloodTest(bloodTest);
    bloodTestingRuleFullViewModel.setPendingTests(pendingTests);
    return bloodTestingRuleFullViewModel;
  }
  
  public static BloodTestingRuleFullViewModelBuilder aBloodTestingRuleFullViewModel() {
    return new BloodTestingRuleFullViewModelBuilder();
  }

}
