package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.mockito.Mockito.when;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DivisionConstraintCheckerTests extends UnitTestSuite {
  
  @InjectMocks
  private DivisionConstraintChecker divisionConstraintChecker;
  @Mock
  private DivisionRepository divisionRepository;
  
  @Test
  public void testCanEditLevelWithNoChildren_shouldReturnTrue() {
    Division division = aDivision().withId(8L).build();
    
    when(divisionRepository.countDivisionsByParent(division)).thenReturn(0L);
    
    boolean canEditLevel = divisionConstraintChecker.canEditLevel(division);
    
    assertThat(canEditLevel, is(true));
  }
  
  @Test
  public void testCanEditLevelWithChildren_shouldReturnFalse() {
    Division division = aDivision().withId(7L).build();
    
    when(divisionRepository.countDivisionsByParent(division)).thenReturn(1L);
    
    boolean canEditLevel = divisionConstraintChecker.canEditLevel(division);
    
    assertThat(canEditLevel, is(false));
  }

}
