package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.TestBatchBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.service.DonationBatchConstraintChecker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonationBatchConstraintCheckerTests {

  @InjectMocks
  private DonationBatchConstraintChecker donationBatchConstraintChecker;

  @Mock
  private DonationBatchRepository donationBatchRepository;

  @Test
  public void testCanDeleteDonationBatchNoDonations() {
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId);

    assertThat("Can delete a DonationBatch with no donations", canDelete, is(true));
  }

  @Test
  public void testCanDeleteDonationBatchNoDonationsEmptyList() {
    List<Donation> donations = new ArrayList<Donation>();
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId);

    assertThat("Can delete a DonationBatch with no donations", canDelete, is(true));
  }

  @Test
  public void testCanDeleteDonationBatchWithDonations() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(1l).build());
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId);

    assertThat("Cannot delete a DonationBatch with donations", canDelete, is(false));
  }

  @Test
  public void testCanEditClosedDonationBatch() {
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).thatIsClosed().build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(donationBatchId);

    assertThat("Cannot edit a closed DonationBatch", canEdit, is(false));
  }

  @Test
  public void testCanEditDeletedDonationBatch() {
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).thatIsDeleted().build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(donationBatchId);

    assertThat("Cannot edit a deleted DonationBatch", canEdit, is(false));
  }

  @Test
  public void testCanEditDonationBatch() {
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(donationBatchId);

    assertThat("Can edit this DonationBatch", canEdit, is(true));
  }

  @Test
  public void testCanEditDonationBatchDateWithDonations() {
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = aDonationBatch()
        .withId(donationBatchId)
        .withDonation(aDonation().build())
        .build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatchDate(donationBatchId);

    assertThat("Cannot edit this DonationBatch", canEdit, is(false));
  }

  @Test
  public void testCanEditDonationBatchDateWithoutDonations() {
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = aDonationBatch()
        .withId(donationBatchId)
        .withDonations(Collections.<Donation>emptyList())
        .build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canEdit = donationBatchConstraintChecker.canEditDonationBatchDate(donationBatchId);

    assertThat("Cannot edit this DonationBatch", canEdit, is(true));
  }

  @Test
  public void testCanCloseDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(1l).build());
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId);

    assertThat("Can close a DonationBatch with donations", canClose, is(true));
  }

  @Test
  public void testCanCloseClosedDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(1l).build());
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations)
        .thatIsClosed().build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId);

    assertThat("Cannot close a closed DonationBatch with donations", canClose, is(false));
  }

  @Test
  public void testCanCloseEmptyDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId);

    assertThat("Cannot close a DonationBatch with no donations", canClose, is(false));
  }

  @Test
  public void testCanCloseDonationBatchWithNoDonations() {
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId);

    assertThat("Cannot close a DonationBatch with no donations", canClose, is(false));
  }

  @Test
  public void testCanReopenDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(1l).build());
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations)
        .thatIsClosed().build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canReopen = donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId);

    assertThat("Can reopen a closed DonationBatch with no TestBatch", canReopen, is(true));
  }

  @Test
  public void testCanReopenDonationBatchWithTestBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(1l).build());
    TestBatch testBatch = new TestBatchBuilder().build();
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations)
        .withTestBatch(testBatch).thatIsClosed().build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canReopen = donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId);

    assertThat("Cannot reopen a closed DonationBatch with a TestBatch", canReopen, is(false));
  }

  @Test
  public void testCanReopenOpenDonationBatch() {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(new DonationBuilder().withId(1l).build());
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();

    when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);

    boolean canReopen = donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId);

    assertThat("Cannot reopen an open DonationBatch", canReopen, is(false));
  }
}
