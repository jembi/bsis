package utils;

import java.util.Arrays;
import java.util.List;

import model.bloodtest.BloodTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.BloodTestRepository;

@Component
public class BloodTestUtils {

  @Autowired
  public BloodTestRepository bloodTestRepository;

  public BloodTestUtils() {
  }

  public List<String> getCorrectResults(BloodTest bt) {
//    String[] correctResults;
//    if (bt.getNegativeRequiredForUse()) {
//      correctResults = bt.getNegativeResults().split(",");
//    } else {
//      correctResults = bt.getPositiveResults().split(",");
//    }
//    return Arrays.asList(correctResults);
    return Arrays.asList(new String[0]);
  }

  public boolean isTestResultCorrect(BloodTest bt, String t) {
    return getCorrectResults(bt).contains(t);
  }
}
