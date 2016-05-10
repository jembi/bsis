package service;

import static helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.ComponentTypeBuilder.aComponentType;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.PackTypeBuilder.aPackType;

import java.util.Arrays;
import java.util.Date;

import model.component.Component;
import model.componentbatch.ComponentBatch;
import model.componentbatch.ComponentBatchStatus;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.packtype.PackType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import repository.ComponentBatchRepository;
import repository.DonationBatchRepository;

@RunWith(MockitoJUnitRunner.class)
public class ComponentBatchCRUDServiceTest {

  @InjectMocks
  private ComponentBatchCRUDService service;
  
  @Mock
  private ComponentBatchRepository componentBatchRepository;
  
  @Mock
  private DonationBatchRepository donationBatchRepository;
  
  @Test
  public void testDeleteComponentBatch() throws Exception {
    // set up data
    ComponentBatch componentBatch = aComponentBatch().withId(1L).withDeliveryDate(new Date()).build();
    
    // set up mocks
    Mockito.when(componentBatchRepository.findById(componentBatch.getId())).thenReturn(componentBatch);
    
    // run test
    service.deleteComponentBatch(componentBatch.getId());
    
    // do asserts
    Assert.assertEquals("ComponentBatch has been deleted", true, componentBatch.getIsDeleted());
    Assert.assertNull("DonationBatch has been deleted", componentBatch.getDonationBatch());
  }
  
  @Test
  public void testCreateComponentBatch() throws Exception {
    // set up data        
    ComponentType componentType1 = aComponentType().build();
    ComponentType componentType2 = aComponentType().build();
    PackType packType = aPackType().withId(1L).withComponentType(componentType1).build();
    
    Donation donation1 = aDonation().withId(1L).withPackType(packType).build();
    Component component1 = aComponent().withId(1L).withComponentType(componentType1).withDonation(donation1).build();
    donation1.setComponents(Arrays.asList(component1));

    Donation donation2 = aDonation().withId(2L).withPackType(packType).build();
    Component component2 = aComponent().withId(2L).withComponentType(componentType1).withDonation(donation2).build();
    Component component3 = aComponent().withId(2L).withComponentType(componentType2).withDonation(donation2).build();
    donation2.setComponents(Arrays.asList(component2, component3));
    
    DonationBatch donationBatch = aDonationBatch().withId(1L).withDonation(donation1).withDonation(donation2).build();
    ComponentBatch componentBatch = aComponentBatch()
        .withDonationBatch(aDonationBatch().withId(1L).build())
        .withDeliveryDate(new Date())
        .build();
    
    // set up mocks
    Mockito.when(donationBatchRepository.findDonationBatchById(donationBatch.getId())).thenReturn(donationBatch);
    
    // run test
    service.createComponentBatch(componentBatch);
    
    // do asserts
    Mockito.verify(componentBatchRepository).save(componentBatch);
    Assert.assertEquals("ComponentBatch has status OPEN", ComponentBatchStatus.OPEN, componentBatch.getStatus());
    Assert.assertNotNull("ComponentBatch has Components", componentBatch.getComponents());
    Assert.assertEquals("ComponentBatch has Components", 2, componentBatch.getComponents().size());
    Component component = componentBatch.getComponents().iterator().next();
    Assert.assertNotNull("Component has a ComponentBatch", component.getComponentBatch());
  }
  
  @Test
  public void testUpdateComponentBatch() throws Exception {
    // set up data        
    ComponentType componentType1 = aComponentType().build();
    ComponentType componentType2 = aComponentType().build();
    PackType packType = aPackType().withId(1L).withComponentType(componentType1).build();
    
    Donation donation1 = aDonation().withId(1L).withPackType(packType).build();
    Component component1 = aComponent().withId(1L).withComponentType(componentType1).withDonation(donation1).build();
    donation1.setComponents(Arrays.asList(component1));

    Donation donation2 = aDonation().withId(2L).withPackType(packType).build();
    Component component2 = aComponent().withId(2L).withComponentType(componentType1).withDonation(donation2).build();
    Component component3 = aComponent().withId(2L).withComponentType(componentType2).withDonation(donation2).build();
    donation2.setComponents(Arrays.asList(component2, component3));
    
    DonationBatch donationBatch = aDonationBatch().withId(1L).withDonation(donation1).withDonation(donation2).build();
    ComponentBatch componentBatch = aComponentBatch()
        .withDonationBatch(aDonationBatch().withId(1L).build())
        .withDeliveryDate(new Date())
        .build();
    
    // set up mocks
    Mockito.when(donationBatchRepository.findDonationBatchById(donationBatch.getId())).thenReturn(donationBatch);
    
    // run test
    service.updateComponentBatch(componentBatch);
    
    // do asserts
    Mockito.verify(componentBatchRepository).update(componentBatch);
    Assert.assertNotNull("ComponentBatch has Components", componentBatch.getComponents());
    Assert.assertEquals("ComponentBatch has Components", 2, componentBatch.getComponents().size());
  }
}
