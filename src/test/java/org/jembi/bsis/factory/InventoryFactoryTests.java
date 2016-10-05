package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.builders.InventoryViewModelBuilder.anInventoryViewModel;
import static org.jembi.bsis.helpers.matchers.InventoryViewModelMatcher.hasSameStateAsInventoryViewModel;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
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
  public void testCreateInventoryViewModel_shouldReturnViewModelWithTheCorrectStateAndNoExpiryStatus() {

    // Setup
    Date createdOn = new Date();
    ComponentType aComponentType = aComponentType().withId(1L).build();
    Component component = aComponent()
        .withComponentType(aComponentType)
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(1L).build())
        .withCreatedOn(createdOn)
        .build();   
    
    // Setup mocks
    LocationFullViewModel locationFullViewModel = new LocationFullViewModel(component.getLocation());
    when(locationFactory.createFullViewModel(component.getLocation()))
        .thenReturn(locationFullViewModel);

    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .withId(1L)
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withLocation(locationFullViewModel)
          .withInventoryStatus(component.getInventoryStatus())
          .withId(component.getId())
          .withDonationIdentificationNumber(component.getDonationIdentificationNumber())
          .withComponentCode(component.getComponentCode())
          .withCreatedOn(createdOn)
          .withComponentType(componentTypeViewModel)
          .withExpiryStatus("")
          .build();
    // Run test
    InventoryViewModel createdInventoryviewModel = inventoryFactory.createViewModel(component);
    
    // Verify
    assertThat(createdInventoryviewModel, hasSameStateAsInventoryViewModel(expectedInventoryViewModel));
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
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
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
    Date expiresOn = new DateTime().plusHours(99).toDate();
    Component component = aComponent().withExpiresOn(expiresOn).build();

    // Setup mocks
    LocationFullViewModel locationFullViewModel = new LocationFullViewModel(component.getLocation());
    when(locationFactory.createFullViewModel(component.getLocation())).thenReturn(locationFullViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withInventoryStatus(component.getInventoryStatus())
          .withExpiresOn(expiresOn)
          .withExpiryStatus("4 days to expire")
          .withComponentType(componentTypeViewModel)
          .withLocation(locationFullViewModel)
          .build();

    // Run test
    InventoryViewModel createdInventoryviewModel = inventoryFactory.createViewModel(component);
    
    // Verify
    assertThat(createdInventoryviewModel, hasSameStateAsInventoryViewModel(expectedInventoryViewModel));
  }
  
  @Test
  public void testExpiryStatusWithPastExpiryDate_shouldReturnAlreadyExpiredMsg() {
    // Setup
    Date expiresOn = new DateTime().minusDays(20).toDate();
    Component component = aComponent().withExpiresOn(expiresOn).build();


    // Setup mocks
    LocationFullViewModel locationFullViewModel = new LocationFullViewModel(component.getLocation());
    when(locationFactory.createFullViewModel(component.getLocation())).thenReturn(locationFullViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withInventoryStatus(component.getInventoryStatus())
          .withExpiresOn(expiresOn)
          .withExpiryStatus("Already expired")
          .withComponentType(componentTypeViewModel)
          .withLocation(locationFullViewModel)
          .build();

    // Run test
    InventoryViewModel createdInventoryviewModel = inventoryFactory.createViewModel(component);

    // Verify
    assertThat(createdInventoryviewModel, hasSameStateAsInventoryViewModel(expectedInventoryViewModel));
  }

}
