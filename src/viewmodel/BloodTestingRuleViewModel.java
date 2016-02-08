package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;
import model.bloodtesting.rules.BloodTestingRule;

import org.apache.commons.lang3.StringUtils;

public class BloodTestingRuleViewModel {

  private BloodTestingRule rule;
  private Map<Long, String> patternMap;

  public BloodTestingRuleViewModel(BloodTestingRule rule) {
    this.rule = rule;
    patternMap = new HashMap<Long, String>();
    List<String> testIds = Arrays.asList(rule.getBloodTestsIds().split(","));
    String pattern = rule.getPattern();
    List<String> testResults = Arrays.asList(pattern.split(","));
    int index = 0;
    for (String testId : testIds) {
      patternMap.put(Long.parseLong(testId), testResults.get(index));
      index++;
    }
  }

  public Long getId() {
    return rule.getId();
  }

  public String getBloodTestsIds() {
    return rule.getBloodTestsIds();
  }

  public String getPattern() {
    return rule.getPattern();
  }

  public String getDonationFieldChanged() {
    String donationField = "";
    switch (rule.getDonationFieldChanged()) {
      case BLOODABO:
        donationField = "Blood ABO";
        break;
      case BLOODRH:
        donationField = "Blood Rh";
        break;
      case EXTRA:
        donationField = "Extra Info";
        break;
      case NOCHANGE:
        donationField = "No Change";
        break;
      case TTISTATUS:
        donationField = "TTI Status";
        break;
    }
    return donationField;
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

  public BloodTestContext getContext() {
    return rule.getContext();
  }

  public BloodTestCategory getCategory() {
    return rule.getCategory();
  }

  public String getSubCategory() {
    String subCategory = "";
    switch (rule.getSubCategory()) {
      case BLOODABO:
        subCategory = "Blood ABO";
        break;
      case BLOODRH:
        subCategory = "Blood Rh";
        break;
      case TTI:
        subCategory = "TTI";
        break;
    }
    return subCategory;
  }

  @JsonIgnore
  public Map<Long, String> getPatternMap() {
    return patternMap;
  }

  public List<Long> getPendingTestsIds() {
    List<Long> pendingTestIds = new ArrayList<Long>();
    if (rule.getPendingTestsIds() == null)
      return pendingTestIds;
    for (String pendingTestId : rule.getPendingTestsIds().split(",")) {
      if (StringUtils.isBlank(pendingTestId))
        continue;
      pendingTestIds.add(Long.parseLong(pendingTestId));
    }
    return pendingTestIds;
  }

}
