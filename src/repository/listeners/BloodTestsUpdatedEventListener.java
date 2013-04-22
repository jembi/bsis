package repository.listeners;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.collectedsample.CollectedSample;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.product.Product;
import model.testresults.TTIStatus;
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
    DonorStatus donorStatus = donor.getDonorStatus();
    if (donorStatus == null)
      donorStatus = DonorStatus.NORMAL;

    String queryStr = "SELECT c FROM CollectedSample c WHERE " +
    		"c.donor.id=:donorId AND c.isDeleted=:isDeleted";
    TypedQuery<CollectedSample> query = em.createQuery(queryStr, CollectedSample.class);

    query.setParameter("donorId", donor.getId());
    query.setParameter("isDeleted", false);

    List<CollectedSample> collectedSamples = query.getResultList();

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
          donorStatus = DonorStatus.BLOOD_GROUP_MISMATCH;
          break;
        }
      }
      if (!donorStatus.equals(DonorStatus.BLOOD_GROUP_MISMATCH)) {
        donor.setBloodAbo(bloodAbo);
        donor.setBloodRh(bloodRh);
      }
    } else {
      donor.setBloodAbo(bloodAbo);
      donor.setBloodRh(bloodRh);
    }

    // If TTI is unsafe then this status should override existing status
    if (collectedSample.getTTIStatus().equals(TTIStatus.TTI_UNSAFE)) {
      donorStatus = DonorStatus.POSITIVE_TTI;
    }
    donor.setDonorStatus(donorStatus);
    em.merge(donor);
  }

  private void updateProductStatus(CollectedSample collectedSample) {
    String queryStr = "SELECT p FROM Product p WHERE " +
    		"p.collectedSample.id=:collectedSampleId AND p.isDeleted=:isDeleted";
    TypedQuery<Product> query = em.createQuery(queryStr, Product.class);
    query.setParameter("collectedSampleId", collectedSample.getId());
    query.setParameter("isDeleted", false);
    List<Product> products = query.getResultList();
    for (Product product : products) {
      productRepository.updateProductInternalFields(product);
    }
  }

  private CollectedSample updateCollectionStatus(BloodTestsUpdatedEvent event) {

    CollectedSample collectedSample = event.getCollectedSample();
    BloodTestingRuleResult ruleResult = event.getBloodTestingRuleResult();

    String bloodAboNew = ruleResult.getBloodAbo();
    String bloodRhNew = ruleResult.getBloodRh();
    Set<String> extraInformationNew = ruleResult.getExtraInformation();

    String extraInformation = collectedSample.getExtraBloodTypeInformation();

    Set<String> extraInformationOld = new HashSet<String>();
    if (StringUtils.isNotBlank(extraInformation)) {
      extraInformationOld.addAll(Arrays.asList(extraInformation.split(",")));
      // extra information is a field to which we add more information
      // do not store duplicate information in this field
      extraInformationNew.removeAll(extraInformationOld);
      collectedSample.setExtraBloodTypeInformation(extraInformation + StringUtils.join(extraInformationNew, ","));
    }
    else {
      collectedSample.setExtraBloodTypeInformation(StringUtils.join(extraInformationNew, ","));
    }

    collectedSample.setBloodAbo(bloodAboNew);
    collectedSample.setBloodRh(bloodRhNew);

    collectedSample.setTTIStatus(ruleResult.getTTIStatus());

    collectedSample.setBloodTypingStatus(ruleResult.getBloodTypingStatus());

    collectedSample = em.merge(collectedSample);

    updateProductStatus(collectedSample);
    return collectedSample;
  }
}
