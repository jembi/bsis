package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.PostDonationCounsellingFactory;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.PostDonationCounsellingCRUDService;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostDonationCounsellingControllerService {
  
  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private LocationFactory locationFactory;
  @Autowired
  private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
  @Autowired
  private PostDonationCounsellingFactory postDonationCounsellingFactory;
  
  public List<LocationViewModel> getVenues() {
    List<Location> venues = locationRepository.getVenues();
    return locationFactory.createViewModels(venues);
  }

  public PostDonationCounsellingViewModel update(PostDonationCounsellingBackingForm backingForm) {
    
    PostDonationCounselling postDonationCounselling = postDonationCounsellingFactory.createEntity(backingForm);

    PostDonationCounselling updatedPostDonationCounselling = postDonationCounsellingCRUDService
        .updatePostDonationCounselling(postDonationCounselling);

    return postDonationCounsellingFactory.createViewModel(updatedPostDonationCounselling);
  }

  public List<LocationViewModel> getReferralSites() {
    return locationFactory.createViewModels(locationRepository.getReferralSites());
  }
}