package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.BloodTransportBoxBackingFormBuilder.aBloodTransportBoxBackingForm;
import static org.jembi.bsis.helpers.builders.BloodTransportBoxBuilder.aBloodTransportBox;
import static org.jembi.bsis.helpers.builders.ComponentBatchBackingFormBuilder.aComponentBatchBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBatchBackingFormBuilder.aDonationBatchBackingForm;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.matchers.ComponentBatchMatcher.hasSameStateAsComponentBatch;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.BloodTransportBoxBackingForm;
import org.jembi.bsis.backingform.ComponentBatchBackingForm;
import org.jembi.bsis.backingform.DonationBatchBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.helpers.builders.ComponentFullViewModelBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.BloodTransportBox;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTransportBoxViewModel;
import org.jembi.bsis.viewmodel.ComponentBatchFullViewModel;
import org.jembi.bsis.viewmodel.ComponentBatchViewModel;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ComponentBatchFactoryTests extends UnitTestSuite {

  private static final UUID DONATION_BATCH_ID = UUID.randomUUID();
  private static final UUID COMPONENT_BATCH_ID = UUID.randomUUID();

  @InjectMocks
  ComponentBatchFactory componentBatchFactory;
  @Mock
  BloodTransportBoxViewModelFactory bloodTransportBoxViewModelFactory;
  @Mock
  ComponentFactory componentFactory;
  @Mock
  DonationBatchViewModelFactory donationBatchViewModelFactory;

  @Test
  public void testCreateEntity_createOneComponentBatch() throws Exception {
    Date deliveryDate = new Date();
    UUID locationId = UUID.randomUUID();
    LocationBackingForm locationForm = aLocationBackingForm().withId(locationId).build();
    DonationBatchBackingForm donationBatchBackingForm = aDonationBatchBackingForm().withId(DONATION_BATCH_ID).build();
    BloodTransportBoxBackingForm bloodTransportBoxBackingForm = aBloodTransportBoxBackingForm().withId(1L).build();
    ComponentBatchBackingForm form = aComponentBatchBackingForm()
        .withId(COMPONENT_BATCH_ID)
        .withStatus(ComponentBatchStatus.OPEN)
        .withDeliveryDate(deliveryDate)
        .withLocation(locationForm)
        .withDonationBatch(donationBatchBackingForm)
        .withBloodTransportBox(bloodTransportBoxBackingForm)
        .build();

    Location location = aLocation().withId(locationId).build();
    DonationBatch donationBatch = aDonationBatch().withId(DONATION_BATCH_ID).build();
    BloodTransportBox bloodTransportBox = aBloodTransportBox().withId(1L).build();
    ComponentBatch expectedComponentBatch = aComponentBatch()
        .withId(COMPONENT_BATCH_ID)
        .withStatus(ComponentBatchStatus.OPEN)
        .withDeliveryDate(deliveryDate)
        .withCollectionDate(null)
        .withLocation(location)
        .withDonationBatch(donationBatch)
        .withBloodTransportBox(bloodTransportBox)
        .build();

    ComponentBatch componentBatch = componentBatchFactory.createEntity(form);

    assertThat(componentBatch, hasSameStateAsComponentBatch(expectedComponentBatch));
  }

  @Test
  public void testCreateEntityWithNoLocation_createOneComponentBatch() throws Exception {
    Date deliveryDate = new Date();
    DonationBatchBackingForm donationBatchBackingForm = aDonationBatchBackingForm().withId(DONATION_BATCH_ID).build();
    BloodTransportBoxBackingForm bloodTransportBoxBackingForm = aBloodTransportBoxBackingForm().withId(1L).build();
    ComponentBatchBackingForm form = aComponentBatchBackingForm()
        .withId(COMPONENT_BATCH_ID)
        .withStatus(ComponentBatchStatus.OPEN)
        .withDeliveryDate(deliveryDate)
        .withLocation(null)
        .withDonationBatch(donationBatchBackingForm)
        .withBloodTransportBox(bloodTransportBoxBackingForm)
        .build();

    DonationBatch donationBatch = aDonationBatch().withId(DONATION_BATCH_ID).build();
    BloodTransportBox bloodTransportBox = aBloodTransportBox().withId(1L).build();
    ComponentBatch expectedComponentBatch = aComponentBatch()
        .withId(COMPONENT_BATCH_ID)
        .withStatus(ComponentBatchStatus.OPEN)
        .withDeliveryDate(deliveryDate)
        .withCollectionDate(null)
        .withLocation(null)
        .withDonationBatch(donationBatch)
        .withBloodTransportBox(bloodTransportBox)
        .build();

    ComponentBatch componentBatch = componentBatchFactory.createEntity(form);

    assertThat(componentBatch, hasSameStateAsComponentBatch(expectedComponentBatch));
  }
  
  @Test
  public void testCreateEntityWithNoDonationBatch_createOneComponentBatch() throws Exception {
    Date deliveryDate = new Date();
    UUID locationId = UUID.randomUUID();
    LocationBackingForm locationForm = aLocationBackingForm().withId(locationId).build();
    BloodTransportBoxBackingForm bloodTransportBoxBackingForm = aBloodTransportBoxBackingForm().withId(1L).build();
    ComponentBatchBackingForm form = aComponentBatchBackingForm()
        .withId(COMPONENT_BATCH_ID)
        .withStatus(ComponentBatchStatus.OPEN)
        .withDeliveryDate(deliveryDate)
        .withLocation(locationForm)
        .withDonationBatch(null)
        .withBloodTransportBox(bloodTransportBoxBackingForm)
        .build();

    Location location = aLocation().withId(locationId).build();
    BloodTransportBox bloodTransportBox = aBloodTransportBox().withId(1L).build();
    ComponentBatch expectedComponentBatch = aComponentBatch()
        .withId(COMPONENT_BATCH_ID)
        .withStatus(ComponentBatchStatus.OPEN)
        .withDeliveryDate(deliveryDate)
        .withCollectionDate(null)
        .withLocation(location)
        .withDonationBatch(null)
        .withBloodTransportBox(bloodTransportBox)
        .build();

    ComponentBatch componentBatch = componentBatchFactory.createEntity(form);

    assertThat(componentBatch, hasSameStateAsComponentBatch(expectedComponentBatch));
  }

  @Test
  public void testCreateComponentBatchViewModel_createOneComponentBatch() throws Exception {
    // set up data
    UUID locationId1 = UUID.randomUUID();
    Location venue = aVenue().withId(locationId1).withName("venue").build();
    UUID locationId2 = UUID.randomUUID();
    Location location = aLocation().withId(locationId2).withName("distribution site").thatIsUsageSite().build();    UUID componentId1 = UUID.randomUUID();
    UUID componentId2 = UUID.randomUUID();
    DonationBatch donationBatch = aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withBatchNumber("BN1234")
        .withVenue(venue)
        .build();
    Donation donation = aDonation()
        .withId(UUID.randomUUID())
        .withDonationIdentificationNumber("DIN1234")
        .withDonationBatch(donationBatch)
        .build();
    donationBatch.setDonation(Arrays.asList(donation));
    Component component = aComponent()
        .withId(componentId1)
        .withDonation(donation)
        .build();
    Date deliveryDate = new Date();
    Date collectionDate = new Date();
    BloodTransportBox box = aBloodTransportBox().withId(1L).withTemperature(0.0).build();
    ComponentBatch componentBatch = aComponentBatch()
        .withId(COMPONENT_BATCH_ID)
        .withStatus(ComponentBatchStatus.OPEN)
        .withBloodTransportBox(box)
        .withComponent(component)
        .withComponent(aComponent()
            .withId(componentId2)
            .withDonation(donation)
            .withParentComponent(component)
            .build())
        .withDeliveryDate(deliveryDate)
        .withCollectionDate(collectionDate)   
        .withDonationBatch(donationBatch)
        .withLocation(location)
        .build();
    
    // set up mocks
    ComponentFullViewModel componentFullViewModel = ComponentFullViewModelBuilder.aComponentFullViewModel().build();
    when(componentFactory.createComponentFullViewModels(componentBatch.getComponents())).thenReturn(Arrays.asList(componentFullViewModel));
    
    DonationBatchViewModel donationBatchViewModel = new DonationBatchViewModel();
    when(donationBatchViewModelFactory.createDonationBatchBasicViewModel(donationBatch)).thenReturn(donationBatchViewModel);
    
    BloodTransportBoxViewModel boxViewModel = new BloodTransportBoxViewModel();
    when(bloodTransportBoxViewModelFactory.createBloodTransportBoxViewModels(componentBatch.getBloodTransportBoxes()))
      .thenReturn(Arrays.asList(boxViewModel));
    
    // run test
    ComponentBatchFullViewModel viewModel =
        componentBatchFactory.createComponentBatchFullViewModel(componentBatch);
    
    // do asserts
    Assert.assertNotNull("View model returned", viewModel);
    Assert.assertEquals("View model is correct", COMPONENT_BATCH_ID, viewModel.getId());
    Assert.assertEquals("View model is correct", "OPEN", viewModel.getStatus());
    Assert.assertEquals("View model is correct", deliveryDate, viewModel.getDeliveryDate());
    Assert.assertEquals("View model is correct", collectionDate, viewModel.getCollectionDate());
    Assert.assertEquals("View model is correct", new LocationFullViewModel(location), viewModel.getLocation());
    Assert.assertNotNull("DonationBatchViewModel is set", viewModel.getDonationBatch());
    Assert.assertNotNull("ComponentViewModels are set", viewModel.getComponents());
    Assert.assertFalse("ComponentViewModels are set", viewModel.getComponents().isEmpty());
    Assert.assertNotNull("BloodTransportBoxViewModels are set", viewModel.getBloodTransportBoxes());
    Assert.assertFalse("BloodTransportBoxViewModels are set", viewModel.getBloodTransportBoxes().isEmpty());
    Assert.assertEquals("numberOfInitialComponents is correct", 1, viewModel.getNumberOfInitialComponents());
  }
  
  @Test
  public void testCreateComponentBatchViewModels_nullList() throws Exception {
    // run test
    List<ComponentBatchViewModel> viewModels =
        componentBatchFactory.createComponentBatchViewModels(null);
    
    // do asserts
    Assert.assertNotNull("View models list returned", viewModels);
    Assert.assertTrue("No view models defined", viewModels.isEmpty());
  }
  
  @Test
  public void testCreateComponentBatchViewModels_ComponentBatchList() throws Exception {
    // set up data
    UUID componentBatchId = UUID.randomUUID();
    List<ComponentBatch> componentBatches = new ArrayList<>();
    componentBatches.add(aComponentBatch().withId(COMPONENT_BATCH_ID).build());
    componentBatches.add(aComponentBatch().withId(componentBatchId).build());
    
    // set up mocks
    
    // run test
    List<ComponentBatchViewModel> viewModels =
        componentBatchFactory.createComponentBatchViewModels(componentBatches);
    
    // do asserts
    Assert.assertNotNull("View models list returned", viewModels);
    Assert.assertEquals("View models defined", 2, viewModels.size());
  }
}
