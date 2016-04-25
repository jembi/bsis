package repository;

import helpers.builders.BloodTransportBoxBuilder;
import helpers.builders.ComponentBatchBuilder;

import java.util.List;

import model.componentbatch.ComponentBatch;
import model.componentbatch.ComponentBatchStatus;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.ContextDependentTestSuite;

public class ComponentBatchRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private ComponentBatchRepository componentBatchRepository;

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
    
    Assert.assertEquals("Correct number of boxes", 3, componentBatch.getBloodTransportBoxCount());
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
}
