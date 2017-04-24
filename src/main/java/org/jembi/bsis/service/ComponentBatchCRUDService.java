package org.jembi.bsis.service;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.repository.ComponentBatchRepository;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.NoResultException;

@Transactional
@Service
public class ComponentBatchCRUDService {

  @Autowired
  private ComponentBatchRepository componentBatchRepository;

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  public ComponentBatch getComponentBatchById(UUID id) throws NoResultException {
    return componentBatchRepository.findByIdEager(id);
  }

  public void deleteComponentBatch(UUID id) throws NoResultException {
    ComponentBatch componentBatch = componentBatchRepository.findById(id);
    componentBatch.setIsDeleted(true);
    componentBatch.setDonationBatch(null);
    componentBatchRepository.update(componentBatch);
  }

  public ComponentBatch createComponentBatch(ComponentBatch componentBatch) {
    DonationBatch donationBatch =
        donationBatchRepository.findDonationBatchById(componentBatch.getDonationBatch().getId());
    donationBatch.setComponentBatch(componentBatch);
    componentBatch.setDonationBatch(donationBatch);
    componentBatch.setCollectionDate(donationBatch.getDonationBatchDate());
    setInitialComponentsFromDonationBatch(donationBatch, componentBatch);
    componentBatch.setStatus(ComponentBatchStatus.OPEN);

    componentBatchRepository.save(componentBatch);

    return componentBatch;
  }

  public ComponentBatch updateComponentBatch(ComponentBatch componentBatch) throws NoResultException {
    DonationBatch donationBatch =
        donationBatchRepository.findDonationBatchById(componentBatch.getDonationBatch().getId());
    donationBatch.setComponentBatch(componentBatch);
    setInitialComponentsFromDonationBatch(donationBatch, componentBatch);
    componentBatch = componentBatchRepository.update(componentBatch);
    return componentBatch;
  }

  private void setInitialComponentsFromDonationBatch(DonationBatch donationBatch, ComponentBatch componentBatch) {
    // associate the ComponentBatch with the initial Components
    componentBatch.getComponents().clear();
    Set<Component> components = new HashSet<>();
    for (Donation donation : donationBatch.getDonations()) {
      Component component = donation.getInitialComponent();
      if (component != null) {
        // found initial component
        components.add(component);
        component.setComponentBatch(componentBatch);
        // Move received components to the component batch's location
        component.setLocation(componentBatch.getLocation());
      }
    }
    componentBatch.setComponents(components);
  }

  public List<ComponentBatch> findComponentBatches(Date startCollectionDate, Date endCollectionDate) {
    return componentBatchRepository.findComponentBatches(startCollectionDate, endCollectionDate);
  }

}
