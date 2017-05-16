package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.PackTypeBackingFormBuilder.aPackTypeBackingForm;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.PackTypeFullViewModelBuilder.aPackTypeViewFullModel;
import static org.jembi.bsis.helpers.matchers.PackTypeMatcher.hasSameStateAsPackType;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.factory.PackTypeFactory;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class PackTypeControllerServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private PackTypeControllerService packTypeControllerService;
  @Mock
  private PackTypeRepository packTypeRepository;
  @Mock
  private PackTypeFactory packTypeFactory;
  
  @Test
  public void testGetAllPackTypes_shouldFindAndReturnAllPackTypes() {
    // Set up
    UUID packTypeId1 = UUID.randomUUID();
    UUID packTypeId2 = UUID.randomUUID();
    List<PackType> packTypes = Arrays.asList(
        aPackType().withId(packTypeId1).build(), 
        aPackType().withId(packTypeId2).build());
    List<PackTypeFullViewModel> expectedPackTypeViewModels = Arrays.asList(
        aPackTypeViewFullModel().withId(packTypeId1).build(),
        aPackTypeViewFullModel().withId(packTypeId2).build());

    // Mock
    when(packTypeRepository.getAllPackTypes()).thenReturn(packTypes);
    when(packTypeFactory.createFullViewModels(packTypes)).thenReturn(expectedPackTypeViewModels);
    
    // Exercise SUT
    List<PackTypeFullViewModel> returnedPackTypes = packTypeControllerService.getAllPackTypes();
    
    // Verify
    assertThat(returnedPackTypes, is(expectedPackTypeViewModels));
  }
  
  @Test
  public void testGetPackTypeById_shouldFindAndReturnPackType() {
    // Set up
    UUID packTypeId = UUID.randomUUID();
    PackType packType = aPackType().withId(packTypeId).build();
    PackTypeFullViewModel expectedPackTypeViewModel = aPackTypeViewFullModel().withId(packTypeId).build();
    
    // Mock
    when(packTypeRepository.getPackTypeById(packTypeId)).thenReturn(packType);
    when(packTypeFactory.createFullViewModel(packType)).thenReturn(expectedPackTypeViewModel);
    
    // Exercise SUT
    PackTypeFullViewModel returnedPackTypeViewModel = packTypeControllerService.getPackTypeById(packTypeId);
    
    // Verify
    assertThat(returnedPackTypeViewModel, is(expectedPackTypeViewModel));
  }
  
  @Test
  public void testCreatePackType_shouldCreateAndReturnPackType() {
    // Set up
    UUID packTypeId = UUID.randomUUID();
    PackTypeBackingForm packTypeBackingForm = aPackTypeBackingForm().withPackType("Test").build();
    PackType packTypeFromBackingForm = aPackType().withPackType("Test").build();
    PackType createdPackType = aPackType().withId(packTypeId).withPackType("Test").build();
    PackTypeFullViewModel expectedPackTypeViewModel = aPackTypeViewFullModel().withPackType("Test").build();
    
    // Mock
    when(packTypeFactory.createEntity(packTypeBackingForm)).thenReturn(packTypeFromBackingForm);
    when(packTypeRepository.savePackType(argThat(hasSameStateAsPackType(packTypeFromBackingForm)))).thenReturn(createdPackType);
    when(packTypeFactory.createFullViewModel(createdPackType)).thenReturn(expectedPackTypeViewModel);
    
    // Exercise SUT
    PackTypeFullViewModel returnedPackTypeViewModel = packTypeControllerService.createPackType(packTypeBackingForm);
    
    // Verify
    assertThat(returnedPackTypeViewModel, is(expectedPackTypeViewModel));
  }
  
  @Test
  public void testupdatePackType_shouldUpdateAndReturnPackType() {
    // Set up
    UUID packTypeId = UUID.randomUUID();
    PackTypeBackingForm packTypeBackingForm = aPackTypeBackingForm().withPackType("Test").build();
    PackType packTypeFromBackingForm = aPackType().withId(packTypeId).withPackType("Test").build();
    PackType updatedPackType = aPackType().withId(packTypeId).withPackType("Test").build();
    PackTypeFullViewModel expectedPackTypeViewModel = aPackTypeViewFullModel().withPackType("Test").build();
    
    // Mock
    when(packTypeFactory.createEntity(packTypeBackingForm)).thenReturn(packTypeFromBackingForm);
    when(packTypeRepository.updatePackType(argThat(hasSameStateAsPackType(packTypeFromBackingForm)))).thenReturn(updatedPackType);
    when(packTypeFactory.createFullViewModel(updatedPackType)).thenReturn(expectedPackTypeViewModel);
    
    // Exercise SUT
    PackTypeFullViewModel returnedPackTypeViewModel = packTypeControllerService.updatePackType(packTypeBackingForm);
    
    // Verify
    assertThat(returnedPackTypeViewModel, is(expectedPackTypeViewModel));
  }

}
