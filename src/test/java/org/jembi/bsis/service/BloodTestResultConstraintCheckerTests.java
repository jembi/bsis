package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBasicBloodTypingBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBasicTTIBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aRepeatBloodTypingBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aRepeatTTIBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder.aBloodTestingRule;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRuleResultSet;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
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
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTestWithOtherPendingAboTests() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTest anotherBloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTest pendingBloodTest = aRepeatBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    List<UUID> pendingTestIds = new ArrayList<>();
    pendingTestIds.add(pendingBloodTest.getId());
    bloodTestingRuleResultSet.setPendingAboTestsIds(pendingTestIds);
    BloodTestingRule rule1 = aBloodTestingRule()
        .withBloodTest(anotherBloodTest)
        .withPendingBloodTest(pendingBloodTest)
        .build();
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTestWithAboPendingTests() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTest pendingBloodTest = aRepeatBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    List<UUID> pendingTestIds = new ArrayList<>();
    pendingTestIds.add(pendingBloodTest.getId());
    bloodTestingRuleResultSet.setPendingAboTestsIds(pendingTestIds);
    BloodTestingRule rule1 = aBloodTestingRule()
        .withBloodTest(bloodTest)
        .withPendingBloodTest(pendingBloodTest)
        .build();
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTestWithOtherPendingRhTests() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTest anotherBloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTest pendingBloodTest = aRepeatBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    List<UUID> pendingTestIds = new ArrayList<>();
    pendingTestIds.add(pendingBloodTest.getId());
    bloodTestingRuleResultSet.setPendingRhTestsIds(pendingTestIds);
    BloodTestingRule rule1 = aBloodTestingRule()
        .withBloodTest(anotherBloodTest)
        .withPendingBloodTest(pendingBloodTest)
        .build();
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditCompleteNoMatchBloodTestWithRhPendingTests() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTest pendingBloodTest = aRepeatBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    List<UUID> pendingTestIds = new ArrayList<>();
    pendingTestIds.add(pendingBloodTest.getId());
    bloodTestingRuleResultSet.setPendingRhTestsIds(pendingTestIds);
    BloodTestingRule rule1 = aBloodTestingRule()
        .withBloodTest(bloodTest)
        .withPendingBloodTest(pendingBloodTest)
        .build();
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditNotDoneBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditNoTypeDeterminedBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(false));
  }

  @Test
  public void testCanEditReleasedDonationBloodTypingTestResult() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicBloodTypingBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    bloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_RELEASED);

    assertThat(canEdit, is(false));
  }

  @Test
  public void testCanEditTTISafeWithNoPendingBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicTTIBloodTest().build();
    BloodTest anotherBloodTest = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest pendingBloodTest = aRepeatTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setTtiStatus(TTIStatus.SAFE);
    List<UUID> pendingTtiTestIds = new ArrayList<>();
    bloodTestingRuleResultSet.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingTtiTestIds);
    BloodTestingRule rule1 = aBloodTestingRule()
        .withBloodTest(anotherBloodTest)
        .withPendingBloodTest(pendingBloodTest)
        .build();
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);


    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditTTISafeWithPendingBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest pendingBloodTest = aRepeatTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    bloodTestResult.setResult("OUTCOME");
    Map<UUID, String> availableTestResults = new HashMap<UUID, String>();
    availableTestResults.put(bloodTest.getId(), "RESULT");
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), availableTestResults, new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());
    bloodTestingRuleResultSet.setTtiStatus(TTIStatus.SAFE);
    List<UUID> pendingTtiTestIds = new ArrayList<>();
    pendingTtiTestIds.add(pendingBloodTest.getId());
    bloodTestingRuleResultSet.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingTtiTestIds);
    BloodTestingRule rule1 = aBloodTestingRule()
        .withBloodTest(bloodTest)
        .withPendingBloodTest(pendingBloodTest)
        .build();
    List<BloodTestingRule> rules = new ArrayList<>();
    rules.add(rule1);
    bloodTestingRuleResultSet.setBloodTestingRules(rules);

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  @Test
  public void testCanEditTTISafeWithOtherPendingBloodTest() {
    Donation donation = aDonation().build();
    BloodTest bloodTest = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest anotherBloodTest = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest pendingBloodTest = aRepeatTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    bloodTestResult.setResult("OUTCOME");
    Map<UUID, String> availableTestResults = new HashMap<>();
    availableTestResults.put(bloodTest.getId(), "RESULT");
    availableTestResults.put(anotherBloodTest.getId(), "RESULT");
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), availableTestResults, new HashMap<UUID, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setTtiStatus(TTIStatus.SAFE);
    List<UUID> pendingTtiTestIds = new ArrayList<>();
    pendingTtiTestIds.add(pendingBloodTest.getId());
    bloodTestingRuleResultSet.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingTtiTestIds);
    BloodTestingRule rule1 = aBloodTestingRule()
        .withBloodTest(bloodTest)
        .withPendingBloodTest(anotherBloodTest)
        .build();
    BloodTestingRule rule2 = aBloodTestingRule()
        .withBloodTest(anotherBloodTest)
        .withPendingBloodTest(pendingBloodTest)
        .build();
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
    BloodTest bloodTest = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest anotherBloodTest = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest pendingBloodTest = aRepeatTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    bloodTestResult.setResult("OUTCOME");
    Map<UUID, String> availableTestResults = new HashMap<>();
    availableTestResults.put(bloodTest.getId(), "RESULT");
    availableTestResults.put(pendingBloodTest.getId(), "RESULT");
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), availableTestResults, new HashMap<UUID, BloodTestResult>(),
        generateTestBloodTestingRules());
    bloodTestingRuleResultSet.setTtiStatus(TTIStatus.SAFE);
    List<UUID> pendingTtiTestIds = new ArrayList<>();
    pendingTtiTestIds.add(anotherBloodTest.getId());
    bloodTestingRuleResultSet.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingTtiTestIds);
    BloodTestingRule rule1 = aBloodTestingRule()
        .withBloodTest(bloodTest)
        .withPendingBloodTest(anotherBloodTest)
        .build();
    BloodTestingRule rule2 = aBloodTestingRule()
        .withBloodTest(anotherBloodTest)
        .withPendingBloodTest(pendingBloodTest)
        .build();
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
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
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
    bloodTest.setId(UUID.randomUUID());
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setDonation(donation);
    bloodTestResult.setBloodTest(bloodTest);
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(),
        new ArrayList<BloodTestingRule>());

    boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, DONATION_NOT_RELEASED);

    assertThat(canEdit, is(true));
  }

  private List<BloodTestingRule> generateTestBloodTestingRules() {
    BloodTest test1 = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest test2 = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest test3 = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    BloodTest test4 = aBasicTTIBloodTest().withId(UUID.randomUUID()).build();
    List<BloodTestingRule> bloodTestingRules = new ArrayList<BloodTestingRule>();
    bloodTestingRules.add(aBloodTestingRule().withBloodTest(test1).build());
    bloodTestingRules.add(aBloodTestingRule().withBloodTest(test2).withPendingBloodTest(test3).build());
    bloodTestingRules.add(aBloodTestingRule().withBloodTest(test3).withPendingBloodTest(test4).build());
    bloodTestingRules.add(aBloodTestingRule().withBloodTest(test4).build());
    return bloodTestingRules;
  }
}
