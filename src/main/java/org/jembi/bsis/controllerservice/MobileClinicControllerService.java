package org.jembi.bsis.controllerservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.factory.DonorOutcomesViewModelFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.MobileClinicDonorFactory;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.viewmodel.DonorOutcomesViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileClinicControllerService {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationFactory locationFactory;

  @Autowired
  private MobileClinicDonorFactory mobileClinicDonorFactory;

  @Autowired
  private DonationRepository donationRepository;

  /*
   * @Autowired private MobileClinicExportDonorFactory mobileClinicExportDonorFactory;
   */

  @Autowired
  private DonorOutcomesViewModelFactory donorOutcomesViewModelFactory;

  @Autowired
  private BloodTestRepository bloodTestRepository;

  public List<LocationViewModel> getMobileVenues() {
    return locationFactory.createViewModels(locationRepository.getMobileVenues());
  }

  public List<MobileClinicLookUpDonorViewModel> getMobileClinicDonorsByVenue(UUID venueId, Date clinicDate) {
    List<MobileClinicDonorDTO> mobileClinicDonorDTOs =
        donorRepository.findMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venueId)));
    return mobileClinicDonorFactory.createMobileClinicDonorViewModels(mobileClinicDonorDTOs, clinicDate);
  }

  public List<MobileClinicExportDonorViewModel> getMobileClinicDonorsByVenues(Set<UUID> venueIds, Date clinicDate) {
    List<MobileClinicDonorDTO> mobileClinicDonorDTOs = donorRepository.findMobileClinicDonorsByVenues(venueIds);
    return mobileClinicDonorFactory.createMobileClinicExportDonorViewModels(mobileClinicDonorDTOs, clinicDate);
  }

  public List<DonorOutcomesViewModel> getDonorOutcomes(UUID venueId, Date startDate, Date endDate) {
    Location donorVenue = locationRepository.getLocation(venueId);
    List<Donation> donations =
        donationRepository.findLastDonationsByDonorVenueAndDonationDate(donorVenue, startDate, endDate);
    return donorOutcomesViewModelFactory.createDonorOutcomesViewModels(donations);
  }

  public List<String> getBloodTestNames() {
    List<String> bloodTestNames = new ArrayList<>();
    // Add basic TTI test names
    for (BloodTest bloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)) {
      bloodTestNames.add(bloodTest.getTestNameShort());
    }
    // Add Repeat TTI test names
    for(BloodTest bloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_TTI)) {
      bloodTestNames.add(bloodTest.getTestNameShort());
    }
    // Add confirmatory TTI test names
    for (BloodTest bloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)) {
      bloodTestNames.add(bloodTest.getTestNameShort());
    }
    return bloodTestNames;
  }

}
