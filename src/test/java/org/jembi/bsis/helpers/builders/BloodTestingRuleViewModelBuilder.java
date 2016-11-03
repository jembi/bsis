package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.viewmodel.BloodTestingRuleViewModel;

public class BloodTestingRuleViewModelBuilder extends AbstractBuilder<BloodTestingRuleViewModel> {
  
  private Long id;
  private String testNameShort;
  private DonationField donationFieldChanged;
  private BloodTestCategory category;
  private String newInformation;
  private String pattern;
  private boolean isDeleted = false;
  
  public BloodTestingRuleViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public BloodTestingRuleViewModelBuilder withTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder withDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder withBloodTestCategory(BloodTestCategory category) {
    this.category = category;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder withNewInformation(String newInformation) {
    this.newInformation = newInformation;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder withPattern(String pattern) {
    this.pattern = pattern;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }
  
  @Override
  public BloodTestingRuleViewModel build() {
    BloodTestingRuleViewModel bloodTestingRuleViewModel = new BloodTestingRuleViewModel();
    bloodTestingRuleViewModel.setId(id);
    bloodTestingRuleViewModel.setTestNameShort(testNameShort);
    bloodTestingRuleViewModel.setDonationFieldChanged(donationFieldChanged);
    bloodTestingRuleViewModel.setCategory(category);
    bloodTestingRuleViewModel.setNewInformation(newInformation);
    bloodTestingRuleViewModel.setPattern(pattern);
    bloodTestingRuleViewModel.setIsDeleted(isDeleted);
    return bloodTestingRuleViewModel;
  }
  
  public static BloodTestingRuleViewModelBuilder aBloodTestingRuleViewModel() {
    return new BloodTestingRuleViewModelBuilder();
  }

}
