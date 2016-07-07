package org.jembi.bsis.service;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabellingConstraintChecker {
  
  @Autowired
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
  
  public boolean canPrintPackLabel(Component component) {
    return component.getStatus() == ComponentStatus.AVAILABLE;
  }
  
  public boolean canPrintPackLabelWithConsistencyChecks(Component component) {
    
    Donation donation = component.getDonation();
    
    // Check that the donation is safe
    if (donation.getTTIStatus() != TTIStatus.TTI_SAFE) {
      throw new IllegalStateException("Can't label component " + component.getId() + ": TTI status is not safe for donation "
          + donation.getId());
    }
    
    // Check that the donation is released
    if (!donation.isReleased()) {
      throw new IllegalStateException("Can't label component " + component.getId() + ": Donation " + donation.getId()
          + " has not been released.");
    }
    
    Donor donor = donation.getDonor();
    
    // Check that the donor has a blood group
    if (StringUtils.isEmpty(donor.getBloodAbo()) || StringUtils.isEmpty(donor.getBloodRh())) {
      throw new IllegalStateException("Can't label component " + component.getId() + ": No blood group for donor "
          + donor.getId());
    }
    
    // Check that the donor and donation blood groups match
    if (!donor.getBloodAbo().equals(donation.getBloodAbo()) || !donor.getBloodRh().equals(donation.getBloodRh())) {
      throw new IllegalStateException("Can't label component " + component.getId() + ": Blood groups don't match for donor "
          + donor.getId() + " and donation " + donation.getId());
    }
    
    // Check that the donor is not deferred
    if (donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)) {
      throw new IllegalStateException("Can't label component " + component.getId() + ": Donor " + donor.getId()
          + " is currently deferred");
    }
    
    return canPrintPackLabel(component);
  }

}
