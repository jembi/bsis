package controller;

import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.DonorSummaryViewModelBuilder.aDonorSummaryViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import constant.GeneralConfigConstants;
import factory.DonorViewModelFactory;
import model.donor.Donor;
import repository.DonorRepository;
import service.GeneralConfigAccessorService;
import suites.UnitTestSuite;
import viewmodel.DonorSummaryViewModel;

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
