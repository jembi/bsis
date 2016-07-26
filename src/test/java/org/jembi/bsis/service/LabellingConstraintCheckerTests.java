package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
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
    Component component = aComponent().withId(1L).withDonation(donation).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(aComponentType().withCanBeIssued(true).build()).build();
    
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
    Component component = aComponent().withId(1L).withDonation(donation).withStatus(ComponentStatus.QUARANTINED)
        .withComponentType(aComponentType().withCanBeIssued(true).build()).build();
    
    // Mocks
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)).thenReturn(false);
    
    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
    
    // Verify
    assertThat(canPrintPackLabel, is(false));
  }
  
  @Test
  public void testCanPrintDiscardLabelWithUnsafeComponentWithDonationNotReleased_shouldReturnFalse() {
    // Set up
    Donation donation = aDonation().thatIsNotReleased().build();
    Component component = aComponent().withId(1L).withDonation(donation).withStatus(ComponentStatus.UNSAFE).build();
    
    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);
    
    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }
  
  @Test
  public void testCanPrintDiscardLabelWithUnsafeComponentWithDonationReleased_shouldReturnTrue() {
    // Set up
    Donation donation = aDonation().thatIsReleased().build();
    Component component = aComponent().withId(1L).withDonation(donation).withStatus(ComponentStatus.UNSAFE).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(true));
  }

  @Test
  public void testCanPrintDiscardLabelWithExpiredComponent_shouldReturnTrue() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.EXPIRED).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(true));
  }

  @Test
  public void testCanPrintDiscardLabelWithDiscardedComponent_shouldReturnTrue() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.DISCARDED).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(true));
  }

  @Test
  public void testCanPrintDiscardLabelWithQuarantinedComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.QUARANTINED).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithIssuedComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.ISSUED).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithSplitComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.SPLIT).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithUsedComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.USED).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithProcessedComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithAvailableComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintPackLabelWithComponentThatCanBeIssued_shouldReturnTrue() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(aComponentType().withCanBeIssued(true).build()).build();

    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabel(component);

    // Verify
    assertThat(canPrintPackLabel, is(true));
  }
  
  @Test
  public void testCanPrintPackLabelWithComponentThatCantBeIssued_shouldReturnFalse() {
    // Set up
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(aComponentType().withCanBeIssued(false).build()).build();

    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabel(component);

    // Verify
    assertThat(canPrintPackLabel, is(false));
  }

}
