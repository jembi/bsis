package org.jembi.bsis.helpers.builders;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;

public class BloodTestFullViewModelBuilder extends AbstractBuilder<BloodTestFullViewModel> {
  
  private UUID id;
  private String testNameShort;
  private String testName;
  private Set<String> validResults = new HashSet<>();
  private Set<String> negativeResults = new HashSet<>();
  private Set<String> positiveResults = new HashSet<>();
  private BloodTestCategory category;
  private BloodTestType bloodTestType;
  private Integer rankInCategory;
  private Boolean isActive = Boolean.TRUE;
  private Boolean isDeleted = Boolean.FALSE;
  private boolean flagComponentsForDiscard;
  private boolean flagComponentsContainingPlasmaForDiscard;
  
  public BloodTestFullViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public BloodTestFullViewModelBuilder withTestName(String testName) {
    this.testName = testName;
    return this;
  }

  public BloodTestFullViewModelBuilder withTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
    return this;
  }
  
  public BloodTestFullViewModelBuilder withValidResult(String validResult) {
    this.validResults.add(validResult);
    return this;
  }

  public BloodTestFullViewModelBuilder withValidResults(Set<String> validResults) {
    this.validResults = validResults;
    return this;
  }

  public BloodTestFullViewModelBuilder withNegativeResult(String negativeResult) {
    this.negativeResults.add(negativeResult);
    return this;
  }

  public BloodTestFullViewModelBuilder withNegativeResults(Set<String> negativeResults) {
    this.negativeResults = negativeResults;
    return this;
  }

  public BloodTestFullViewModelBuilder withPositiveResult(String positiveResult) {
    this.positiveResults.add(positiveResult);
    return this;
  }

  public BloodTestFullViewModelBuilder withPositiveResults(Set<String> positiveResults) {
    this.positiveResults = positiveResults;
    return this;
  }

  public BloodTestFullViewModelBuilder withCategory(BloodTestCategory category) {
    this.category = category;
    return this;
  }

  public BloodTestFullViewModelBuilder withBloodTestType(BloodTestType bloodTestType) {
    this.bloodTestType = bloodTestType;
    return this;
  }

  public BloodTestFullViewModelBuilder withRankInCategory(Integer rankInCategory) {
    this.rankInCategory = rankInCategory;
    return this;
  }

  public BloodTestFullViewModelBuilder thatIsDeleted() {
    this.isDeleted = Boolean.TRUE;
    return this;
  }

  public BloodTestFullViewModelBuilder thatIsInActive() {
    this.isActive = Boolean.FALSE;
    return this;
  }
  
  public BloodTestFullViewModelBuilder thatShouldFlagComponentsForDiscard() {
    this.flagComponentsForDiscard = true;
    return this;
  }

  public BloodTestFullViewModelBuilder thatShouldNotFlagComponentsForDiscard() {
    this.flagComponentsForDiscard = false;
    return this;
  }

  public BloodTestFullViewModelBuilder thatShouldFlagComponentsContainingPlasmaForDiscard() {
    this.flagComponentsContainingPlasmaForDiscard = true;
    return this;
  }

  public BloodTestFullViewModelBuilder thatShouldNotFlagComponentsContainingPlasmaForDiscard() {
    this.flagComponentsContainingPlasmaForDiscard = false;
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
    viewModel.setCategory(category);
    viewModel.setBloodTestType(bloodTestType);
    viewModel.setIsActive(isActive);
    viewModel.setIsDeleted(isDeleted);
    viewModel.setFlagComponentsContainingPlasmaForDiscard(flagComponentsContainingPlasmaForDiscard);
    viewModel.setFlagComponentsForDiscard(flagComponentsForDiscard);
    return viewModel;
  }
  
  public static BloodTestFullViewModelBuilder aBloodTestFullViewModel() {
    return new BloodTestFullViewModelBuilder();
  }

  public static BloodTestFullViewModelBuilder aBasicTTIBloodTestFullViewModel() {
    return new BloodTestFullViewModelBuilder().withCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.BASIC_TTI);
  }

  public static BloodTestFullViewModelBuilder aRepeatTTIBloodTestFullViewModel() {
    return new BloodTestFullViewModelBuilder().withCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.REPEAT_TTI);
  }

  public static BloodTestFullViewModelBuilder aConfirmatoryTTIBloodTestFullViewModel() {
    return new BloodTestFullViewModelBuilder().withCategory(BloodTestCategory.TTI).withBloodTestType(BloodTestType.CONFIRMATORY_TTI);
  }

  public static BloodTestFullViewModelBuilder aBasicBloodTypingBloodTestFullViewModel() {
    return new BloodTestFullViewModelBuilder()
      .withCategory(BloodTestCategory.BLOODTYPING)
      .withBloodTestType(BloodTestType.BASIC_BLOODTYPING);
  }

  public static BloodTestFullViewModelBuilder aRepeatBloodTypingBloodTestFullViewModel() {
    return new BloodTestFullViewModelBuilder()
      .withCategory(BloodTestCategory.BLOODTYPING)
      .withBloodTestType(BloodTestType.REPEAT_BLOODTYPING);
  }
}
