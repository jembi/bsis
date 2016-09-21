package org.jembi.bsis.factory;

import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModelBuilder;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.InventoryViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InventoryFactoryTests {

  @InjectMocks
  private InventoryFactory inventoryFactory;

  @Mock
  private ComponentTypeFactory componentTypeFactory;

  @Mock
  private LocationFactory locationFactory;

  @Test
  public void testCreateInventoryViewModel_shouldReturnViewModelWithTheCorrectState() {

    // Setup
    Date createdOn = new Date();
    Component component = aComponent()
        .withComponentType(ComponentTypeBuilder.aComponentType().withId(1L).build())
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(1L).build())
        .withCreatedOn(createdOn)
        .withExpiresOn(new Date())
        .build();   
    
    // Setup mocks
    LocationFullViewModel locationFullViewModel = new LocationFullViewModel(component.getLocation());
    when(locationFactory.createFullViewModel(component.getLocation()))
        .thenReturn(locationFullViewModel);

    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModelBuilder()
        .withId(1L)
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    // Run test
    InventoryViewModel viewModel = inventoryFactory.createViewModel(component);

    // Verify
    Assert.assertNotNull("view model was created", viewModel);
    Assert.assertEquals("location is correct", locationFullViewModel, viewModel.getLocation());
    Assert.assertEquals("inventory status is correct", component.getInventoryStatus(), viewModel.getInventoryStatus());
    Assert.assertEquals("id is correct", component.getId(), viewModel.getId());
    Assert.assertEquals("donationIdentificationNumber is correct", component.getDonationIdentificationNumber(),
        viewModel.getDonationIdentificationNumber());
    Assert.assertEquals("componentCode is correct", component.getComponentCode(), viewModel.getComponentCode());
    Assert.assertEquals("createdOn is correct", component.getCreatedOn(), viewModel.getCreatedOn());
    Assert.assertEquals("id is correct", component.getId(), viewModel.getId());
    Assert.assertEquals("componentType is correct", componentTypeViewModel, viewModel.getComponentType());

  }

  @Test
  public void testExpiryStatusWithNoExpiryDate_shouldBeEmpty() {

    // Setup
    Component component = aComponent()
        .withDonation(DonationBuilder.aDonation().build())
        .withExpiresOn(null).build();

    // Setup mocks
    LocationFullViewModel locationFullViewModel = new LocationFullViewModel(component.getLocation());
    when(locationFactory.createFullViewModel(component.getLocation())).thenReturn(locationFullViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModelBuilder()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    // Run test
    InventoryViewModel viewModel = inventoryFactory.createViewModel(component);

    // Verify
    Assert.assertEquals("correct expiry status", "", viewModel.getExpiryStatus());

  }

  @Test
  public void testExpiryStatusWithFutureExpiryDate_shouldReturnDaysUntilExpiry() {

    // Setup
    DateTime expiresOn = (new DateTime()).plusHours(99);
    Component component = aComponent()
        .withDonation(DonationBuilder.aDonation().build())
        .withExpiresOn(expiresOn.toDate()).build();

    // Setup mocks
    LocationFullViewModel locationFullViewModel = new LocationFullViewModel(component.getLocation());
    when(locationFactory.createFullViewModel(component.getLocation())).thenReturn(locationFullViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModelBuilder()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    // Run test
    InventoryViewModel viewModel = inventoryFactory.createViewModel(component);

    // Verify
    Assert.assertEquals("correct expiry status", "4 days to expire", viewModel.getExpiryStatus());

  }
  
  @Test
  public void testExpiryStatusWithPastExpiryDate_shouldReturnAlreadyExpiredMsg() {

    // Setup
    DateTime expiresOn = (new DateTime()).minusDays(20);
    Component component = aComponent()
        .withDonation(DonationBuilder.aDonation().build())
        .withExpiresOn(expiresOn.toDate()).build();

    // Setup mocks
    LocationFullViewModel locationFullViewModel = new LocationFullViewModel(component.getLocation());
    when(locationFactory.createFullViewModel(component.getLocation())).thenReturn(locationFullViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModelBuilder()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);

    // Run test
    InventoryViewModel viewModel = inventoryFactory.createViewModel(component);

    // Verify
    Assert.assertEquals("correct expiry status", "Already expired", viewModel.getExpiryStatus());

  }

}
