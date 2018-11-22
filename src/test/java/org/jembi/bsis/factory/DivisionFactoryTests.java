package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBackingFormBuilder.aDivisionBackingForm;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.builders.DivisionFullViewModelBuilder.aDivisionFullViewModel;
import static org.jembi.bsis.helpers.builders.DivisionViewModelBuilder.aDivisionViewModel;
import static org.jembi.bsis.helpers.matchers.DivisionFullViewModelMatcher.hasSameStateAsDivisionFullViewModel;
import static org.jembi.bsis.helpers.matchers.DivisionMatcher.hasSameStateAsDivision;
import static org.jembi.bsis.helpers.matchers.DivisionViewModelMatcher.hasSameStateAsDivisionViewModel;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.service.DivisionConstraintChecker;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DivisionFullViewModel;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class DivisionFactoryTests extends UnitTestSuite {
  
  @Spy
  @InjectMocks
  private DivisionFactory divisionFactory;
  @Mock
  private DivisionRepository divisionRepository;
  @Mock
  private DivisionConstraintChecker divisionConstraintChecker;
  
  @Test
  public void testCreateDivisionViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();
    String divisionName = "Some Location Division";
    int divisionLevel = 2;

    UUID parentDivisionId = UUID.randomUUID();
    String parentDivisionName = "Parent Division";
    int parentDivisionLevel = 1;
    
    Division division = aDivision()
        .withId(divisionId)
        .withName(divisionName)
        .withLevel(divisionLevel)
        .withParent(aDivision()
            .withId(parentDivisionId)
            .withName(parentDivisionName)
            .withLevel(parentDivisionLevel)
            .build())
        .build();
    
    // Set up expectations
    DivisionViewModel expectedViewModel = aDivisionViewModel()
        .withId(divisionId)
        .withName(divisionName)
        .withLevel(divisionLevel)
        .withParent(aDivisionViewModel()
            .withId(parentDivisionId)
            .withName(parentDivisionName)
            .withLevel(parentDivisionLevel)
            .build())
        .build();
    
    // Exercise SUT
    DivisionViewModel returnedViewModel = divisionFactory.createDivisionViewModel(division);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsDivisionViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateDivisionFullViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();
    String divisionName = "Some Location Division";
    int divisionLevel = 2;

    UUID parentDivisionId = UUID.randomUUID();
    String parentDivisionName = "Parent Division";
    int parentDivisionLevel = 1;
    
    Division division = aDivision()
        .withId(divisionId)
        .withName(divisionName)
        .withLevel(divisionLevel)
        .withParent(aDivision()
            .withId(parentDivisionId)
            .withName(parentDivisionName)
            .withLevel(parentDivisionLevel)
            .build())
        .build();
    
    // Set up expectations
    DivisionFullViewModel expectedViewModel = aDivisionFullViewModel()
        .withId(divisionId)
        .withName(divisionName)
        .withLevel(divisionLevel)
        .withParent(aDivisionViewModel()
            .withId(parentDivisionId)
            .withName(parentDivisionName)
            .withLevel(parentDivisionLevel)
            .build())
        .withPermission("canEditLevel", true)
        .build();
    
    when(divisionConstraintChecker.canEditLevel(division)).thenReturn(true);
    
    // Exercise SUT
    DivisionFullViewModel returnedViewModel = divisionFactory.createDivisionFullViewModel(division);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsDivisionFullViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateDivisionViewModelWithNullParent_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();
    String name = "Some Location Division";
    int level = 1;
    
    Division division = aDivision()
        .withId(divisionId)
        .withName(name)
        .withLevel(level)
        .withParent(null)
        .build();
    
    // Set up expectations
    DivisionViewModel expectedViewModel = aDivisionViewModel()
        .withId(divisionId)
        .withName(name)
        .withLevel(level)
        .withParent(null)
        .build();
    
    // Exercise SUT
    DivisionViewModel returnedViewModel = divisionFactory.createDivisionViewModel(division);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsDivisionViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateDivisionViewModelExcludeParent_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();
    String divisionName = "Some Location Division";
    int divisionLevel = 2;
    
    Division division = aDivision()
        .withId(divisionId)
        .withName(divisionName)
        .withLevel(divisionLevel)
        .withParent(aDivision()
            .withId(UUID.randomUUID())
            .withName("Excluded parent")
            .withLevel(1)
            .build())
        .build();
    
    // Set up expectations
    DivisionViewModel expectedViewModel = aDivisionViewModel()
        .withId(divisionId)
        .withName(divisionName)
        .withLevel(divisionLevel)
        .withParent(null)
        .build();
    
    // Exercise SUT
    DivisionViewModel returnedViewModel = divisionFactory.createDivisionViewModel(division, false);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsDivisionViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateDivisionViewModels_shouldReturnExpectedViewModels() {
    // Set up fixture
    UUID divisionId1 = UUID.randomUUID();
    UUID divisionId2 = UUID.randomUUID();
    Division firstDivision = aDivision().withId(divisionId1).withName("First").withLevel(1).build();
    Division secondDivision = aDivision().withId(divisionId2).withName("Second").withLevel(2).build();
    List<Division> divisions = Arrays.asList(firstDivision, secondDivision);
    
    // Set up expectations
    DivisionViewModel firstViewModel = aDivisionViewModel().withId(divisionId1).withName("First").withLevel(1).build();
    DivisionViewModel secondViewModel = aDivisionViewModel().withId(divisionId2).withName("Second").withLevel(2).build();
    List<DivisionViewModel> expectedViewModels = Arrays.asList(firstViewModel, secondViewModel);
    
    doReturn(firstViewModel).when(divisionFactory).createDivisionViewModel(firstDivision);
    doReturn(secondViewModel).when(divisionFactory).createDivisionViewModel(secondDivision);
    
    // Exercise SUT
    List<DivisionViewModel> returnedViewModels = divisionFactory.createDivisionViewModels(divisions);
    
    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }

  @Test
  public void testConvertDivisionBackingFormToDivisionEntity_shouldReturnExpectedEntity() {
    UUID divisionId = UUID.randomUUID();
    String name = "aDiv";
    Integer level = 1;

    Division expectedEntity = aDivision()
        .withId(divisionId)
        .withLevel(level)
        .withName(name)
        .build();

    DivisionBackingForm form = aDivisionBackingForm()
        .withId(divisionId)
        .withName(name)
        .withLevel(level)
        .build();

    Division convertedEntity = divisionFactory.createEntity(form);

    Assert.assertNotNull("Entity was created", convertedEntity);
    assertThat(convertedEntity, hasSameStateAsDivision(expectedEntity));
  }

  @Test
  public void testConvertDivisionBackingFormWithParentToDivisionEntityWithParent_shouldReturnExpectedEntity() {
    UUID parentId = UUID.randomUUID();
    UUID id = UUID.randomUUID();

    String parentName = "parentName";
    String name = "aDiv";

    int parentLevel = 1;
    int level = 2;

    Division parent = aDivision()
        .withLevel(parentLevel)
        .withName(parentName)
        .build();

    Division expectedEntity = aDivision()
        .withId(id)
        .withLevel(level)
        .withName(name)
        .withParent(parent)
        .build();

    DivisionBackingForm parentForm = aDivisionBackingForm()
        .withId(parentId)
        .withName(parentName)
        .withLevel(parentLevel)
        .build();

    DivisionBackingForm form = aDivisionBackingForm()
        .withId(id)
        .withName(name)
        .withLevel(level)
        .withParent(parentForm)
        .build();

    when(divisionRepository.findDivisionById(parentId)).thenReturn(parent);

    Division convertedEntity = divisionFactory.createEntity(form);

    Assert.assertNotNull("Entity was created", convertedEntity);
    assertThat(convertedEntity, hasSameStateAsDivision(expectedEntity));
  }

}

