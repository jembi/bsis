package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.PackTypeBackingFormBuilder.aPackTypeBackingForm;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.PackTypeFullViewModelBuilder.aPackTypeViewFullModel;
import static org.jembi.bsis.helpers.matchers.PackTypeFullViewModelMatcher.hasSameStateAsPackTypeViewFullModel;
import static org.jembi.bsis.helpers.matchers.PackTypeMatcher.hasSameStateAsPackType;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class PackTypeFactoryTests extends UnitTestSuite {

  @InjectMocks
  private PackTypeFactory packTypeFactory;
  @Mock
  private ComponentTypeFactory componentTypeFactory;

  @Test
  public void testConvertEntityToPackTypeFullViewModel_shouldReturnExpectedViewModel() {
    UUID packTypeId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    ComponentType componentType = ComponentTypeBuilder.aComponentType().withId(componentTypeId).build();
    
    ComponentTypeViewModel componentTypeViewModel = ComponentTypeViewModelBuilder.aComponentTypeViewModel()
        .withId(componentType.getId())
        .build();
    
    PackTypeFullViewModel expectedViewModel = aPackTypeViewFullModel()
        .withComponentType(componentTypeViewModel)
        .withPackType("Single")
        .withPeriodBetweenDonations(90)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .withMinWeight(222)
        .withMaxWeight(999)
        .withLowVolumeWeight(555)
        .withId(packTypeId)
        .build();

    PackType packType = PackTypeBuilder.aPackType()
        .withComponentType(componentType)
        .withPackType("Single")
        .withPeriodBetweenDonations(90)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .withMinWeight(222)
        .withMaxWeight(999)
        .withLowVolumeWeight(555)
        .withId(packTypeId)
        .build();

    // Setup mocks
    when(componentTypeFactory.createViewModel(componentType)).thenReturn(componentTypeViewModel);

    PackTypeFullViewModel convertedViewModel = packTypeFactory.createFullViewModel(packType);

    assertThat(convertedViewModel, hasSameStateAsPackTypeViewFullModel(expectedViewModel));
  }
  
  @Test
  public void testCreateEntity_shouldReturnEntityWithTheCorrectState() {
    // Set up fixture
    UUID packTypeId = UUID.randomUUID();
    String packTypeName = "Name";
    UUID componentTypeId = UUID.randomUUID();
    Boolean canPool = true;
    Boolean canSplit = true;
    Boolean isDeleted = false;
    Boolean countAsDonation = true;
    Boolean testSampleProduced = false;
    Integer periodBetweenDonations = 56;
    
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    
    PackTypeBackingForm packTypeBackingForm = aPackTypeBackingForm()
        .withId(packTypeId)
        .withPackType(packTypeName)
        .withComponentType(componentTypeBackingForm)
        .withCanPool(canPool)
        .withCanSplit(canSplit)
        .withIsDeleted(isDeleted)
        .withCountAsDonation(countAsDonation)
        .withTestSampleProduced(testSampleProduced)
        .withPeriodBetweenDonations(periodBetweenDonations)
        .build();
    
    // Set up expectations
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    
    PackType expectedPackType = aPackType()
        .withId(packTypeId)
        .withPackType(packTypeName)
        .withComponentType(componentType)
        .withCanPool(canPool)
        .withCanSplit(canSplit)
        .withIsDeleted(isDeleted)
        .withCountAsDonation(countAsDonation)
        .withTestSampleProduced(testSampleProduced)
        .withPeriodBetweenDonations(periodBetweenDonations)
        .build();
    
    when(componentTypeFactory.createEntity(componentTypeBackingForm)).thenReturn(componentType);
    
    // Exercise SUT
    PackType returnedPackType = packTypeFactory.createEntity(packTypeBackingForm);
    
    // Verify
    assertThat(returnedPackType, hasSameStateAsPackType(expectedPackType));
  }

}
