package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.PackTypeBackingFormBuilder.aPackTypeBackingForm;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.PackTypeViewFullModelBuilder.aPackTypeViewFullModel;
import static org.jembi.bsis.helpers.matchers.PackTypeMatcher.hasSameStateAsPackType;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.factory.PackTypeFactory;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.PackTypeViewFullModel;
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
    List<PackType> packTypes = Arrays.asList(aPackType().withId(1L).build(), aPackType().withId(2L).build());
    List<PackTypeViewFullModel> expectedPackTypeViewModels = Arrays.asList(aPackTypeViewFullModel().withId(1L).build(),
        aPackTypeViewFullModel().withId(2L).build());

    // Mock
    when(packTypeRepository.getAllPackTypes()).thenReturn(packTypes);
    when(packTypeFactory.createFullViewModels(packTypes)).thenReturn(expectedPackTypeViewModels);
    
    // Exercise SUT
    List<PackTypeViewFullModel> returnedPackTypes = packTypeControllerService.getAllPackTypes();
    
    // Verify
    assertThat(returnedPackTypes, is(expectedPackTypeViewModels));
  }
  
  @Test
  public void testGetPackTypeById_shouldFindAndReturnPackType() {
    // Set up
    long packTypeId = 88L;
    PackType packType = aPackType().withId(packTypeId).build();
    PackTypeViewFullModel expectedPackTypeViewModel = aPackTypeViewFullModel().withId(packTypeId).build();
    
    // Mock
    when(packTypeRepository.getPackTypeById(packTypeId)).thenReturn(packType);
    when(packTypeFactory.createFullViewModel(packType)).thenReturn(expectedPackTypeViewModel);
    
    // Exercise SUT
    PackTypeViewFullModel returnedPackTypeViewModel = packTypeControllerService.getPackTypeById(packTypeId);
    
    // Verify
    assertThat(returnedPackTypeViewModel, is(expectedPackTypeViewModel));
  }
  
  @Test
  public void testCreatePackType_shouldCreateAndReturnPackType() {
    // Set up
    PackTypeBackingForm packTypeBackingForm = aPackTypeBackingForm().withPackType("Test").build();
    PackType packTypeFromBackingForm = aPackType().withPackType("Test").build();
    PackType createdPackType = aPackType().withId(1L).withPackType("Test").build();
    PackTypeViewFullModel expectedPackTypeViewModel = aPackTypeViewFullModel().withPackType("Test").build();
    
    // Mock
    when(packTypeFactory.createEntity(packTypeBackingForm)).thenReturn(packTypeFromBackingForm);
    when(packTypeRepository.savePackType(argThat(hasSameStateAsPackType(packTypeFromBackingForm)))).thenReturn(createdPackType);
    when(packTypeFactory.createFullViewModel(createdPackType)).thenReturn(expectedPackTypeViewModel);
    
    // Exercise SUT
    PackTypeViewFullModel returnedPackTypeViewModel = packTypeControllerService.createPackType(packTypeBackingForm);
    
    // Verify
    assertThat(returnedPackTypeViewModel, is(expectedPackTypeViewModel));
  }
  
  @Test
  public void testupdatePackType_shouldUpdateAndReturnPackType() {
    // Set up
    PackTypeBackingForm packTypeBackingForm = aPackTypeBackingForm().withPackType("Test").build();
    PackType packTypeFromBackingForm = aPackType().withId(1L).withPackType("Test").build();
    PackType updatedPackType = aPackType().withId(1L).withPackType("Test").build();
    PackTypeViewFullModel expectedPackTypeViewModel = aPackTypeViewFullModel().withPackType("Test").build();
    
    // Mock
    when(packTypeFactory.createEntity(packTypeBackingForm)).thenReturn(packTypeFromBackingForm);
    when(packTypeRepository.updatePackType(argThat(hasSameStateAsPackType(packTypeFromBackingForm)))).thenReturn(updatedPackType);
    when(packTypeFactory.createFullViewModel(updatedPackType)).thenReturn(expectedPackTypeViewModel);
    
    // Exercise SUT
    PackTypeViewFullModel returnedPackTypeViewModel = packTypeControllerService.updatePackType(packTypeBackingForm);
    
    // Verify
    assertThat(returnedPackTypeViewModel, is(expectedPackTypeViewModel));
  }

}
