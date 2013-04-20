package viewmodel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;
import model.bloodtesting.CollectionField;
import model.bloodtesting.rules.BloodTestingRule;

public class BloodTestingRuleViewModel {

  private BloodTestingRule rule;
  private Map<Integer, String> patternMap;

  public BloodTestingRuleViewModel(BloodTestingRule rule) {
    this.rule = rule;
    patternMap = new HashMap<Integer, String>();
    List<String> testIds = Arrays.asList(rule.getBloodTestsIds().split(","));
    String pattern = rule.getPattern();
    List<String> testResults = Arrays.asList(pattern.split(","));
    int index = 0;
    for (String testId : testIds) {
      patternMap.put(Integer.parseInt(testId), testResults.get(index));
      index++;
    }
  }

  public Integer getId() {
    return rule.getId();
  }

  public String getBloodTestsIds() {
    return rule.getBloodTestsIds();
  }

  public String getPattern() {
    return rule.getPattern();
  }

  public CollectionField getCollectionFieldChanged() {
    return rule.getCollectionFieldChanged();
  }

  public Boolean getMarkSampleAsUnsafe() {
    return rule.getMarkSampleAsUnsafe();
  }

  public Boolean getIsActive() {
    return rule.getIsActive();
  }

  public String getNewInformation() {
    return rule.getNewInformation();
  }

  public String getExtraInformation() {
    return rule.getExtraInformation();
  }

  public String getExtraAboTestsIds() {
    return rule.getExtraAboTestsIds();
  }

  public String getExtraRhTestsIds() {
    return rule.getExtraRhTestsIds();
  }

  public String getExtraTtiTestsIds() {
    return rule.getExtraTtiTestsIds();
  }

  public BloodTestContext getContext() {
    return rule.getContext();
  }

  public BloodTestCategory getCategory() {
    return rule.getCategory();
  }

  public Map<Integer, String> getPatternMap() {
    return patternMap;
  }
}
