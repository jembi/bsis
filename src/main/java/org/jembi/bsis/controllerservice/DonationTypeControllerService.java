package org.jembi.bsis.controllerservice;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.DonationTypeBackingForm;
import org.jembi.bsis.factory.DonationTypeFactory;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.repository.DonationTypeRepository;
import org.jembi.bsis.service.DonationTypeCRUDService;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DonationTypeControllerService {

  @Autowired
  DonationTypeCRUDService donationTypeCRUDService;

  @Autowired
  DonationTypeRepository donationTypeRepository;
  
  @Autowired
  DonationTypeFactory donationTypeFactory;

  public DonationTypeViewModel getDonationType(UUID id) {
    return donationTypeFactory.createViewModel(donationTypeRepository.getDonationTypeById(id));
  }

  public List<DonationTypeViewModel> getAllDonationTypes() {
    return donationTypeFactory.createViewModels(donationTypeRepository.getAllDonationTypes(true));
  }

  public DonationTypeViewModel updateDonationType(UUID id, DonationTypeBackingForm backingForm) {
    DonationType donationType = donationTypeFactory.createEntity(backingForm);
    donationType.setId(id);
    donationTypeCRUDService.updateDonationType(donationType);
    return donationTypeFactory.createViewModel(donationType);
  }

  public DonationTypeViewModel createDonationType(DonationTypeBackingForm backingForm) {
    DonationType donationType = donationTypeFactory.createEntity(backingForm);
    donationTypeRepository.save(donationType);
    return donationTypeFactory.createViewModel(donationType);
  }
}
