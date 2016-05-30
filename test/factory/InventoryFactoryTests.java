package factory;

import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import helpers.builders.ComponentBuilder;
import helpers.builders.ComponentTypeBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.LocationBuilder;
import model.component.Component;
import viewmodel.ComponentTypeViewModel;
import viewmodel.InventoryViewModel;
import viewmodel.LocationViewModel;

@RunWith(MockitoJUnitRunner.class)
public class InventoryFactoryTests {

  @InjectMocks
  private InventoryFactory inventoryFactory;

  @Mock
  private ComponentTypeFactory componentTypeFactory;

  @Mock
  private LocationViewModelFactory locationViewModelFactory;

  @Test
  public void testCreateInventoryViewModel_shouldReturnViewModelWithTheCorrectState() {

    // Setup
    Date createdOn = new Date();
    Component component = ComponentBuilder.aComponent()
        .withComponentType(ComponentTypeBuilder.aComponentType().withId(1L).build())
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(1L).build())
        .withCreatedOn(createdOn)
        .withExpiresOn(new Date())
        .build();   
    
    // Setup mocks
    LocationViewModel locationViewModel = new LocationViewModel(component.getLocation());
    when(locationViewModelFactory.createLocationViewModel(component.getLocation()))
        .thenReturn(locationViewModel);
    ComponentTypeViewModel componentTypeViewModel = new ComponentTypeViewModel(component.getComponentType());
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    // Run test
    InventoryViewModel viewModel = inventoryFactory.createInventoryViewModel(component);

    // Verify
    Assert.assertNotNull("view model was created", viewModel);
    Assert.assertEquals("location is correct", locationViewModel, viewModel.getLocation());
    Assert.assertEquals("inventory status is correct", component.getInventoryStatus(), viewModel.getInventoryStatus());
    Assert.assertEquals("id is correct", component.getId(), viewModel.getId());
    Assert.assertEquals("donationIdentificationNumber is correct", component.getDonationIdentificationNumber(),
        viewModel.getDonationIdentificationNumber());
    Assert.assertEquals("componentCode is correct", component.getComponentCode(), viewModel.getComponentCode());
    Assert.assertEquals("createdOn is correct", component.getCreatedOn(), viewModel.getCreatedOn());
    Assert.assertEquals("id is correct", component.getId(), viewModel.getId());
    Assert.assertEquals("componentType is correct", componentTypeViewModel, viewModel.getComponentType());

  }

}
