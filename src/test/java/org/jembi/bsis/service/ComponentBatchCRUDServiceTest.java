package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.ComponentBatchRepository;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class ComponentBatchCRUDServiceTest extends UnitTestSuite {

  private static final UUID DONATION_BATCH_ID = UUID.randomUUID();
  private static final UUID COMPONENT_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_2 = UUID.randomUUID();

  @InjectMocks
  private ComponentBatchCRUDService service;
  
  @Mock
  private ComponentBatchRepository componentBatchRepository;
  
  @Mock
  private DonationBatchRepository donationBatchRepository;
  
  @Test
  public void testDeleteComponentBatch() throws Exception {
    // set up data
    ComponentBatch componentBatch = aComponentBatch().withId(UUID.randomUUID()).withDeliveryDate(new Date()).build();
    
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
    UUID packTypeId = UUID.randomUUID();
    
    ComponentType componentType1 = aComponentType().build();
    ComponentType componentType2 = aComponentType().build();
    PackType packType = aPackType().withId(packTypeId).withComponentType(componentType1).build();

    Donation donation1 = aDonation().withId(UUID.randomUUID()).withPackType(packType).build();
    Component component1 = aComponent().withId(COMPONENT_ID_1).withComponentType(componentType1).withDonation(donation1).build();
    donation1.setComponents(Arrays.asList(component1));

    Donation donation2 = aDonation().withId(UUID.randomUUID()).withPackType(packType).build();
    Component component2 = aComponent().withId(COMPONENT_ID_2).withComponentType(componentType1).withDonation(donation2).build();
    Component component3 = aComponent().withId(COMPONENT_ID_2).withComponentType(componentType2).withDonation(donation2).build();
    donation2.setComponents(Arrays.asList(component2, component3));
    
    DonationBatch donationBatch = aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withDonation(donation1)
        .withDonation(donation2)
        .withDonationBatchDate(new Date())
        .build();
    ComponentBatch componentBatch = aComponentBatch()
        .withDonationBatch(aDonationBatch().withId(DONATION_BATCH_ID).build())
        .withDeliveryDate(new Date())
        .build();
    
    // set up mocks
    Mockito.when(donationBatchRepository.findDonationBatchById(donationBatch.getId())).thenReturn(donationBatch);
    
    // run test
    service.createComponentBatch(componentBatch);
    
    // do asserts
    Mockito.verify(componentBatchRepository).save(componentBatch);
    Assert.assertEquals("ComponentBatch has status OPEN", ComponentBatchStatus.OPEN, componentBatch.getStatus());
    Assert.assertEquals("ComponentBatch has collection date", donationBatch.getDonationBatchDate(), componentBatch.getCollectionDate());
    Assert.assertNotNull("ComponentBatch has Components", componentBatch.getComponents());
    Assert.assertEquals("ComponentBatch has Components", 2, componentBatch.getComponents().size());
    Component component = componentBatch.getComponents().iterator().next();
    Assert.assertNotNull("Component has a ComponentBatch", component.getComponentBatch());
    Assert.assertEquals("Component has correct location", componentBatch.getLocation(), component.getLocation());
  }
  
  @Test
  public void testUpdateComponentBatch() throws Exception {
    // set up data
    UUID packTypeId = UUID.randomUUID();
    ComponentType componentType1 = aComponentType().build();
    ComponentType componentType2 = aComponentType().build();
    PackType packType = aPackType().withId(packTypeId).withComponentType(componentType1).build();
    
    Donation donation1 = aDonation().withId(UUID.randomUUID()).withPackType(packType).build();
    Component component1 = aComponent().withId(COMPONENT_ID_1).withComponentType(componentType1).withDonation(donation1).build();
    donation1.setComponents(Arrays.asList(component1));

    Donation donation2 = aDonation().withId(UUID.randomUUID()).withPackType(packType).build();
    Component component2 = aComponent().withId(COMPONENT_ID_2).withComponentType(componentType1).withDonation(donation2).build();
    Component component3 = aComponent().withId(COMPONENT_ID_2).withComponentType(componentType2).withDonation(donation2).build();
    donation2.setComponents(Arrays.asList(component2, component3));
    
    DonationBatch donationBatch =
        aDonationBatch().withId(DONATION_BATCH_ID).withDonation(donation1).withDonation(donation2).build();
    ComponentBatch componentBatch = aComponentBatch()
        .withDonationBatch(aDonationBatch().withId(DONATION_BATCH_ID).build())
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
