package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonationBatchConstraintCheckerTests extends UnitTestSuite {

  private static final UUID DONATION_BATCH_ID = UUID.randomUUID();
  private static final UUID DONATION_ID = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");

  @InjectMocks
  private DonationBatchConstraintChecker donationBatchConstraintChecker;

  @Mock
  private DonationBatchRepository donationBatchRepository;

  @Test
  public void testCanDeleteDonationBatchNoDonations() {
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(DONATION_BATCH_ID);

    assertThat("Can delete a DonationBatch with no donations", canDelete, is(true));
  }

  @Test
  public void testCanDeleteDonationBatchNoDonationsEmptyList() {
    List<Donation> donations = new ArrayList<Donation>();
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(DONATION_BATCH_ID);

    assertThat("Can delete a DonationBatch with no donations", canDelete, is(true));
  }

  @Test
  public void testCanDeleteDonationBatchWithDonations() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(DONATION_ID).build());
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(DONATION_BATCH_ID);

    assertThat("Cannot delete a DonationBatch with donations", canDelete, is(false));
  }

  @Test
  public void testCanEditClosedDonationBatch() {
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).thatIsClosed().build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(DONATION_BATCH_ID);

    assertThat("Cannot edit a closed DonationBatch", canEdit, is(false));
  }

  @Test
  public void testCanEditDeletedDonationBatch() {
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).thatIsDeleted().build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(DONATION_BATCH_ID);

    assertThat("Cannot edit a deleted DonationBatch", canEdit, is(false));
  }

  @Test
  public void testCanEditDonationBatch() {
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(DONATION_BATCH_ID);

    assertThat("Can edit this DonationBatch", canEdit, is(true));
  }

  @Test
  public void testCanEditDonationBatchDateWithDonations() {
    DonationBatch donationBatch = aDonationBatch()
.withId(DONATION_BATCH_ID)
        .withDonation(aDonation().build())
        .build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatchDate(DONATION_BATCH_ID);

    assertThat("Cannot edit this DonationBatch", canEdit, is(false));
  }

  @Test
  public void testCanEditDonationBatchDateWithoutDonations() {
    DonationBatch donationBatch = aDonationBatch()
.withId(DONATION_BATCH_ID)
        .withDonations(Collections.<Donation>emptyList())
        .build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatchDate(DONATION_BATCH_ID);

    assertThat("Cannot edit this DonationBatch", canEdit, is(true));
  }

  @Test
  public void testCanCloseDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(DONATION_ID).build());
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(DONATION_BATCH_ID);

    assertThat("Can close a DonationBatch with donations", canClose, is(true));
  }

  @Test
  public void testCanCloseClosedDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(DONATION_ID).build());
    DonationBatch donationBatch =
        new DonationBatchBuilder().withId(DONATION_BATCH_ID).withDonations(donations)
        .thatIsClosed().build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(DONATION_BATCH_ID);

    assertThat("Cannot close a closed DonationBatch with donations", canClose, is(false));
  }

  @Test
  public void testCanCloseEmptyDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(DONATION_BATCH_ID);

    assertThat("Cannot close a DonationBatch with no donations", canClose, is(false));
  }

  @Test
  public void testCanCloseDonationBatchWithNoDonations() {
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(DONATION_BATCH_ID);

    assertThat("Cannot close a DonationBatch with no donations", canClose, is(false));
  }

  @Test
  public void testCanReopenDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(DONATION_ID).build());
    DonationBatch donationBatch =
        new DonationBatchBuilder().withId(DONATION_BATCH_ID).withDonations(donations)
        .thatIsClosed().build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canReopen = donationBatchConstraintChecker.canReopenDonationBatch(DONATION_BATCH_ID);

    assertThat("Can reopen a closed DonationBatch with no TestBatch", canReopen, is(true));
  }

  @Test
  public void testCanReopenOpenDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(DONATION_ID).build());
    DonationBatch donationBatch = new DonationBatchBuilder().withId(DONATION_BATCH_ID).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID)).thenReturn(donationBatch);

    boolean canReopen = donationBatchConstraintChecker.canReopenDonationBatch(DONATION_BATCH_ID);

    assertThat("Cannot reopen an open DonationBatch", canReopen, is(false));
  }
}
