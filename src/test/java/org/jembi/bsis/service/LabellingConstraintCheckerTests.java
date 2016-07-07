package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.mockito.Mockito.when;

import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class LabellingConstraintCheckerTests extends UnitTestSuite {
  
  @InjectMocks
  private LabellingConstraintChecker labellingConstraintChecker;
  @Mock
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithUnsafeDonation_shouldThrow() {
    // Set up
    Donation unsafeDonation = aDonation().withId(1L).withTTIStatus(TTIStatus.TTI_UNSAFE).build();
    Component component = aComponent().withId(1L).withDonation(unsafeDonation).build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithUnreleasedDonation_shouldThrow() {
    // Set up
    Donation unreleasedDonation = aDonation().withId(1L).withTTIStatus(TTIStatus.TTI_SAFE).thatIsNotReleased().build();
    Component component = aComponent().withId(1L).withDonation(unreleasedDonation).build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithDonorWithNoBloodAbo_shouldThrow() {
    // Set up
    Donation donation = aDonation()
        .withId(1L)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .withDonor(aDonor().withId(1L).withBloodAbo(null).withBloodRh("+").build())
        .build();
    Component component = aComponent().withId(1L).withDonation(donation).build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithDonorWithNoBloodRh_shouldThrow() {
    // Set up
    Donation donation = aDonation()
        .withId(1L)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .withDonor(aDonor().withId(1L).withBloodAbo("A").withBloodRh(null).build())
        .build();
    Component component = aComponent().withId(1L).withDonation(donation).build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithNonMatchingBloodAbo_shouldThrow() {
    // Set up
    Donation donation = aDonation()
        .withId(1L)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .withDonor(aDonor().withId(1L).withBloodAbo("B").withBloodRh("+").build())
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent().withId(1L).withDonation(donation).build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithNonMatchingBloodRh_shouldThrow() {
    // Set up
    Donation donation = aDonation()
        .withId(1L)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .withDonor(aDonor().withId(1L).withBloodAbo("A").withBloodRh("-").build())
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent().withId(1L).withDonation(donation).build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithDeferredDonor_shouldThrow() {
    // Set up
    Donor deferredDonor = aDonor().withId(1L).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation()
        .withId(1L)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .withDonor(deferredDonor)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent().withId(1L).withDonation(donation).build();
    
    // Mocks
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(deferredDonor)).thenReturn(true);
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test
  public void testCanPrintPackLabelWithConsistencyChecksWithAvailableComponent_shouldReturnTrue() {
    // Set up
    Donor donor = aDonor().withId(1L).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation()
        .withId(1L)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .withDonor(donor)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent().withId(1L).withDonation(donation).withStatus(ComponentStatus.AVAILABLE).build();
    
    // Mocks
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)).thenReturn(false);
    
    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
    
    // Verify
    assertThat(canPrintPackLabel, is(true));
  }
  
  @Test
  public void testCanPrintPackLabelWithConsistencyChecksWithUnavailableComponent_shouldReturnFalse() {
    // Set up
    Donor donor = aDonor().withId(1L).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation()
        .withId(1L)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .withDonor(donor)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent().withId(1L).withDonation(donation).withStatus(ComponentStatus.QUARANTINED).build();
    
    // Mocks
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)).thenReturn(false);
    
    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
    
    // Verify
    assertThat(canPrintPackLabel, is(false));
  }

}
