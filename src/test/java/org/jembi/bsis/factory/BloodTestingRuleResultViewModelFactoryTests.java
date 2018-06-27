package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleResultMatcher.hasSameStateAsBloodTestingRuleResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRuleResultSet;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.service.DonationConstraintChecker;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodTestingRuleResultViewModelFactoryTests extends UnitTestSuite {

  private static final UUID DONATION_ID = UUID.randomUUID();

  @InjectMocks
  BloodTestingRuleResultViewModelFactory bloodTestingRuleResultViewModelFactory;

  @Mock
  private DonationFactory donationFactory;

  @Mock
  private DonationConstraintChecker donationConstraintChecker;

  @Test
  public void testCreateBloodTestResultFullViewModelWithHighTitre_shouldCreateCorrectResultSet()
      throws Exception {
    // Setup data
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(), 
        new ArrayList<BloodTestingRule>());

    bloodTestingRuleResultSet.addTitreChanges("HIGH");

    BloodTestingRuleResult expectedRuleResult = aBloodTestingRuleResult().withTitre(Titre.HIGH).build();
    
    // Run test
    BloodTestingRuleResult ruleResult =
        bloodTestingRuleResultViewModelFactory.createBloodTestResultFullViewModel(bloodTestingRuleResultSet);

    // Verify result
    assertThat(ruleResult, is(hasSameStateAsBloodTestingRuleResult(expectedRuleResult)));
  }

  @Test
  public void testCreateBloodTestResultFullViewModelWithLowTitre_shouldCreateCorrectResultSet()
      throws Exception {
    // Setup data
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(), 
        new ArrayList<BloodTestingRule>());

    bloodTestingRuleResultSet.addTitreChanges("LOW");

    BloodTestingRuleResult expectedRuleResult = aBloodTestingRuleResult().withTitre(Titre.LOW).build();
    
    // Run test
    BloodTestingRuleResult ruleResult =
        bloodTestingRuleResultViewModelFactory.createBloodTestResultFullViewModel(bloodTestingRuleResultSet);

    // Verify result
    assertThat(ruleResult, is(hasSameStateAsBloodTestingRuleResult(expectedRuleResult)));
  }

  @Test
  public void testCreateBloodTestResultFullViewModelWithNTTitre_shouldCreateCorrectResultSet()
      throws Exception {
    // Setup data
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(), 
        new ArrayList<BloodTestingRule>());

    bloodTestingRuleResultSet.addTitreChanges("");

    BloodTestingRuleResult expectedRuleResult = aBloodTestingRuleResult().build();
    
    // Run test
    BloodTestingRuleResult ruleResult =
        bloodTestingRuleResultViewModelFactory.createBloodTestResultFullViewModel(bloodTestingRuleResultSet);

    // Verify result
    assertThat(ruleResult, is(hasSameStateAsBloodTestingRuleResult(expectedRuleResult)));
  }

  @Test
  public void testCreateBloodTestResultFullViewModelWithUpdatedTitre_shouldCreateCorrectResultSet()
      throws Exception {
    // Setup data
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withTitre(Titre.HIGH).build();
    
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(), 
        new ArrayList<BloodTestingRule>());

    bloodTestingRuleResultSet.addTitreChanges("LOW");

    BloodTestingRuleResult expectedRuleResult = aBloodTestingRuleResult().withTitre(Titre.LOW).build();
    
    // Run test
    BloodTestingRuleResult ruleResult =
        bloodTestingRuleResultViewModelFactory.createBloodTestResultFullViewModel(bloodTestingRuleResultSet);

    // Verify result
    assertThat(ruleResult, is(hasSameStateAsBloodTestingRuleResult(expectedRuleResult)));
  }
  
  @Test
  public void testCreateBloodTestResultFullViewModelWithUpdatedNTTitre_shouldCreateCorrectResultSet()
      throws Exception {
    // Setup data
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withTitre(Titre.HIGH).build();
    
    BloodTestingRuleResultSet bloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), new HashMap<UUID, BloodTestResult>(), 
        new ArrayList<BloodTestingRule>());

    bloodTestingRuleResultSet.addTitreChanges("");

    BloodTestingRuleResult expectedRuleResult = aBloodTestingRuleResult().withTitre(null).build();
    
    // Run test
    BloodTestingRuleResult ruleResult =
        bloodTestingRuleResultViewModelFactory.createBloodTestResultFullViewModel(bloodTestingRuleResultSet);

    // Verify result
    assertThat(ruleResult, is(hasSameStateAsBloodTestingRuleResult(expectedRuleResult)));
  }
}
