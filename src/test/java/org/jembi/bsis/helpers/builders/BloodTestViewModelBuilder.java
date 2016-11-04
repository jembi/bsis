package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.viewmodel.BloodTestViewModel;

public class BloodTestViewModelBuilder extends AbstractBuilder<BloodTestViewModel> {
  
  private Long id;
  private String testNameShort;
  private BloodTestCategory category;
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
  
  public BloodTestViewModelBuilder withCategory(BloodTestCategory category) {
    this.category = category;
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
    viewModel.setCategory(category);
    viewModel.setBloodTestType(bloodTestType);
    viewModel.setIsActive(isActive);
    viewModel.setIsDeleted(isDeleted);
    return viewModel;
  }
  
  public static BloodTestViewModelBuilder aBloodTestViewModel() {
    return new BloodTestViewModelBuilder();
  }

  public static BloodTestViewModelBuilder aBasicTTIBloodTestViewModel() {
    return new BloodTestViewModelBuilder().withCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.BASIC_TTI);
  }

  public static BloodTestViewModelBuilder aRepeatTTIBloodTestViewModel() {
    return new BloodTestViewModelBuilder().withCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.REPEAT_TTI);
  }

  public static BloodTestViewModelBuilder aConfirmatoryTTIBloodTestViewModel() {
    return new BloodTestViewModelBuilder().withCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.CONFIRMATORY_TTI);
  }

  public static BloodTestViewModelBuilder aBasicBloodTypingBloodTestViewModel() {
    return new BloodTestViewModelBuilder()
      .withCategory(BloodTestCategory.BLOODTYPING)
      .withBloodTestType(BloodTestType.BASIC_BLOODTYPING);
  }

  public static BloodTestViewModelBuilder aRepeatBloodTypingBloodTestViewModel() {
    return new BloodTestViewModelBuilder()
      .withCategory(BloodTestCategory.BLOODTYPING)
      .withBloodTestType(BloodTestType.REPEAT_BLOODTYPING);
  }
}
