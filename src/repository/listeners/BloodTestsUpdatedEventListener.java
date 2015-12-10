package repository.listeners;

import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.testbatch.TestBatchStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import repository.ComponentRepository;
import repository.bloodtesting.BloodTypingStatus;
import repository.events.BloodTestsUpdatedEvent;
import service.TestBatchStatusChangeService;
import viewmodel.BloodTestingRuleResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class BloodTestsUpdatedEventListener implements ApplicationListener<BloodTestsUpdatedEvent> {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private ComponentRepository componentRepository;
  @Autowired
  private TestBatchStatusChangeService testBatchStatusChangeService;

  @Override
  public void onApplicationEvent(BloodTestsUpdatedEvent event) {
    System.out.println("donation added event listener called");
    System.out.println("event ID: " + event.getEventId());
    System.out.println("event context: " + event.getEventContext());
    updateDonationStatus(event);
    updateDonorStatus(event);
  }

  private void updateDonorStatus(BloodTestsUpdatedEvent event) {
    Donation donation = event.getDonation();
    Donor donor = donation.getDonor();

    String oldBloodAbo = donor.getBloodAbo();
    String newBloodAbo = donor.getBloodAbo();
    String oldBloodRh = donor.getBloodRh();
    String newBloodRh = donor.getBloodRh();
    DonorStatus oldDonorStatus = donor.getDonorStatus();
    DonorStatus newDonorStatus = donor.getDonorStatus();
    if (newDonorStatus == null)
      newDonorStatus = DonorStatus.NORMAL;
    if (newBloodAbo == null)
      newBloodAbo = "";
    if (newBloodRh == null)
      newBloodRh = "";

    String queryStr = "SELECT c FROM Donation c WHERE " +
            "c.donor.id=:donorId AND c.isDeleted=:isDeleted";
    TypedQuery<Donation> query = em.createQuery(queryStr, Donation.class);

    query.setParameter("donorId", donor.getId());
    query.setParameter("isDeleted", false);

    List<Donation> donations = query.getResultList();

    /*
    String bloodAbo = BloodAbo.Unknown.toString();
    String bloodRh = BloodRh.Unknown.toString();
    if (donations.size() > 0) {
      bloodAbo = donations.get(0).getBloodAbo();
      bloodRh = donations.get(0).getBloodRh();
      for (int i = 1; i < donations.size(); i++) {
        Donation c = donations.get(i);
        BloodTypingStatus bloodTypingStatus = c.getBloodTypingStatus();
        // blood typing should be done otherwise we will not compare
        if (!bloodTypingStatus.equals(BloodTypingStatus.COMPLETE))
          continue;
        if (c.getBloodAbo() == null || c.getBloodRh() == null)
          continue;
        if (bloodAbo == null || bloodRh == null)
          continue;
        if (!bloodAbo.equals(c.getBloodAbo()) ||
            !bloodRh.equals(c.getBloodRh())) {
          newDonorStatus = DonorStatus.BLOOD_GROUP_MISMATCH;
          break;
        }
      }
      if (!newDonorStatus.equals(DonorStatus.BLOOD_GROUP_MISMATCH)) {
        newBloodAbo = bloodAbo;
        newBloodRh = bloodRh;
      }
    } else {
      newBloodAbo = bloodAbo;
      newBloodRh = bloodRh;
    }
    */

    // If TTI is unsafe then this status should override existing status
    if (donation.getTTIStatus().equals(TTIStatus.TTI_UNSAFE)) {
      newDonorStatus = DonorStatus.POSITIVE_TTI;
    }
    if (newBloodAbo == null)
      newBloodAbo = "";
    if (newBloodRh == null)
      newBloodRh = "";

    if (!newDonorStatus.equals(oldDonorStatus) ||
            !newBloodAbo.equals(oldBloodAbo) ||
            !newBloodRh.equals(oldBloodRh)
            ) {
      donor.setBloodAbo(newBloodAbo);
      donor.setBloodRh(newBloodRh);
      donor.setDonorStatus(newDonorStatus);
      em.merge(donor);
    }
  }

  private void updateDonationStatus(BloodTestsUpdatedEvent event) {

    Donation donation = event.getDonation();
    BloodTestingRuleResult ruleResult = event.getBloodTestingRuleResult();

    Set<String> extraInformationNewSet = ruleResult.getExtraInformation();

    String oldExtraInformation = donation.getExtraBloodTypeInformation();
    String newExtraInformation = donation.getExtraBloodTypeInformation();

    Set<String> oldExtraInformationSet = new HashSet<>();
    if (StringUtils.isNotBlank(oldExtraInformation)) {
      oldExtraInformationSet.addAll(Arrays.asList(oldExtraInformation.split(",")));
      // extra information is a field to which we add more information
      // do not store duplicate information in this field
      extraInformationNewSet.removeAll(oldExtraInformationSet);
      newExtraInformation = oldExtraInformation + StringUtils.join(extraInformationNewSet, ",");
    } else {
      newExtraInformation = StringUtils.join(extraInformationNewSet, ",");
    }

    String oldBloodAbo = donation.getBloodAbo();
    String newBloodAbo = ruleResult.getBloodAbo();
    String oldBloodRh = donation.getBloodRh();
    String newBloodRh = ruleResult.getBloodRh();

    TTIStatus oldTtiStatus = donation.getTTIStatus();
    TTIStatus newTtiStatus = ruleResult.getTTIStatus();

    BloodTypingStatus oldBloodTypingStatus = donation.getBloodTypingStatus();
    BloodTypingStatus newBloodTypingStatus = ruleResult.getBloodTypingStatus();

    if (!newExtraInformation.equals(oldExtraInformation) ||
            !newBloodAbo.equals(oldBloodAbo) ||
            !newBloodRh.equals(oldBloodRh) ||
            !newTtiStatus.equals(oldTtiStatus) ||
            !newBloodTypingStatus.equals(oldBloodTypingStatus)
            ) {
      donation.setExtraBloodTypeInformation(newExtraInformation);
      donation.setBloodAbo(newBloodAbo);
      donation.setBloodRh(newBloodRh);
      donation.setTTIStatus(ruleResult.getTTIStatus());
      donation.setBloodTypingStatus(ruleResult.getBloodTypingStatus());
      donation = em.merge(donation);
    }

    if (donation.getDonationBatch().getTestBatch().getStatus() == TestBatchStatus.RELEASED) {
      testBatchStatusChangeService.handleRelease(donation);
    }

    donation.setBloodTypingMatchStatus(ruleResult.getBloodTypingMatchStatus());

  }
}
