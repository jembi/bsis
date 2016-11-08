package org.jembi.bsis.backingform;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

import com.fasterxml.jackson.annotation.JsonIgnore;

import scala.actors.threadpool.Arrays;

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

  public void setIsDeleted(Boolean isDeleted) {
    typingRule.setIsDeleted(isDeleted);
  }

  public void setNewInformation(String newInformation) {
    typingRule.setNewInformation(newInformation);
  }

  public void setBloodTest(BloodTest bloodTest) {
    typingRule.setBloodTest(bloodTest);
  }

}
