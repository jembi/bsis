package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.viewmodel.BloodTestingRuleViewModel;

public class BloodTestingRuleViewModelBuilder extends AbstractBuilder<BloodTestingRuleViewModel> {
  
  private Long id;
  private String bloodTestNameShort;
  private DonationField donationField;
  private BloodTestCategory category;
  private String donationFieldValue;
  private Boolean isDeleted = Boolean.FALSE;
  
  public BloodTestingRuleViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  public BloodTestingRuleViewModelBuilder withBloodTestNameShort(String bloodTestingNameShort) {
    this.bloodTestNameShort = bloodTestingNameShort;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder withDonationField(DonationField donationField) {
    this.donationField = donationField;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder withBloodTestCategory(BloodTestCategory category) {
    this.category = category;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder withDinationFieldValue(String donationFieldValue) {
    this.donationFieldValue = donationFieldValue;
    return this;
  }
  
  public BloodTestingRuleViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  @Override
  public BloodTestingRuleViewModel build() {
    BloodTestingRuleViewModel bloodTestingRuleViewModel = new BloodTestingRuleViewModel();
    bloodTestingRuleViewModel.setId(id);
    bloodTestingRuleViewModel.setBloodTestNameShort(bloodTestNameShort);
    bloodTestingRuleViewModel.setDonationField(donationField);
    bloodTestingRuleViewModel.setCategory(category);
    bloodTestingRuleViewModel.setDonationFieldValue(donationFieldValue);
    bloodTestingRuleViewModel.setIsDeleted(isDeleted);
    return bloodTestingRuleViewModel;
  }
  
  public static BloodTestingRuleViewModelBuilder aBloodTestingRuleViewModel() {
    return new BloodTestingRuleViewModelBuilder();
  }

}
