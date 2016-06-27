package org.jembi.bsis.factory;

import static org.jembi.bsis.helpers.builders.BloodTransportBoxBuilder.aBloodTransportBox;
import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.helpers.builders.ComponentViewModelBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.BloodTransportBox;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.BloodTransportBoxViewModel;
import org.jembi.bsis.viewmodel.ComponentBatchFullViewModel;
import org.jembi.bsis.viewmodel.ComponentBatchViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentBatchViewModelFactoryTests {

  @InjectMocks
  ComponentBatchViewModelFactory componentBatchViewModelFactory;
  @Mock
  BloodTransportBoxViewModelFactory bloodTransportBoxViewModelFactory;
  @Mock
  ComponentViewModelFactory componentViewModelFactory;
  @Mock
  DonationBatchViewModelFactory donationBatchViewModelFactory;
  
  @Test
  public void testCreateComponentBatchViewModel_createOneComponentBatch() throws Exception {
    // set up data
    Location venue = aVenue().withId(1L).withName("venue").build();
    Location location = aLocation().withId(2L).withName("distribution site").thatIsUsageSite().build();
    DonationBatch donationBatch = aDonationBatch()
        .withId(1L)
        .withBatchNumber("BN1234")
        .withVenue(venue)
        .build();
    Donation donation = aDonation()
        .withId(1L)
        .withDonationIdentificationNumber("DIN1234")
        .withDonationBatch(donationBatch)
        .build();
    donationBatch.setDonation(Arrays.asList(donation));
    Component component = aComponent()
        .withId(1L)
        .withDonation(donation)
        .build();
    Date deliveryDate = new Date();
    Date collectionDate = new Date();
    BloodTransportBox box = aBloodTransportBox().withId(1L).withTemperature(0.0).build();
    ComponentBatch componentBatch = aComponentBatch()
        .withId(1L)
        .withStatus(ComponentBatchStatus.OPEN)
        .withBloodTransportBox(box)
        .withComponent(component)
        .withDeliveryDate(deliveryDate)
        .withCollectionDate(collectionDate)   
        .withDonationBatch(donationBatch)
        .withLocation(location)
        .build();
    
    // set up mocks
    ComponentViewModel componentViewModel = ComponentViewModelBuilder.aComponentViewModel().build();
    when(componentViewModelFactory.createComponentViewModels(componentBatch.getComponents())).thenReturn(Arrays.asList(componentViewModel));
    
    DonationBatchViewModel donationBatchViewModel = new DonationBatchViewModel();
    when(donationBatchViewModelFactory.createDonationBatchBasicViewModel(donationBatch)).thenReturn(donationBatchViewModel);
    
    BloodTransportBoxViewModel boxViewModel = new BloodTransportBoxViewModel();
    when(bloodTransportBoxViewModelFactory.createBloodTransportBoxViewModels(componentBatch.getBloodTransportBoxes()))
      .thenReturn(Arrays.asList(boxViewModel));
    
    // run test
    ComponentBatchFullViewModel viewModel =
        componentBatchViewModelFactory.createComponentBatchFullViewModel(componentBatch);
    
    // do asserts
    Assert.assertNotNull("View model returned", viewModel);
    Assert.assertEquals("View model is correct", Long.valueOf(1), viewModel.getId());
    Assert.assertEquals("View model is correct", "OPEN", viewModel.getStatus());
    Assert.assertEquals("View model is correct", deliveryDate, viewModel.getDeliveryDate());
    Assert.assertEquals("View model is correct", collectionDate, viewModel.getCollectionDate());
    Assert.assertEquals("View model is correct", new LocationViewModel(location), viewModel.getLocation());
    Assert.assertNotNull("DonationBatchViewModel is set", viewModel.getDonationBatch());
    Assert.assertNotNull("ComponentViewModels are set", viewModel.getComponents());
    Assert.assertFalse("ComponentViewModels are set", viewModel.getComponents().isEmpty());
    Assert.assertNotNull("BloodTransportBoxViewModels are set", viewModel.getBloodTransportBoxes());
    Assert.assertFalse("BloodTransportBoxViewModels are set", viewModel.getBloodTransportBoxes().isEmpty());
  }
  
  @Test
  public void testCreateComponentBatchViewModels_nullList() throws Exception {
    // run test
    List<ComponentBatchViewModel> viewModels =
        componentBatchViewModelFactory.createComponentBatchViewModels(null);
    
    // do asserts
    Assert.assertNotNull("View models list returned", viewModels);
    Assert.assertTrue("No view models defined", viewModels.isEmpty());
  }
  
  @Test
  public void testCreateComponentBatchViewModels_ComponentBatchList() throws Exception {
    // set up data
    List<ComponentBatch> componentBatches = new ArrayList<>();
    componentBatches.add(aComponentBatch().withId(1L).build());
    componentBatches.add(aComponentBatch().withId(2L).build());
    
    // set up mocks
    
    // run test
    List<ComponentBatchViewModel> viewModels =
        componentBatchViewModelFactory.createComponentBatchViewModels(componentBatches);
    
    // do asserts
    Assert.assertNotNull("View models list returned", viewModels);
    Assert.assertEquals("View models defined", 2, viewModels.size());
  }
}
