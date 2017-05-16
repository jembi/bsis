package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class LabellingConstraintCheckerTests extends UnitTestSuite {
  private static final UUID DONATION_ID = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");
  private static final UUID COMPONENT_BATCH_ID = UUID.randomUUID();
  private static final UUID COMPONENT_ID = UUID.randomUUID();
  @InjectMocks
  private LabellingConstraintChecker labellingConstraintChecker;
  @Mock
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithUnsafeDonation_shouldThrow() {
    // Set up
    Donation unsafeDonation = aDonation().withId(DONATION_ID).withTTIStatus(TTIStatus.UNSAFE).build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(unsafeDonation)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithUnreleasedDonation_shouldThrow() {
    // Set up
    Donation unreleasedDonation = aDonation().withId(DONATION_ID).withTTIStatus(TTIStatus.SAFE).thatIsNotReleased().build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(unreleasedDonation)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithDonorWithNoBloodAbo_shouldThrow() {
    // Set up
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .withDonor(aDonor().withId(UUID.randomUUID()).withBloodAbo(null).withBloodRh("+").build())
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithDonorWithNoBloodRh_shouldThrow() {
    // Set up
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .withDonor(aDonor().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh(null).build())
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithNonMatchingBloodAbo_shouldThrow() {
    // Set up
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .withDonor(aDonor().withId(UUID.randomUUID()).withBloodAbo("B").withBloodRh("+").build())
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithNonMatchingBloodRh_shouldThrow() {
    // Set up
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .withDonor(aDonor().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh("-").build())
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCanPrintPackLabelWithConsistencyChecksWithDeferredDonor_shouldThrow() {
    // Set up
    Donor deferredDonor = aDonor().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .withDonor(deferredDonor)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Mocks
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(deferredDonor.getId())).thenReturn(true);
    
    // Exercise SUT
    labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
  }
  
  @Test
  public void testCanPrintPackLabelWithConsistencyChecksWithAvailableComponent_shouldReturnTrue() {
    // Set up
    Donor donor = aDonor().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .withDonor(donor)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID).withDonation(donation)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .withComponentType(aComponentType()
            .thatCanBeIssued()
            .build())
        .build();
    
    // Mocks
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor.getId())).thenReturn(false);
    
    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
    
    // Verify
    assertThat(canPrintPackLabel, is(true));
  }
  
  @Test
  public void testCanPrintPackLabelWithConsistencyChecksWithUnavailableComponent_shouldReturnFalse() {
    // Set up
    Donor donor = aDonor().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .withDonor(donor)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withStatus(ComponentStatus.QUARANTINED)
        .withComponentType(aComponentType()
            .thatCanBeIssued()
            .build())
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Mocks
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor.getId())).thenReturn(false);
    
    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
    
    // Verify
    assertThat(canPrintPackLabel, is(false));
  }
  
  @Test
  public void testCanPrintDiscardLabelWithUnsafeComponentWithDonationNotReleased_shouldReturnFalse() {
    // Set up
    Donation donation = aDonation().thatIsNotReleased().build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withStatus(ComponentStatus.UNSAFE)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();
    
    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);
    
    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }
  
  @Test
  public void testCanPrintDiscardLabelWithUnsafeComponentWithDonationReleased_shouldReturnTrue() {
    // Set up
    Donation donation = aDonation().thatIsReleased().build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withStatus(ComponentStatus.UNSAFE)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(true));
  }

  @Test
  public void testCanPrintDiscardLabelWithExpiredComponent_shouldReturnTrue() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.EXPIRED)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(true));
  }

  @Test
  public void testCanPrintDiscardLabelWithDiscardedComponent_shouldReturnTrue() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.DISCARDED)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(true));
  }

  @Test
  public void testCanPrintDiscardLabelWithQuarantinedComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithIssuedComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.ISSUED)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithUsedComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.TRANSFUSED)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithProcessedComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.PROCESSED)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithAvailableComponent_shouldReturnFalse() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

  @Test
  public void testCanPrintPackLabelWithComponentThatCanBeIssued_shouldReturnTrue() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .withComponentType(aComponentType()
            .thatCanBeIssued()
            .build())
        .build();

    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabel(component);

    // Verify
    assertThat(canPrintPackLabel, is(true));
  }
  
  @Test
  public void testCanPrintPackLabelWithComponentThatCantBeIssued_shouldReturnFalse() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch()
            .withId(COMPONENT_BATCH_ID)
            .build())
        .withComponentType(aComponentType()
            .thatCanNotBeIssued()
            .build())
        .build();

    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabel(component);

    // Verify
    assertThat(canPrintPackLabel, is(false));
  }

  @Test
  public void testCanPrintPackLabelWithComponentThatHasNoComponentBatch_shouldReturnFalse() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(aComponentType()
            .thatCanBeIssued()
            .build())
        .build();

    // Exercise SUT
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabel(component);

    // Verify
    assertThat(canPrintPackLabel, is(false));
  }

  @Test
  public void testCanPrintDiscardLabelWithComponentThatHasNoComponentBatch_shouldReturnFalse() {
    // Set up
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.EXPIRED)
        .withComponentType(aComponentType()
            .thatCanNotBeIssued()
            .build())
        .build();

    // Exercise SUT
    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // Verify
    assertThat(canPrintDiscardLabel, is(false));
  }

}
