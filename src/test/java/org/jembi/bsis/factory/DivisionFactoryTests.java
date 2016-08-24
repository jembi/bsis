package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBackingFormBuilder.aDivisionBackingForm;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.builders.DivisionViewModelBuilder.aDivisionViewModel;
import static org.jembi.bsis.helpers.matchers.DivisionViewModelMatcher.hasSameStateAsDivisionViewModel;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.suites.UnitTestSuite;
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

  
  @Test
  public void testCreateDivisionViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    long divisionId = 769L;
    String divisionName = "Some Location Division";
    int divisionLevel = 2;

    long parentDivisionId = 7L;
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
  public void testCreateDivisionViewModelWithNullParent_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    long divisionId = 769L;
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
    long divisionId = 769L;
    String divisionName = "Some Location Division";
    int divisionLevel = 2;
    
    Division division = aDivision()
        .withId(divisionId)
        .withName(divisionName)
        .withLevel(divisionLevel)
        .withParent(aDivision()
            .withId(9L)
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
    Division firstDivision = aDivision().withId(1L).withName("First").withLevel(1).build();
    Division secondDivision = aDivision().withId(3L).withName("Second").withLevel(2).build();
    List<Division> divisions = Arrays.asList(firstDivision, secondDivision);
    
    // Set up expectations
    DivisionViewModel firstViewModel = aDivisionViewModel().withId(1L).withName("First").withLevel(1).build();
    DivisionViewModel secondViewModel = aDivisionViewModel().withId(3L).withName("Second").withLevel(2).build();
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
    long id = 5L;
    String name = "aDiv";
    int level = 1;
    DivisionBackingForm form = aDivisionBackingForm()
        .withId(id)
        .withName(name)
        .withLevel(level)
        .build();

    Division convertedEntity = divisionFactory.createEntity(form);

    Assert.assertNotNull("Entity was created", convertedEntity);
    Assert.assertEquals("Entity id is correct", id, form.getId());
    Assert.assertEquals("Entity name is correct", name, form.getName());
    Assert.assertEquals("Entity level is correct", level, form.getLevel());
    Assert.assertNull("Entity parent is null", form.getParent());
    
  }

  @Test
  public void testConvertDivisionBackingFormWithParentToDivisionEntityWithParent_shouldReturnExpectedEntity() {
    long parentId = 4L;
    long id = 5L;

    String parentName = "parentName";
    String name = "aDiv";

    int parentLevel = 1;
    int level = 2;

    Division parent = aDivision()
        .withLevel(parentLevel)
        .withName(parentName)
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

    when(divisionRepository.findDivisionById(4L)).thenReturn(parent);

    Division convertedEntity = divisionFactory.createEntity(form);

    Assert.assertNotNull("Entity was created", convertedEntity);
    Assert.assertEquals("Entity id is correct",  Long.valueOf(id), convertedEntity.getId());
    Assert.assertEquals("Entity name is correct", name, convertedEntity.getName());
    Assert.assertEquals("Entity level is correct", level, convertedEntity.getLevel());

    Division convertedEntityParent = convertedEntity.getParent();

    Assert.assertNotNull("Entity Parent was set", convertedEntityParent);
    Assert.assertEquals("Entity parent name is correct", parentName, convertedEntityParent.getName());
    Assert.assertEquals("Entity parent level is correct", parentLevel, convertedEntityParent.getLevel());
    Assert.assertNull("Entity parent's parent is not set", convertedEntityParent.getParent());
  }

}

