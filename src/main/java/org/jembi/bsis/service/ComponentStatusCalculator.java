package org.jembi.bsis.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentStatusCalculator {
  
  @Autowired
  private DonationRepository donationRepository;

  public boolean shouldComponentsBeDiscarded(List<BloodTestResult> bloodTestResults) {

    for (BloodTestResult bloodTestResult : bloodTestResults) {

      BloodTest bloodTest = bloodTestResult.getBloodTest();
      if (!bloodTest.isFlagComponentsForDiscard()) {
        // This blood test does not flag components for discard
        continue;
      }

      List<String> positiveBloodTestResults = Arrays.asList(bloodTest.getPositiveResults().split(","));
      if (positiveBloodTestResults.contains(bloodTestResult.getResult())) {
        // The blood test result is positive and it flags components for discard
        return true;
      }
    }

    // There are no positive blood tests which flag components for discard
    return false;
  }
  
  /**
   * Determines if the Component should be flagged for discard.
   * 
   * Reasons:
   *  - initial component weight not between the PackType min and max weight
   * 
   * @param component
   * @return
   */
  public boolean shouldComponentBeDiscarded(Component component) {
    if (component.getParentComponent() == null && component.getWeight() != null) {
      PackType packType = component.getDonation().getPackType();
      if (packType.getMinWeight() == null || packType.getMaxWeight() == null) {
        throw new IllegalStateException("PackType does not have a min and max weight specified: " + packType);
      }
      Integer weight = component.getWeight();
      if (weight > packType.getMaxWeight() || weight < packType.getMinWeight()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Updates the status of a Component. 
   * 
   * This method looks at the existing status of the Component, the Donation's 
   * TTI and Blood Group statuses and the TestBatch status in order to determine 
   * a new status for the Component.
   * 
   * These statuses will never be updated, as they are Component end statuses:
   *  - ComponentStatus.DISCARDED, 
   *   - ComponentStatus.ISSUED,
   *   - ComponentStatus.USED,  
   *   - ComponentStatus.SPLIT, 
   *   - ComponentStatus.PROCESSED
   *   
   * Also if a Component with an existing status of UNSAFE will never
   * be taken out of that status even if the TTI status of the Donation
   * has been changed to SAFE.
   * 
   * @param component
   * @return
   */
  public boolean updateComponentStatus(Component component) {

    List<ComponentStatus> statusNotToBeChanged =
        Arrays.asList(ComponentStatus.DISCARDED, ComponentStatus.ISSUED,
            ComponentStatus.USED, ComponentStatus.SPLIT, ComponentStatus.PROCESSED, ComponentStatus.UNSAFE);

    ComponentStatus oldComponentStatus = component.getStatus();

    // nothing to do if the component has any of these statuses
    if (oldComponentStatus != null && statusNotToBeChanged.contains(oldComponentStatus)) {
      return false;
    }

    Long donationId = component.getDonation().getId();
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTypingStatus bloodTypingStatus = donation.getBloodTypingStatus();
    TTIStatus ttiStatus = donation.getTTIStatus();

    // Start with the old status if there is one.
    ComponentStatus newComponentStatus = oldComponentStatus == null ? ComponentStatus.QUARANTINED : oldComponentStatus;

    // Conditions for AVAILABLE status:
    // 1. Donation is released
    // 2. Blood typing is complete
    // 3. Blood group is confirmed
    // 4. TTI status is safe
    if (donation.isReleased() &&
        bloodTypingStatus.equals(BloodTypingStatus.COMPLETE) &&
        BloodTypingMatchStatus.isBloodGroupConfirmed(donation.getBloodTypingMatchStatus()) &&
        ttiStatus.equals(TTIStatus.TTI_SAFE)) {
      newComponentStatus = ComponentStatus.AVAILABLE;
    }

    // just mark it as expired or unsafe
    // note that expired or unsafe status should override
    // available, quarantined status hence this check is done
    // later in the code
    if (component.getExpiresOn().before(new Date())) {
      newComponentStatus = ComponentStatus.EXPIRED;
    }
    
    // If this component belongs to a donation with an unconfirmed blood typing match status
    boolean unconfirmedBloodGroup = donation.isReleased()
        && !BloodTypingMatchStatus.isBloodGroupConfirmed(donation.getBloodTypingMatchStatus());

    if (donation.isIneligibleDonor() || TTIStatus.isUnsafeStatus(ttiStatus) || unconfirmedBloodGroup) {
      newComponentStatus = ComponentStatus.UNSAFE;
    }

    if (!newComponentStatus.equals(oldComponentStatus)) {
      component.setStatus(newComponentStatus);
      return true;
    }
    return false;
  }
}
