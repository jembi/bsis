package service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import model.component.Component;
import model.componentbatch.ComponentBatch;
import model.componentbatch.ComponentBatchStatus;
import model.donation.Donation;
import model.donationbatch.DonationBatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.ComponentBatchRepository;
import repository.DonationBatchRepository;

@Transactional
@Service
public class ComponentBatchCRUDService {

  @Autowired
  private ComponentBatchRepository componentBatchRepository;

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  public ComponentBatch getComponentBatchById(Long id) throws NoResultException {
    return componentBatchRepository.findByIdEager(id);
  }

  public void deleteComponentBatch(Long id) throws NoResultException {
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
      for (Component component : donation.getComponents()) {
        if (component.getParentComponent() == null) {
          // found initial component
          components.add(component);
          component.setComponentBatch(componentBatch);
          break;
        }
      }
    }
    componentBatch.setComponents(components);
  }

  public List<ComponentBatch> findComponentBatches(Date startCollectionDate, Date endCollectionDate) {
    return componentBatchRepository.findComponentBatches(startCollectionDate, endCollectionDate);
  }

}
