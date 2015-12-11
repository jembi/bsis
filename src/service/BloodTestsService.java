package service;

import model.bloodtesting.TTIStatus;
import model.component.Component;
import model.donation.Donation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ComponentRepository;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTypingStatus;
import viewmodel.BloodTestingRuleResult;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service for BloodTest related business logic/workflow
 */
@Transactional
@Service
public class BloodTestsService {

  @Autowired
  ComponentRepository componentRepository;

  @Autowired
  BloodTestingRepository bloodTestingRepository;

  /**
   * Executes the BloodTestingRuleEngine with the configured BloodTests and returns the results
   *
   * @param donation Donation to run the tests on
   * @return BloodTestingRuleResult with the results from the tests
   */
  public BloodTestingRuleResult executeTests(Donation donation) {
    BloodTestingRuleResult ruleResult = bloodTestingRepository.getAllTestsStatusForDonation(donation.getId());
    return ruleResult;
  }

  /**
   * Updates the specified Donation given the results from the BloodTests. Updates include blood
   * grouping, extra information, TTI status and blood typing statuses.
   *
   * @param donation   Donation on which the tests were run
   * @param ruleResult BloodTestingRuleResult containing the results from the tests
   * @return boolean, true if the Donation was updated
   */
  public boolean updateDonationWithTestResults(Donation donation, BloodTestingRuleResult ruleResult) {
    boolean donationUpdated = false;

    String oldExtraInformation = donation.getExtraBloodTypeInformation();
    String newExtraInformation = addNewExtraInformation(oldExtraInformation, ruleResult.getExtraInformation());

    String oldBloodAbo = donation.getBloodAbo();
    String newBloodAbo = ruleResult.getBloodAbo();

    String oldBloodRh = donation.getBloodRh();
    String newBloodRh = ruleResult.getBloodRh();

    TTIStatus oldTtiStatus = donation.getTTIStatus();
    TTIStatus newTtiStatus = ruleResult.getTTIStatus();

    BloodTypingStatus oldBloodTypingStatus = donation.getBloodTypingStatus();
    BloodTypingStatus newBloodTypingStatus = ruleResult.getBloodTypingStatus();

    if (!newExtraInformation.equals(oldExtraInformation) || !newBloodAbo.equals(oldBloodAbo)
            || !newBloodRh.equals(oldBloodRh) || !newTtiStatus.equals(oldTtiStatus)
            || !newBloodTypingStatus.equals(oldBloodTypingStatus)) {
      donation.setExtraBloodTypeInformation(newExtraInformation);
      donation.setBloodAbo(newBloodAbo);
      donation.setBloodRh(newBloodRh);
      donation.setTTIStatus(ruleResult.getTTIStatus());
      donation.setBloodTypingStatus(ruleResult.getBloodTypingStatus());

      donationUpdated = true;
    }
    donation.setBloodTypingMatchStatus(ruleResult.getBloodTypingMatchStatus());

    return donationUpdated;
  }

  /**
   * Updates Components as a result of Blood Tests being done on a Donation. The updates include
   * the Component Status - and should result in Components being discarded if a Donation is
   * marked as TTI_UNSAFE.
   *
   * @param donation   Donation on which the tests were run
   * @param ruleResult BloodTestingRuleResult results from the Blood Tests.
   */
  public void updateComponentsWithTestResults(Donation donation, BloodTestingRuleResult ruleResult) {
    List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber(donation
            .getDonationIdentificationNumber());
    if (components != null) {
      for (Component component : components) {
        // FIXME: this method should be in this service, but it has too many references in ComponentRepository
        componentRepository.updateComponentInternalFields(component);
      }
    }
  }

  private String addNewExtraInformation(String donationExtraInformation, Set<String> extraInformationNewSet) {
    String newExtraInformation;
    Set<String> oldExtraInformationSet = new HashSet<>();
    if (StringUtils.isNotBlank(donationExtraInformation)) {
      oldExtraInformationSet.addAll(Arrays.asList(donationExtraInformation.split(",")));
      extraInformationNewSet.removeAll(oldExtraInformationSet); // remove duplicates
      newExtraInformation = donationExtraInformation + StringUtils.join(extraInformationNewSet, ",");
    } else {
      newExtraInformation = StringUtils.join(extraInformationNewSet, ",");
    }
    return newExtraInformation;
  }
}
