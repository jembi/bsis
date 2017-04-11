package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.BloodTransportBoxBuilder;
import org.jembi.bsis.helpers.builders.ComponentBatchBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.BloodTransportBox;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentBatchRepositoryTests extends SecurityContextDependentTestSuite {

  @Autowired
  private ComponentBatchRepository componentBatchRepository;
  
  @Test
  public void testPersist() throws Exception {
    // set up data
    ComponentType componentType = aComponentType().buildAndPersist(entityManager);
    PackType packType = aPackType().withPackType("packType").withComponentType(componentType).buildAndPersist(entityManager);
    Donation donation = aDonation().withDonationIdentificationNumber("DIN123").withPackType(packType).buildAndPersist(entityManager);
    Component component = aComponent().withComponentType(componentType).withDonation(donation).buildAndPersist(entityManager);
    donation.setComponents(Arrays.asList(component));
    DonationBatch donationBatch = aDonationBatch().withDonation(donation).buildAndPersist(entityManager);    
    Location location = aProcessingSite().buildAndPersist(entityManager);
    ComponentBatch entity = ComponentBatchBuilder.aComponentBatch()
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(0.5).build())
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(12).build())
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(-2.5).build())
        .withStatus(ComponentBatchStatus.OPEN)
        .withDonationBatch(donationBatch)
        .withComponent(component)
        .withLocation(location)
        .build();
    component.setComponentBatch(entity);

    // run test
    componentBatchRepository.save(entity);
    
    // do checks - entity was saved
    Assert.assertNotNull("Entity was saved", entity.getId());
    ComponentBatch savedEntity = entityManager.find(ComponentBatch.class, entity.getId());
    Assert.assertNotNull("Entity was saved", savedEntity);
    Assert.assertEquals("Status was persisted", ComponentBatchStatus.OPEN, savedEntity.getStatus());
    // do checks - associations were persisted through Cascades
    Assert.assertNotNull("DonationBatch association was saved", savedEntity.getDonationBatch());
    Assert.assertNotNull("BloodTransportBox association was saved", savedEntity.getBloodTransportBoxes());
    BloodTransportBox savedBox = savedEntity.getBloodTransportBoxes().iterator().next();
    Assert.assertNotNull("BloodTransportBox association was saved", savedBox.getId());
    Assert.assertNotNull("Component association was saved", savedEntity.getComponents());
    Assert.assertFalse("Component association was saved", savedEntity.getComponents().isEmpty());
    Component savedComponent = savedEntity.getComponents().iterator().next();
    Assert.assertNotNull("Component association was saved", savedComponent.getComponentBatch());
    // do checks - audit fields were set By Entity
    Assert.assertNotNull("Audit fields were set", savedEntity.getCreatedBy());
    Assert.assertNotNull("Audit fields were set", savedEntity.getLastUpdatedBy());
    Assert.assertNotNull("Audit fields were set", savedEntity.getCreatedDate());
    Assert.assertNotNull("Audit fields were set", savedEntity.getLastUpdated());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindById_cannotFindUnknownId() throws Exception {
    componentBatchRepository.findById(UUID.randomUUID());
  }
  
  @Test
  public void testFindById_returnsComponentBatchAndBoxes() throws Exception {
    ComponentBatch newComponentBatch = ComponentBatchBuilder.aComponentBatch()
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(0.5).build())
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(0.5).build())
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(0.5).build())
        .buildAndPersist(entityManager);
    
    ComponentBatch componentBatch = componentBatchRepository.findById(newComponentBatch.getId());
    
    Assert.assertNotNull("ComponentBatch is found",  componentBatch);
    Assert.assertEquals("Correct ComponentBatch is returned",  newComponentBatch.getId(), componentBatch.getId());
    
    Assert.assertFalse("Boxes are persisted", componentBatch.getBloodTransportBoxes().isEmpty());
    Assert.assertEquals("Box temperature correct", 0.5, 
        componentBatch.getBloodTransportBoxes().iterator().next().getTemperature(), 0);
  }
  
  @Test
  public void testFindByIdEager_returnsComponentBatchAndLazyDependencies() throws Exception {
    ComponentType componentType = aComponentType().buildAndPersist(entityManager);
    PackType packType = aPackType().withPackType("packType").withComponentType(componentType).buildAndPersist(entityManager);
    Donation donation = aDonation().withDonationIdentificationNumber("DIN123").withPackType(packType).buildAndPersist(entityManager);
    Component component = aComponent().withComponentType(componentType).withDonation(donation).buildAndPersist(entityManager);
    donation.setComponents(Arrays.asList(component));
    DonationBatch donationBatch = aDonationBatch().withDonation(donation).buildAndPersist(entityManager);
    ComponentBatch newComponentBatch = ComponentBatchBuilder.aComponentBatch()
        .withComponent(component)
        .withDonationBatch(donationBatch)
        .buildAndPersist(entityManager);
    component.setComponentBatch(newComponentBatch);
    
    ComponentBatch componentBatch = componentBatchRepository.findByIdEager(newComponentBatch.getId());
    
    Assert.assertNotNull("ComponentBatch is found",  componentBatch);
    Assert.assertEquals("Correct ComponentBatch is returned",  newComponentBatch.getId(), componentBatch.getId());
    Assert.assertFalse("Components are returned", componentBatch.getComponents().isEmpty());
    Assert.assertNotNull("DonationBatch is returned", componentBatch.getDonationBatch());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindById_doesNotReturnDeletedComponentBatch() throws Exception {
    ComponentBatch newComponentBatch = ComponentBatchBuilder.aComponentBatch().thatIsDeleted().buildAndPersist(entityManager);
    
    componentBatchRepository.findById(newComponentBatch.getId());
  }
  
  @Test
  public void testFindByIdIncludeDeleted_returnsComponentBatch() throws Exception {
    ComponentBatch newComponentBatch = ComponentBatchBuilder.aComponentBatch().thatIsDeleted().buildAndPersist(entityManager);
    
    ComponentBatch componentBatch = componentBatchRepository.findByIdIncludeDeleted(newComponentBatch.getId());
    
    Assert.assertNotNull("ComponentBatch is found",  componentBatch);
    Assert.assertEquals("Correct ComponentBatch is returned",  newComponentBatch.getId(), componentBatch.getId());
  }
  
  @Test
  public void testFindByStatus_returnsOpenComponentBatches() throws Exception {
    ComponentBatchBuilder.aComponentBatch().withStatus(ComponentBatchStatus.OPEN).buildAndPersist(entityManager);
    ComponentBatchBuilder.aComponentBatch().withStatus(ComponentBatchStatus.OPEN).buildAndPersist(entityManager);
    ComponentBatchBuilder.aComponentBatch().withStatus(ComponentBatchStatus.CLOSED).buildAndPersist(entityManager);
    
    List<ComponentBatch> componentBatches = componentBatchRepository.findByStatus(ComponentBatchStatus.OPEN);
    
    Assert.assertNotNull("ComponentBatches are found",  componentBatches);
    Assert.assertFalse("ComponentBatches are found",  componentBatches.isEmpty());
    Assert.assertEquals("ComponentBatches are found", 2,  componentBatches.size());
    Assert.assertEquals("OPEN ComponentBatches are found", ComponentBatchStatus.OPEN,  componentBatches.get(0).getStatus());
    Assert.assertEquals("OPEN ComponentBatches are found", ComponentBatchStatus.OPEN,  componentBatches.get(1).getStatus());
  }
  
  @Test
  public void testFindByStatus_returnsOpenAndClosedComponentBatches() throws Exception {
    ComponentBatchBuilder.aComponentBatch().withStatus(ComponentBatchStatus.OPEN).buildAndPersist(entityManager);
    ComponentBatchBuilder.aComponentBatch().withStatus(ComponentBatchStatus.OPEN).buildAndPersist(entityManager);
    ComponentBatchBuilder.aComponentBatch().withStatus(ComponentBatchStatus.CLOSED).buildAndPersist(entityManager);
    
    List<ComponentBatch> componentBatches = componentBatchRepository.findByStatus(ComponentBatchStatus.OPEN, ComponentBatchStatus.CLOSED);
    
    Assert.assertNotNull("ComponentBatches are found",  componentBatches);
    Assert.assertFalse("ComponentBatches are found",  componentBatches.isEmpty());
    Assert.assertEquals("ComponentBatches are found", 3,  componentBatches.size());
  }

  @Test
  public void testFindComponentBatches_returnsComponentBatchesForSearchDates() throws Exception {

    DateTime now = new DateTime();
    Date aDayAgo = now.minusDays(1).toDate();
    Date twoDaysAgo = now.minusDays(2).toDate();
    Date threeDaysAgo = now.minusDays(3).toDate();

    ComponentBatchBuilder.aComponentBatch().withCollectionDate(aDayAgo).buildAndPersist(entityManager);
    ComponentBatchBuilder.aComponentBatch().withCollectionDate(twoDaysAgo).buildAndPersist(entityManager);
    ComponentBatchBuilder.aComponentBatch().withCollectionDate(threeDaysAgo).buildAndPersist(entityManager);

    List<ComponentBatch> componentBatches = componentBatchRepository.findComponentBatches(twoDaysAgo, now.toDate());

    Assert.assertEquals("Two componentBatches are found", 2, componentBatches.size());
  }
}
