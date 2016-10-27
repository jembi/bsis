package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;

public class BloodTestViewModelBuilder extends AbstractBuilder<BloodTestFullViewModel> {
  
  private Long id;
  private String testNameShort;
  private String testName;
  private List<String> validResults = new ArrayList<>();
  private List<String> negativeResults = new ArrayList<>();
  private List<String> positiveResults = new ArrayList<>();
  private BloodTestCategory bloodTestCategory;
  private BloodTestType bloodTestType;
  private Integer rankInCategory;
  private Boolean isActive = Boolean.TRUE;
  private Boolean isDeleted = Boolean.FALSE;
  
  public BloodTestViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public BloodTestViewModelBuilder withTestName(String testName) {
    this.testName = testName;
    return this;
  }

  public BloodTestViewModelBuilder withTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
    return this;
  }
  
  public BloodTestViewModelBuilder withValidResult(String validResult) {
    this.validResults.add(validResult);
    return this;
  }

  public BloodTestViewModelBuilder withValidResults(List<String> validResults) {
    this.validResults = validResults;
    return this;
  }

  public BloodTestViewModelBuilder withNegativeResult(String negativeResult) {
    this.negativeResults.add(negativeResult);
    return this;
  }

  public BloodTestViewModelBuilder withNegativeResults(List<String> negativeResults) {
    this.negativeResults = negativeResults;
    return this;
  }

  public BloodTestViewModelBuilder withPositiveResult(String positiveResult) {
    this.positiveResults.add(positiveResult);
    return this;
  }

  public BloodTestViewModelBuilder withPositiveResults(List<String> positiveResults) {
    this.positiveResults = positiveResults;
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

  public BloodTestViewModelBuilder withRankInCategory(Integer rankInCategory) {
    this.rankInCategory = rankInCategory;
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
  public BloodTestFullViewModel build() {
    BloodTestFullViewModel viewModel = new BloodTestFullViewModel();
    viewModel.setId(id);
    viewModel.setTestName(testName);
    viewModel.setTestNameShort(testNameShort);
    viewModel.setValidResults(validResults);
    viewModel.setPositiveResults(positiveResults);
    viewModel.setNegativeResults(negativeResults);
    viewModel.setRankInCategory(rankInCategory);
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
