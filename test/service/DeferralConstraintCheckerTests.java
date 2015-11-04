package service;

import static org.mockito.Mockito.when;
import helpers.builders.DeferralReasonBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.DonorDeferralBuilder;

import java.util.Date;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.DonorDeferralRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeferralConstraintCheckerTests {
	
	@InjectMocks
	private DeferralConstraintChecker deferralConstraintChecker;
	
	@Mock
	private DonorDeferralRepository deferralRepository;
	
	@Test
    public void testCanDeleteDonorDeferralTrue() {
    	// create test data
		Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
		DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT).withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
		DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(new Date())
		        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();
		
		// set up mocks
		when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);
		
		// run test
    	boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1l);
    	
    	// do asserts
    	Assert.assertFalse("Can not delete an automatic deferral", result);
    }
	
	@Test
    public void testCanDeleteDonorDeferralTemporaryFalse() {
    	// create test data
		Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
		DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY).withType(DeferralReasonType.NORMAL).build();
		DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(new Date())
		        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();
		
		// set up mocks
		when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);
		
		// run test
    	boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1l);
    	
    	// do asserts
    	Assert.assertTrue("Can delete this temporary deferral", result);
    }
	
	@Test
    public void testCanDeleteDonorDeferralPermanentFalse() {
    	// create test data
		Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
		DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT).withType(DeferralReasonType.NORMAL).build();
		DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(new Date())
		        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();
		
		// set up mocks
		when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);
		
		// run test
    	boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1l);
    	
    	// do asserts
    	Assert.assertTrue("Can delete this permanent deferral", result);
    }
}
