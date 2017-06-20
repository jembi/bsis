package org.jembi.bsis.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.DonationRepository;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentStatusCalculator {
  
  @Autowired
  private DonationRepository donationRepository;
  
  @Autowired
  private DateGeneratorService dateGeneratorService;

  public boolean shouldComponentsBeDiscardedForTestResults(List<BloodTestResult> bloodTestResults) {

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
  
  public boolean shouldComponentsBeDiscardedForTestResultsIfContainsPlasma(List<BloodTestResult> bloodTestResults) {
    for (BloodTestResult bloodTestResult : bloodTestResults) {
      BloodTest bloodTest = bloodTestResult.getBloodTest();
      if (!bloodTest.getFlagComponentsContainingPlasmaForDiscard()) {
        // The blood test does not flag components with plasma for discard
        continue;
      }
      
      List<String> positiveBloodTestResults = Arrays.asList(bloodTest.getPositiveResults().split(","));
      if (positiveBloodTestResults.contains(bloodTestResult.getResult())) {
        // The blood test result is positive and it should therefore flag components that contain plasma for discard
        return true;
      }
    }
    // There are no positive blood tests which flag components for discard if they contain plasma
    return false;
  }
  
  /**
   * Determines if the Component should be flagged for discard whether it contains
   * plasma or not.
   * 
   * Reasons:
   *  - initial component weight not between the PackType low volume and max weight
   * 
   * @param component
   * @return
   */
  public boolean shouldComponentBeDiscardedForInvalidWeight(Component component) {
    if (component.isInitialComponent() && component.getWeight() != null) {
      PackType packType = component.getDonation().getPackType();
      if (packType.getMinWeight() == null || packType.getMaxWeight() == null) {
        throw new IllegalStateException("PackType does not have a min and max weight specified: " + packType);
      }
      Integer weight = component.getWeight();
      if (packType.getLowVolumeWeight() == null
          && (weight > packType.getMaxWeight() || weight < packType.getMinWeight())) {
        return true;
      } else if (packType.getLowVolumeWeight() != null
          && (weight > packType.getMaxWeight() || weight <= packType.getLowVolumeWeight())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if the component contains plasma and its weight is between lowVolumeWeight and max minWeight.
   *
   * @param component
   * @return
   */
  public boolean shouldComponentBeDiscardedForLowWeight(Component component) {
    if (component.isInitialComponent() && component.getWeight() != null) {
      PackType packType = component.getDonation().getPackType();
      Integer weight = component.getWeight();
      if (packType.getLowVolumeWeight() != null && component.getComponentType().getContainsPlasma()
          && weight >= packType.getLowVolumeWeight() && weight < packType.getMinWeight()) {
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

    ComponentStatus oldComponentStatus = component.getStatus();

    // nothing to do if the component is already in a final state
    if (ComponentStatus.isFinalStatus(oldComponentStatus)) {
      return false;
    }

    UUID donationId = component.getDonation().getId();
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
        ttiStatus.equals(TTIStatus.SAFE)) {
      newComponentStatus = ComponentStatus.AVAILABLE;
    }

    // just mark it as expired or unsafe
    // note that expired or unsafe status should override
    // available, quarantined status hence this check is done
    // later in the code
    if (component.getExpiresOn().before(new Date())) {
      newComponentStatus = ComponentStatus.EXPIRED;
    }
    
    // if the Component has an undeleted UNSAFE status changes, then it is unsafe
    if (component.getStatusChanges() != null) {
      for (ComponentStatusChange statusChange : component.getStatusChanges()) {
        if (!statusChange.getIsDeleted() && statusChange.getStatusChangeReason().getCategory() == ComponentStatusChangeReasonCategory.UNSAFE) {
          newComponentStatus = ComponentStatus.UNSAFE;
        }
      }
    }
    
    // If this component belongs to a donation with an unconfirmed blood typing match status
    boolean unconfirmedBloodGroup = donation.isReleased()
        && !BloodTypingMatchStatus.isBloodGroupConfirmed(donation.getBloodTypingMatchStatus());

    if (donation.isIneligibleDonor() || TTIStatus.makesComponentsUnsafe(ttiStatus) || unconfirmedBloodGroup) {
      newComponentStatus = ComponentStatus.UNSAFE;
    }

    if (!newComponentStatus.equals(oldComponentStatus)) {
      component.setStatus(newComponentStatus);
      return true;
    }
    return false;
  }
  
  
  /**
   * Calculates the number of days before a component expires 
   * 
   * If the component has already expired the difference is -1 
   * 
   * If the component expires today the difference is 0 
   * 
   * Otherwise the number of days left before the component expires is returned
   * 
   * @param component
   * @return
   */
  public int getDaysToExpire(Component component) {

    Date today = dateGeneratorService.generateDate();
    if (today.after(component.getExpiresOn())) {
      return -1;
    } else {
      DateTime expiresOn = new DateTime(component.getExpiresOn());
      return Days.daysBetween(new DateTime(today), expiresOn).getDays();
    }
  }
}
