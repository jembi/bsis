package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestViewModel;

public class BloodTestViewModelBuilder extends AbstractBuilder<BloodTestViewModel> {
  
  private Long id;
  private String testNameShort;
  private BloodTestCategory bloodTestCategory;
  private BloodTestType bloodTestType;
  private Boolean isActive = Boolean.TRUE;
  private Boolean isDeleted = Boolean.FALSE;
  
  public BloodTestViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public BloodTestViewModelBuilder withTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
    return this;
  }
  
  public BloodTestViewModelBuilder withBloodTestCategory(BloodTestCategory bloodTestCategory) {
    this.bloodTestCategory = bloodTestCategory;
    return this;
  }

  public BloodTestViewModelBuilder withBloodTestType(BloodTestType bloodTestType) {
    this.bloodTestType = bloodTestType;
    return this;
  }

  public BloodTestViewModelBuilder thatIsDeleted() {
    this.isDeleted = Boolean.TRUE;
    return this;
  }

  public BloodTestViewModelBuilder thatIsInActive() {
    this.isActive = Boolean.FALSE;
    return this;
  }

  @Override
  public BloodTestViewModel build() {
    BloodTestViewModel viewModel = new BloodTestViewModel();
    viewModel.setId(id);
    viewModel.setTestNameShort(testNameShort);
    viewModel.setBloodTestCategory(bloodTestCategory);
    viewModel.setBloodTestType(bloodTestType);
    viewModel.setIsActive(isActive);
    viewModel.setIsDeleted(isDeleted);
    return viewModel;
  }
  
  public static BloodTestViewModelBuilder aBloodTestViewModel() {
    return new BloodTestViewModelBuilder();
  }

  public static BloodTestViewModelBuilder aBasicTTIBloodTestViewModel() {
    return new BloodTestViewModelBuilder().withBloodTestCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.BASIC_TTI);
  }

  public static BloodTestViewModelBuilder aRepeatTTIBloodTestViewModel() {
    return new BloodTestViewModelBuilder().withBloodTestCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.REPEAT_TTI);
  }

  public static BloodTestViewModelBuilder aConfirmatoryTTIBloodTestViewModel() {
    return new BloodTestViewModelBuilder().withBloodTestCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.CONFIRMATORY_TTI);
  }

  public static BloodTestViewModelBuilder aBasicBloodTypingBloodTestViewModel() {
    return new BloodTestViewModelBuilder()
      .withBloodTestCategory(BloodTestCategory.BLOODTYPING)
      .withBloodTestType(BloodTestType.BASIC_BLOODTYPING);
  }

  public static BloodTestViewModelBuilder aRepeatBloodTypingBloodTestViewModel() {
    return new BloodTestViewModelBuilder()
      .withBloodTestCategory(BloodTestCategory.BLOODTYPING)
      .withBloodTestType(BloodTestType.REPEAT_BLOODTYPING);
  }
}
