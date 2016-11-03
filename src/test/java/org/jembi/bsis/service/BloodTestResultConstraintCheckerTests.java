package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRuleResultSet;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BloodTestResultConstraintCheckerTests {

  private static final boolean DONATION_RELEASED = true;
  private static final boolean DONATION_NOT_RELEASED = false;

  @InjectMocks
  private BloodTestResultConstraintChecker bloodTestResultConstraintChecker;

  @Test
  public void testCanEditCompleteAmbiguousBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTestWithOtherPendingAboTests() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    bloodTest.setId(1l);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    List<String> pendingTestIds = new ArrayList<String>();
    pendingTestIds.add("123");
    bloodTestingRuleResultSet.setPendingAboTestsIds(pendingTestIds);
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("2");
    rule1.setPendingTestsIds("123");
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTestWithAboPendingTests() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    bloodTest.setId(1l);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    List<String> pendingTestIds = new ArrayList<String>();
    pendingTestIds.add("123");
    bloodTestingRuleResultSet.setPendingAboTestsIds(pendingTestIds);
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("1");
    rule1.setPendingTestsIds("123");
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTestWithOtherPendingRhTests() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    bloodTest.setId(1l);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    List<String> pendingTestIds = new ArrayList<String>();
    pendingTestIds.add("3");
    bloodTestingRuleResultSet.setPendingRhTestsIds(pendingTestIds);
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("2");
    rule1.setPendingTestsIds("3");
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTestWithRhPendingTests() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    bloodTest.setId(1l);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    List<String> pendingTestIds = new ArrayList<String>();
    pendingTestIds.add("123");
    bloodTestingRuleResultSet.setPendingRhTestsIds(pendingTestIds);
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("1");
    rule1.setPendingTestsIds("123");
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditNotDoneBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditNoTypeDeterminedBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(false));
  }

  @Test
  public void testCanEditReleasedDonationBloodTypingTestResult() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_RELEASED);

    assertThat(canEdit, is(false));
  }

  @Test
  public void testCanEditTTISafeWithNoPendingBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.TTI);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setTtiStatus(TTIStatus.TTI_SAFE);
    List<String> pendingTtiTestIds = new ArrayList<String>();
    bloodTestingRuleResultSet.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingTtiTestIds);
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("1");
    rule1.setPendingTestsIds("2");
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);


    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditTTISafeWithPendingBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.TTI);
    bloodTest.setId(1l);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    bloodTestResult.setResult("OUTCOME");
    Map<String, String> availableTestResults = new HashMap<String, String>();
    availableTestResults.put("1", "RESULT");
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), availableTestResults, new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setTtiStatus(TTIStatus.TTI_SAFE);
    List<String> pendingTtiTestIds = new ArrayList<String>();
    pendingTtiTestIds.add("2");
    bloodTestingRuleResultSet.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingTtiTestIds);
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("1");
    rule1.setPendingTestsIds("2");
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditTTISafeWithOtherPendingBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.TTI);
    bloodTest.setId(1l);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    bloodTestResult.setResult("OUTCOME");
    Map<String, String> availableTestResults = new HashMap<String, String>();
    availableTestResults.put("1", "RESULT");
    availableTestResults.put("2", "RESULT");
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), availableTestResults, new HashMap<Long, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setTtiStatus(TTIStatus.TTI_SAFE);
    List<String> pendingTtiTestIds = new ArrayList<String>();
    pendingTtiTestIds.add("3");
    bloodTestingRuleResultSet.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingTtiTestIds);
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("1");
    rule1.setPendingTestsIds("2");
    BloodTestingRule rule2 = new BloodTestingRule();
    rule2.setBloodTestsIds("2");
    rule2.setPendingTestsIds("3");
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    rules.add(rule2);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(false));
  }

  @Test
  public void testCanEditTTISafeWithOtherRelatedPendingBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.TTI);
    bloodTest.setId(1l);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    bloodTestResult.setResult("OUTCOME");
    Map<String, String> availableTestResults = new HashMap<String, String>();
    availableTestResults.put("1", "RESULT");
    availableTestResults.put("3", "RESULT");
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), availableTestResults, new HashMap<Long, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setTtiStatus(TTIStatus.TTI_SAFE);
    List<String> pendingTtiTestIds = new ArrayList<String>();
    pendingTtiTestIds.add("2");
    bloodTestingRuleResultSet.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingTtiTestIds);
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("1");
    rule1.setPendingTestsIds("2");
    BloodTestingRule rule2 = new BloodTestingRule();
    rule2.setBloodTestsIds("2");
    rule2.setPendingTestsIds("3");
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    rules.add(rule2);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(false));
  }

  @Test
  public void testCanEditReleasedDonationTTITestResult() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(BloodTestCategory.TTI);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_RELEASED);

    assertThat(canEdit, is(false));
  }

  @Test
  public void testCanEditUnknownTestCategory() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = new BloodTest();
    bloodTest.setCategory(null);
    bloodTest.setId(1l);
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Long, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  private List<BloodTestingRule> generateTestBloodTestingRules() {
    BloodTestingRule rule1 = new BloodTestingRule();
    rule1.setBloodTestsIds("1");
    rule1.setPendingTestsIds("");
    BloodTestingRule rule2 = new BloodTestingRule();
    rule2.setBloodTestsIds("2");
    rule2.setPendingTestsIds("3");
    BloodTestingRule rule3 = new BloodTestingRule();
    rule3.setBloodTestsIds("3");
    rule3.setPendingTestsIds("4");
    BloodTestingRule rule4 = new BloodTestingRule();
    rule4.setBloodTestsIds("4");
    rule4.setPendingTestsIds("");
    List<BloodTestingRule> bloodTestingRules = new ArrayList<BloodTestingRule>();
    bloodTestingRules.add(rule1);
    bloodTestingRules.add(rule2);
    bloodTestingRules.add(rule3);
    bloodTestingRules.add(rule4);
    return bloodTestingRules;
  }
}
