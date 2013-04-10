package repository.listeners;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.collectedsample.CollectedSample;
import model.product.Product;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.ProductRepository;
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
  }

  private void updateProductStatus(CollectedSample collectedSample) {
    String queryStr = "SELECT p FROM Product p WHERE p.collectedSample.id=:collectedSampleId";
    TypedQuery<Product> query = em.createQuery(queryStr, Product.class);
    query.setParameter("collectedSampleId", collectedSample.getId());
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
