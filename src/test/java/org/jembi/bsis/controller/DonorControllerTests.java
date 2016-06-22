package org.jembi.bsis.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.DonorSummaryViewModelBuilder.aDonorSummaryViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.controller.DonorController;
import org.jembi.bsis.factory.DonorViewModelFactory;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonorSummaryViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonorControllerTests extends UnitTestSuite {

  @InjectMocks
  private DonorController donorController;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonorViewModelFactory donorViewModelFactory;
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;
  
  @Test
  @SuppressWarnings("unchecked")
  public void testFindDonorsWithDonorNumber_shouldFindDonorByDonorNumber() {
    // Set up
    String donorNumber = "000001";
    Donor donor = aDonor().withDonorNumber(donorNumber).build();
    List<DonorSummaryViewModel> donorViewModels = Arrays.asList(aDonorSummaryViewModel().withDonorNumber(donorNumber).build());
    
    when(donorRepository.findDonorByDonorNumber(donorNumber)).thenReturn(donor);
    when(donorViewModelFactory.createDonorSummaryViewModels(Arrays.asList(donor))).thenReturn(donorViewModels);
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DONOR_REGISTRATION_OPEN_BATCH_REQUIRED)).thenReturn(false);
    
    // Test
    Map<String, Object> result = donorController.findDonors("", "", donorNumber, false, null);
    
    // Verify
    assertThat((List<DonorSummaryViewModel>) result.get("donors"), equalTo(donorViewModels));
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testFindDonorsWithDonorNumberAndNoDonor_shouldReturnEmptyList() {
    // Set up
    String donorNumber = "000001";
    
    when(donorRepository.findDonorByDonorNumber(donorNumber)).thenThrow(NoResultException.class);
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DONOR_REGISTRATION_OPEN_BATCH_REQUIRED)).thenReturn(false);
    
    // Test
    Map<String, Object> result = donorController.findDonors("", "", donorNumber, false, null);
    
    // Verify
    assertThat((List<DonorSummaryViewModel>) result.get("donors"), equalTo(Collections.<DonorSummaryViewModel>emptyList()));
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testFindDonorsWithDonationIdentificationNumber_shouldFindDonorByDonationIdentificationNumber() {
    // Set up
    String donationIdentificationNumber = "1000001";
    Donor donor = aDonor().withId(2L).build();
    List<DonorSummaryViewModel> donorViewModels = Arrays.asList(aDonorSummaryViewModel().withId(2L).build());
    
    when(donorRepository.findDonorByDonationIdentificationNumber(donationIdentificationNumber)).thenReturn(donor);
    when(donorViewModelFactory.createDonorSummaryViewModels(Arrays.asList(donor))).thenReturn(donorViewModels);
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DONOR_REGISTRATION_OPEN_BATCH_REQUIRED)).thenReturn(false);
    
    // Test
    Map<String, Object> result = donorController.findDonors("", "", null, false, donationIdentificationNumber);
    
    // Verify
    assertThat((List<DonorSummaryViewModel>) result.get("donors"), equalTo(donorViewModels));
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testFindDonorsWithDonationIdentificationNumberAndNoDonor_shouldReturnEmptyList() {
    // Set up
    String donationIdentificationNumber = "1000001";
    
    when(donorRepository.findDonorByDonationIdentificationNumber(donationIdentificationNumber)).thenThrow(NoResultException.class);
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DONOR_REGISTRATION_OPEN_BATCH_REQUIRED)).thenReturn(false);
    
    // Test
    Map<String, Object> result = donorController.findDonors("", "", null, false, donationIdentificationNumber);
    
    // Verify
    assertThat((List<DonorSummaryViewModel>) result.get("donors"), equalTo(Collections.<DonorSummaryViewModel>emptyList()));
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testFindDonors_shouldFindAnyDonor() {
    // Set up
    String firstName = "first";
    String lastName = "last";
    boolean usePhraseMatch = true;
    Donor donor = aDonor().withFirstName(firstName).withLastName(lastName).build();
    List<DonorSummaryViewModel> donorViewModels = Arrays.asList(
        aDonorSummaryViewModel().withFirstName(firstName).withLastName(lastName).build()
    );
    
    when(donorRepository.findAnyDonor(firstName, lastName, usePhraseMatch)).thenReturn(Arrays.asList(donor));
    when(donorViewModelFactory.createDonorSummaryViewModels(Arrays.asList(donor))).thenReturn(donorViewModels);
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DONOR_REGISTRATION_OPEN_BATCH_REQUIRED)).thenReturn(false);
    
    // Test
    Map<String, Object> result = donorController.findDonors(firstName, lastName, null, usePhraseMatch, null);
    
    // Verify
    assertThat((List<DonorSummaryViewModel>) result.get("donors"), equalTo(donorViewModels));
  }
  
}
