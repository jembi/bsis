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
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentStatusCalculator {
  
  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private DonationConstraintChecker donationConstraintChecker;

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

  /*
   * FIXME: This method needs comprehensive tests and the allowed status changes should be documented. It would be
   * best to write tests for the expected behaviours and check that the method handles those rather than basing the
   * tests on what is currently implemented.
   */
  public boolean updateComponentStatus(Component component) {

    // if a component has been explicitly discarded maintain that status.
    // if the component has been issued do not change its status.
    // suppose a component from a donation tested as safe was issued
    // then some additional tests were done for some reason and it was
    // discovered that the component was actually unsafe and it should not
    // have been issued then it should be easy to track down all components
    // created from that sample which were issued. By maintaining the status as
    // issued even if the component is unsafe we can search for all components created
    // from that donation and then look at which ones were already issued.
    // Conclusion is do not change the component status once it is marked as issued.
    // Similar reasoning for not changing USED status for a component. It should be
    // easy to track which used components were made from unsafe donations.
    // of course if the test results are not available or the donation is known
    // to be unsafe it should not have been issued in the first place.
    // In exceptional cases an admin can always delete this component and create a new one
    // if he wants to change the status to a new one.
    // once a component has been labeled as split it does not exist anymore so we just mark
    // it as SPLIT/PROCESSED. Even if the donation is found to be unsafe later it should not matter
    // as SPLIT/PROCESSED components are not allowed to be issued
    List<ComponentStatus> statusNotToBeChanged =
        Arrays.asList(ComponentStatus.DISCARDED, ComponentStatus.ISSUED,
            ComponentStatus.USED, ComponentStatus.SPLIT, ComponentStatus.PROCESSED);

    ComponentStatus oldComponentStatus = component.getStatus();

    // nothing to do if the component has any of these statuses
    if (component.getStatus() != null && statusNotToBeChanged.contains(component.getStatus()))
      return false;

    if (component.getDonation() == null)
      return false;
    Long donationId = component.getDonation().getId();
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTypingStatus bloodTypingStatus = donation.getBloodTypingStatus();

    TestBatch testBatch = donation.getDonationBatch().getTestBatch();
    boolean donationReleased = testBatch != null &&
        testBatch.getStatus() != TestBatchStatus.OPEN &&
        !donationConstraintChecker.donationHasDiscrepancies(donation);
    // If the donation has not been released yet, then don't use its TTI status
    TTIStatus ttiStatus = donationReleased ? donation.getTTIStatus() : TTIStatus.NOT_DONE;

    // Start with the old status if there is one.
    ComponentStatus newComponentStatus = oldComponentStatus == null ? ComponentStatus.QUARANTINED : oldComponentStatus;

    if (bloodTypingStatus.equals(BloodTypingStatus.COMPLETE) &&
        ttiStatus.equals(TTIStatus.TTI_SAFE) &&
        oldComponentStatus != ComponentStatus.UNSAFE) {
      newComponentStatus = ComponentStatus.AVAILABLE;
    }

    // just mark it as expired or unsafe
    // note that expired or unsafe status should override
    // available, quarantined status hence this check is done
    // later in the code
    if (component.getExpiresOn().before(new Date())) {
      newComponentStatus = ComponentStatus.EXPIRED;
    }

    if (ttiStatus.equals(TTIStatus.TTI_UNSAFE)) {
      newComponentStatus = ComponentStatus.UNSAFE;
    }

    if (!newComponentStatus.equals(oldComponentStatus)) {
      component.setStatus(newComponentStatus);
      return true;
    }
    return false;
  }
}
