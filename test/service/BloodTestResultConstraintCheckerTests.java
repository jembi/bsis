package service;

import static helpers.builders.DonationBuilder.aDonation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import repository.bloodtesting.BloodTestingRuleResultSet;
import repository.bloodtesting.BloodTypingStatus;

@RunWith(MockitoJUnitRunner.class)
public class BloodTestResultConstraintCheckerTests {

    @InjectMocks
    private BloodTestResultConstraintChecker bloodTestResultConstraintChecker;
    
    @Test
    public void testCanEditCompleteBloodTest() {
        Donation donation = aDonation().withBloodTyingStatus(BloodTypingStatus.COMPLETE).build();
        BloodTest bloodTest = new BloodTest();
        bloodTest.setCategory(BloodTestCategory.BLOODTYPING);
        BloodTestResult bloodTestResult = new BloodTestResult();
        bloodTestResult.setDonation(donation);
        bloodTestResult.setBloodTest(bloodTest);
        BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation, new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Integer, BloodTestResult>());
        
        boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult);
        
        assertThat(canEdit, is(false));
    }
    
    @Test
    public void testCanEditNotDoneBloodTest() {
        Donation donation = aDonation().withTTIStatus(TTIStatus.NOT_DONE).build();
        BloodTest bloodTest = new BloodTest();
        bloodTest.setCategory(BloodTestCategory.TTI);
        BloodTestResult bloodTestResult = new BloodTestResult();
        bloodTestResult.setDonation(donation);
        bloodTestResult.setBloodTest(bloodTest);
        BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation, new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Integer, BloodTestResult>());
        
        boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult);
        
        assertThat(canEdit, is(true));
    }
    
    @Test
    public void testCanEditTTISafeWithNoPendingBloodTest() {
        Donation donation = aDonation().withTTIStatus(TTIStatus.TTI_SAFE).build();
        BloodTest bloodTest = new BloodTest();
        bloodTest.setCategory(BloodTestCategory.TTI);
        BloodTestResult bloodTestResult = new BloodTestResult();
        bloodTestResult.setDonation(donation);
        bloodTestResult.setBloodTest(bloodTest);
        BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation, new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Integer, BloodTestResult>());
        List<String> pendingTtiTestIds = new ArrayList<String>();
        bloodTestingRuleResultSet.setPendingTtiTestsIds(pendingTtiTestIds);
        
        boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult);
        
        assertThat(canEdit, is(false));
    }
    
    @Test
    public void testCanEditTTISafeWithPendingBloodTest() {
        Donation donation = aDonation().withTTIStatus(TTIStatus.TTI_SAFE).build();
        BloodTest bloodTest = new BloodTest();
        bloodTest.setCategory(BloodTestCategory.TTI);
        bloodTest.setId(1);
        BloodTestResult bloodTestResult = new BloodTestResult();
        bloodTestResult.setDonation(donation);
        bloodTestResult.setBloodTest(bloodTest);
        BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation, new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Integer, BloodTestResult>());
        List<String> pendingTtiTestIds = new ArrayList<String>();
        pendingTtiTestIds.add("123");
        bloodTestingRuleResultSet.setPendingTtiTestsIds(pendingTtiTestIds);
        bloodTestingRuleResultSet.addPendingTtiTest("1", "123");
        
        boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult);
        
        assertThat(canEdit, is(true));
    }
    
    @Test
    public void testCanEditTTISafeWithOtherPendingBloodTest() {
        Donation donation = aDonation().withTTIStatus(TTIStatus.TTI_SAFE).build();
        BloodTest bloodTest = new BloodTest();
        bloodTest.setCategory(BloodTestCategory.TTI);
        bloodTest.setId(1);
        BloodTestResult bloodTestResult = new BloodTestResult();
        bloodTestResult.setDonation(donation);
        bloodTestResult.setBloodTest(bloodTest);
        BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation, new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Integer, BloodTestResult>());
        List<String> pendingTtiTestIds = new ArrayList<String>();
        pendingTtiTestIds.add("123");
        bloodTestingRuleResultSet.setPendingTtiTestsIds(pendingTtiTestIds);
        bloodTestingRuleResultSet.addPendingTtiTest("2", "123");
        
        boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult);
        
        assertThat(canEdit, is(false));
    }
    
    @Test
    public void testCanEditTTISafeWithOtherPendingBloodTest2() {
        Donation donation = aDonation().withTTIStatus(TTIStatus.TTI_SAFE).build();
        BloodTest bloodTest = new BloodTest();
        bloodTest.setCategory(BloodTestCategory.TTI);
        bloodTest.setId(1);
        BloodTestResult bloodTestResult = new BloodTestResult();
        bloodTestResult.setDonation(donation);
        bloodTestResult.setBloodTest(bloodTest);
        BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation, new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Integer, BloodTestResult>());
        List<String> pendingTtiTestIds = new ArrayList<String>();
        pendingTtiTestIds.add("123");
        bloodTestingRuleResultSet.setPendingTtiTestsIds(pendingTtiTestIds);
        bloodTestingRuleResultSet.getPendingTtiTests().put(1, new ArrayList<Integer>());
        
        boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult);
        
        assertThat(canEdit, is(false));
    }
    
    @Test
    public void testCanEditUnknownTestCategory() {
        Donation donation = aDonation().build();
        BloodTest bloodTest = new BloodTest();
        bloodTest.setCategory(null);
        bloodTest.setId(1);
        BloodTestResult bloodTestResult = new BloodTestResult();
        bloodTestResult.setDonation(donation);
        bloodTestResult.setBloodTest(bloodTest);
        BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation, new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<Integer, BloodTestResult>());
        
        boolean canEdit = bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult);
        
        assertThat(canEdit, is(true));
    }
}
