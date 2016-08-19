package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.builders.DivisionViewModelBuilder.aDivisionViewModel;
import static org.jembi.bsis.helpers.matchers.DivisionViewModelMatcher.hasSameStateAsDivisionViewModel;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

public class DivisionFactoryTests extends UnitTestSuite {
  
  @Spy
  @InjectMocks
  private DivisionFactory divisionFactory;
  
  @Test
  public void testCreateDivisionViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    long id = 769L;
    String name = "Some Location Division";
    int level = 1;
    
    Division division = aDivision().withId(id).withName(name).withLevel(level).build();
    
    // Set up expectations
    DivisionViewModel expectedViewModel = aDivisionViewModel().withId(id).withName(name).withLevel(level).build();
    
    // Exercise SUT
    DivisionViewModel returnedViewModel = divisionFactory.createDivisionViewModel(division);
    
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

}
