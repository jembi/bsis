package viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;
import model.bloodtesting.rules.BloodTestingRule;

import org.apache.commons.lang3.StringUtils;

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

  public String getCollectionFieldChanged() {
    String collectionField = "";
    switch (rule.getCollectionFieldChanged()) {
    case BLOODABO:  collectionField = "Blood ABO";
                    break;
    case BLOODRH:   collectionField = "Blood Rh";
                    break;
    case EXTRA:     collectionField = "Extra Info";
                    break;
    case NOCHANGE:  collectionField = "No Change";
                    break;
    case TTISTATUS: collectionField = "TTI Status";
                    break;
    }
    return collectionField;
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
    case BLOODABO: subCategory = "Blood ABO";
                   break;
    case BLOODRH:  subCategory = "Blood Rh";
                   break;
    case TTI:      subCategory = "TTI";
                   break;
    }
    return subCategory;
  }

  public Map<Integer, String> getPatternMap() {
    return patternMap;
  }

  public List<Integer> getPendingTestsIds() {
    List<Integer> pendingTestIds = new ArrayList<Integer>();
    if (rule.getPendingTestsIds() == null)
      return pendingTestIds;
    for (String pendingTestId : rule.getPendingTestsIds().split(",")) {
      if (StringUtils.isBlank(pendingTestId))
        continue;
      pendingTestIds.add(Integer.parseInt(pendingTestId));
    }
    return pendingTestIds;
  }
}
