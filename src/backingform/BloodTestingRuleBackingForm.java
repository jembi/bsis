package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;
import model.bloodtesting.rules.BloodTestSubCategory;
import model.bloodtesting.rules.BloodTestingRule;
import model.bloodtesting.rules.DonationField;
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

  public void setId(Integer id) {
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
