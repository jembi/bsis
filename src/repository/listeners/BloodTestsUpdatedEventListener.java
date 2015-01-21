package repository.listeners;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtesting.TTIStatus;
import model.collectedsample.CollectedSample;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.product.Product;
import model.util.BloodAbo;
import model.util.BloodRh;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.ProductRepository;
import repository.bloodtesting.BloodTypingStatus;
import repository.events.BloodTestsUpdatedEvent;
import viewmodel.BloodTestingRuleResult;

@Repository
@Transactional
public class BloodTestsUpdatedEventListener implements ApplicationListener<BloodTestsUpdatedEvent> {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private ProductRepository productRepository;

  @Override
  public void onApplicationEvent(BloodTestsUpdatedEvent event) {
    System.out.println("collection added event listener called");
    System.out.println("event ID: " + event.getEventId());
    System.out.println("event context: " + event.getEventContext());
    updateCollectionStatus(event);
    updateDonorStatus(event);
  }

  private void updateDonorStatus(BloodTestsUpdatedEvent event) {
    CollectedSample collectedSample = event.getCollectedSample();
    Donor donor = collectedSample.getDonor();

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

    String queryStr = "SELECT c FROM CollectedSample c WHERE " +
        "c.donor.id=:donorId AND c.isDeleted=:isDeleted";
    TypedQuery<CollectedSample> query = em.createQuery(queryStr, CollectedSample.class);

    query.setParameter("donorId", donor.getId());
    query.setParameter("isDeleted", false);

    List<CollectedSample> collectedSamples = query.getResultList();

    /*
    String bloodAbo = BloodAbo.Unknown.toString();
    String bloodRh = BloodRh.Unknown.toString();
    if (collectedSamples.size() > 0) {
      bloodAbo = collectedSamples.get(0).getBloodAbo();
      bloodRh = collectedSamples.get(0).getBloodRh();
      for (int i = 1; i < collectedSamples.size(); i++) {
        CollectedSample c = collectedSamples.get(i);
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
    if (collectedSample.getTTIStatus().equals(TTIStatus.TTI_UNSAFE)) {
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

  private void updateProductStatus(CollectedSample collectedSample) {
    String queryStr = "SELECT p FROM Product p WHERE " +
        "p.collectedSample.id=:collectedSampleId AND p.isDeleted=:isDeleted";
    TypedQuery<Product> query = em.createQuery(queryStr, Product.class);
    query.setParameter("collectedSampleId", collectedSample.getId());
    query.setParameter("isDeleted", false);
    List<Product> products = query.getResultList();
    for (Product product : products) {
      if (productRepository.updateProductInternalFields(product)) {
        em.merge(product);
      }
    }
  }

  private void updateCollectionStatus(BloodTestsUpdatedEvent event) {

    CollectedSample collectedSample = event.getCollectedSample();
    BloodTestingRuleResult ruleResult = event.getBloodTestingRuleResult();

    Set<String> extraInformationNewSet = ruleResult.getExtraInformation();

    String oldExtraInformation = collectedSample.getExtraBloodTypeInformation();
    String newExtraInformation = collectedSample.getExtraBloodTypeInformation();

    Set<String> oldExtraInformationSet = new HashSet<String>();
    if (StringUtils.isNotBlank(oldExtraInformation)) {
      oldExtraInformationSet.addAll(Arrays.asList(oldExtraInformation.split(",")));
      // extra information is a field to which we add more information
      // do not store duplicate information in this field
      extraInformationNewSet.removeAll(oldExtraInformationSet);
      newExtraInformation = oldExtraInformation + StringUtils.join(extraInformationNewSet, ",");
    }
    else {
      newExtraInformation = StringUtils.join(extraInformationNewSet, ",");
    }

    String oldBloodAbo = collectedSample.getBloodAbo();
    String newBloodAbo = ruleResult.getBloodAbo();
    String oldBloodRh = collectedSample.getBloodRh();
    String newBloodRh = ruleResult.getBloodRh();

    TTIStatus oldTtiStatus = collectedSample.getTTIStatus();
    TTIStatus newTtiStatus = ruleResult.getTTIStatus();

    BloodTypingStatus oldBloodTypingStatus = collectedSample.getBloodTypingStatus();
    BloodTypingStatus newBloodTypingStatus = ruleResult.getBloodTypingStatus();

    if (!newExtraInformation.equals(oldExtraInformation) ||
        !newBloodAbo.equals(oldBloodAbo) ||
        !newBloodRh.equals(oldBloodRh) ||
        !newTtiStatus.equals(oldTtiStatus) ||
        !newBloodTypingStatus.equals(oldBloodTypingStatus)
        ) {
      collectedSample.setExtraBloodTypeInformation(newExtraInformation);
      collectedSample.setBloodAbo(newBloodAbo);
      collectedSample.setBloodRh(newBloodRh);
      collectedSample.setTTIStatus(ruleResult.getTTIStatus());
      collectedSample.setBloodTypingStatus(ruleResult.getBloodTypingStatus());
      collectedSample = em.merge(collectedSample);
    }
    updateProductStatus(collectedSample);
    
    collectedSample.setBloodTypingMatchStatus(ruleResult.getBloodTypingMatchStatus());

  }
}
