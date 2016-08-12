package org.jembi.bsis.controllerservice;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.MobileClinicDonorViewModelFactory;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.viewmodel.LocationViewModel;
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
  private MobileClinicDonorViewModelFactory mobileClinicDonorViewModelFactory;

  public List<LocationViewModel> getVenues() {
    return locationFactory.createViewModels(locationRepository.getVenues());
  }

  public List<MobileClinicLookUpDonorViewModel> getMobileClinicDonors(Long venueId, Date clinicDate) {
    List<MobileClinicDonorDTO> mobileClinicDonorDTOs = donorRepository.findMobileClinicDonorsByVenue(venueId);
    return mobileClinicDonorViewModelFactory.createMobileClinicDonorViewModels(mobileClinicDonorDTOs, clinicDate);
  }

}
