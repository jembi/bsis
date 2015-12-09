package service;

import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import helpers.builders.DonationBatchBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.TestBatchBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.DonationBatchRepository;

@RunWith(MockitoJUnitRunner.class)
public class DonationBatchConstraintCheckerTests {
	
	@InjectMocks
	private DonationBatchConstraintChecker donationBatchConstraintChecker;
	
	@Mock
	private DonationBatchRepository donationBatchRepository;
	
	@Test
	public void testCanDeleteDonationBatchNoDonations() {
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId);
		
		assertThat("Can delete a DonationBatch with no donations", canDelete, is(true));
	}
	
	@Test
	public void testCanDeleteDonationBatchNoDonationsEmptyList() {
		List<Donation> donations = new ArrayList<>();
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId);
		
		assertThat("Can delete a DonationBatch with no donations", canDelete, is(true));
	}
	
	@Test
	public void testCanDeleteDonationBatchWithDonations() {
		List<Donation> donations = new ArrayList<>();
		donations.add(new DonationBuilder().withId(1l).build());
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canDelete = donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId);
		
		assertThat("Cannot delete a DonationBatch with donations", canDelete, is(false));
	}
	
	@Test
	public void testCanEditClosedDonationBatch() {
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).thatIsClosed().build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(donationBatchId);
		
		assertThat("Cannot edit a closed DonationBatch", canEdit, is(false));
	}
	
	@Test
	public void testCanEditDeletedDonationBatch() {
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).thatIsDeleted().build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(donationBatchId);
		
		assertThat("Cannot edit a deleted DonationBatch", canEdit, is(false));
	}
	
	@Test
	public void testCanEditDonationBatch() {
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canEdit = donationBatchConstraintChecker.canEditDonationBatch(donationBatchId);
		
		assertThat("Can edit this DonationBatch", canEdit, is(true));
	}
	
	@Test
	public void testCanEditDonationBatchDateWithDonations() {
		int donationBatchId = 1;
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
		int donationBatchId = 1;
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
		List<Donation> donations = new ArrayList<>();
		donations.add(new DonationBuilder().withId(1l).build());
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId);
		
		assertThat("Can close a DonationBatch with donations", canClose, is(true));
	}
	
	@Test
	public void testCanCloseClosedDonationBatch() {
		List<Donation> donations = new ArrayList<>();
		donations.add(new DonationBuilder().withId(1l).build());
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations)
		        .thatIsClosed().build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId);
		
		assertThat("Cannot close a closed DonationBatch with donations", canClose, is(false));
	}
	
	@Test
	public void testCanCloseEmptyDonationBatch() {
		List<Donation> donations = new ArrayList<>();
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId);
		
		assertThat("Cannot close a DonationBatch with no donations", canClose, is(false));
	}
	
	@Test
	public void testCanCloseDonationBatchWithNoDonations() {
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canClose = donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId);
		
		assertThat("Cannot close a DonationBatch with no donations", canClose, is(false));
	}
	
	@Test
	public void testCanReopenDonationBatch() {
		List<Donation> donations = new ArrayList<>();
		donations.add(new DonationBuilder().withId(1l).build());
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations)
		        .thatIsClosed().build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canReopen = donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId);
		
		assertThat("Can reopen a closed DonationBatch with no TestBatch", canReopen, is(true));
	}
	
	@Test
	public void testCanReopenDonationBatchWithTestBatch() {
		List<Donation> donations = new ArrayList<>();
		donations.add(new DonationBuilder().withId(1l).build());
		TestBatch testBatch = new TestBatchBuilder().build();
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations)
		        .withTestBatch(testBatch).thatIsClosed().build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canReopen = donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId);
		
		assertThat("Cannot reopen a closed DonationBatch with a TestBatch", canReopen, is(false));
	}
	
	@Test
	public void testCanReopenOpenDonationBatch() {
		List<Donation> donations = new ArrayList<>();
		donations.add(new DonationBuilder().withId(1l).build());
		Integer donationBatchId = new Integer(1);
		DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();
		
		when(donationBatchRepository.findDonationBatchById(donationBatchId)).thenReturn(donationBatch);
		
		boolean canReopen = donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId);
		
		assertThat("Cannot reopen an open DonationBatch", canReopen, is(false));
	}
}
