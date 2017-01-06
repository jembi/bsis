package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;

public class ComponentVolumeServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private ComponentVolumeService componentVolumeService;
  
  @Test
  public void testCalculateVolumeWithGavityAndWeightSet_shouldReturnCorrectResult() {
    Location location = aLocation().withId(1L).build();
    Donation donation = aDonation().withId(1L).build();
    Double gravity = 1.001;
    Integer weight = 95;
    ComponentType componentType = aComponentType()
        .withId(1L)
        .withGravity(gravity)
        .build();

    Component component = aComponent()
        .withId(1L)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withWeight(weight)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .build();

    // Verify
    Integer volume = componentVolumeService.calculateVolume(component); 
    assertThat(volume, is(95));
  }
  
  @Test
  public void testCalculateVolumeWithNULLWieghtAndNULLGravity_shouldReturnNULL() {
    Location location = aLocation().withId(1L).build();
    Donation donation = aDonation().withId(1L).build();
    ComponentType componentType = aComponentType()
        .withId(1L)
        .withGravity(null)
        .build();

    Component component = aComponent()
        .withId(1L)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withWeight(null)
        .build();

    // Verify
    assertThat(componentVolumeService.calculateVolume(component), is(nullValue()));
  }
  
  @Test
  public void testCalculateVolumeWithNULLWieght_shouldReturnNULL() {
    Location location = aLocation().withId(1L).build();
    Donation donation = aDonation().withId(1L).build();
    ComponentType componentType = aComponentType()
        .withId(1L)
        .withGravity(1.001)
        .build();

    Component component = aComponent()
        .withId(1L)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withWeight(null)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .build();

    // Verify
    assertThat(componentVolumeService.calculateVolume(component), is(nullValue()));
  }
  
  @Test
  public void testCalculateVolumeWithNULLGravity_shouldReturnNULL() {
    Location location = aLocation().withId(1L).build();
    Donation donation = aDonation().withId(1L).build();
    ComponentType componentType = aComponentType()
        .withId(1L)
        .withGravity(null)
        .build();

    Component component = aComponent()
        .withId(1L)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withWeight(95)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .build();

    // Verify
    assertThat(componentVolumeService.calculateVolume(component), is(nullValue()));
  }
}