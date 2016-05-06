package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.ComponentTypeBuilder.aComponentType;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.PackTypeBuilder.aPackType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helpers.builders.BloodTransportBoxBuilder;
import helpers.builders.ComponentBatchBuilder;
import model.component.Component;
import model.componentbatch.BloodTransportBox;
import model.componentbatch.ComponentBatch;
import model.componentbatch.ComponentBatchStatus;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.packtype.PackType;
import suites.SecurityContextDependentTestSuite;

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
    ComponentBatch entity = ComponentBatchBuilder.aComponentBatch()
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(0.5).build())
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(12).build())
        .withBloodTransportBox(BloodTransportBoxBuilder.aBloodTransportBox().withTemperature(-2.5).build())
        .withStatus(ComponentBatchStatus.OPEN)
        .withDonationBatch(donationBatch)
        .withComponent(component)
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
    Assert.assertNotNull("Audit fields were set", savedBox.getCreatedBy());
    Assert.assertNotNull("Audit fields were set", savedBox.getLastUpdatedBy());
    Assert.assertNotNull("Audit fields were set", savedBox.getCreatedDate());
    Assert.assertNotNull("Audit fields were set", savedBox.getLastUpdated());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindById_cannotFindUnknownId() throws Exception {
    componentBatchRepository.findById(1L);
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
