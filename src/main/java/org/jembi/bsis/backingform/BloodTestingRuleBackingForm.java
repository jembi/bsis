package org.jembi.bsis.backingform;

import scala.actors.threadpool.Arrays;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestContext;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestSubCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BloodTestingRuleBackingForm {

  @JsonIgnore
  private BloodTestingRule typingRule;

  public BloodTestingRuleBackingForm() {
    typingRule = new BloodTestingRule();
  }

  public BloodTestingRule getTypingRule() {
    return typingRule;
  }

  public void setTypingRule(BloodTestingRule typingRule) {
    this.typingRule = typingRule;
  }

  public void setId(Long id) {
    typingRule.setId(id);
  }

  public void setPendingTestsIds(int[] pendingTestsIds) {
    String testIds = Arrays.toString(pendingTestsIds).replaceAll("\\s", "");
    typingRule.setPendingTestsIds(testIds.substring(1, testIds.length() - 1));
  }

  public void setCategory(String category) {
    typingRule.setCategory(BloodTestCategory.valueOf(category.replaceAll("\\s", "").toUpperCase()));
  }

  public void setDonationFieldChanged(String donationField) {
    typingRule.setDonationFieldChanged(DonationField.valueOf(donationField.replaceAll("\\s", "").toUpperCase()));
  }

  public void setPattern(String pattern) {
    typingRule.setPattern(pattern);
  }

  public void setContext(String context) {
    typingRule.setContext(BloodTestContext.valueOf(context.replaceAll("\\s", "").toUpperCase()));
  }

  public void setExtraInformation(String extraInformation) {
    typingRule.setExtraInformation(extraInformation);
  }

  public void setSubCategory(String subCategory) {
    typingRule.setSubCategory(BloodTestSubCategory.valueOf(subCategory.replaceAll("\\s", "").toUpperCase()));
  }

  public void setIsActive(Boolean isActive) {
    typingRule.setIsActive(isActive);
  }

  public void setMarkSampleAsUnsafe(Boolean MarkSampleAsUnsafe) {
    typingRule.setMarkSampleAsUnsafe(MarkSampleAsUnsafe);
  }

  public void setNewInformation(String newInformation) {
    typingRule.setNewInformation(newInformation);
  }

  public void setBloodTestsIds(String bloodTestIds) {
    typingRule.setBloodTestsIds(bloodTestIds);
  }

}
