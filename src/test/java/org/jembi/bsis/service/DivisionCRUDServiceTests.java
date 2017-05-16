package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.matchers.DivisionMatcher.hasSameStateAsDivision;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DivisionCRUDServiceTests extends UnitTestSuite {

  @InjectMocks
  private DivisionCRUDService divisionCRUDService;
  @Mock
  private DivisionRepository divisionRepository;
  @Mock
  private DivisionConstraintChecker divisionConstraintChecker;

  @Test
  public void testCreateDivision_shouldSaveDivisionCorrectly() {
    String name = "aDiv";
    int level = 2;

    Division parentDivision = aDivision().build();

    Division expectedEntity = aDivision()
        .withLevel(level)
        .withName(name)
        .withParent(parentDivision)
        .build();

    Division createdDivision = divisionCRUDService.createDivision(expectedEntity);

    verify(divisionRepository).save(argThat(hasSameStateAsDivision(expectedEntity)));

    assertThat(createdDivision, is(expectedEntity));
  }
  
  @Test
  public void testUpdateDivision_shouldSetCorrectFieldsAndUpdate() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();

    Division existingDivision = aDivision()
        .withId(divisionId)
        .withName("Existing")
        .withLevel(1)
        .withParent(null)
        .build();
    
    Division updatedDivision = aDivision()
        .withId(divisionId)
        .withName("Updated")
        .withLevel(2)
        .withParent(aDivision().withId(UUID.randomUUID()).build())
        .build();
    
    // Set up expectations
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(existingDivision);
    when(divisionConstraintChecker.canEditLevel(existingDivision)).thenReturn(true);
    when(divisionRepository.update(any(Division.class))).thenAnswer(returnsFirstArg());
    
    // Exercise SUT
    Division returnedDivision = divisionCRUDService.updateDivision(updatedDivision);
    
    // Verify
    verify(divisionRepository).update(argThat(hasSameStateAsDivision(updatedDivision)));
    assertThat(returnedDivision, hasSameStateAsDivision(updatedDivision));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDivisionWhenCannotEditLevel_shouldThrow() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();

    Division existingDivision = aDivision()
        .withId(divisionId)
        .withName("Existing")
        .withLevel(1)
        .withParent(null)
        .build();
    
    Division updatedDivision = aDivision()
        .withId(divisionId)
        .withName("Updated")
        .withLevel(2)
        .withParent(aDivision().withId(UUID.randomUUID()).build())
        .build();
    
    // Set up expectations
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(existingDivision);
    when(divisionConstraintChecker.canEditLevel(existingDivision)).thenReturn(false);
    
    // Exercise SUT
    divisionCRUDService.updateDivision(updatedDivision);
  }
  
  @Test
  public void testUpdateDivisionWithoutChangingLevel_shouldSetCorrectFieldsAndUpdate() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();

    Division existingDivision = aDivision()
        .withId(divisionId)
        .withName("Existing")
        .withLevel(1)
        .withParent(null)
        .build();
    
    Division updatedDivision = aDivision()
        .withId(divisionId)
        .withName("Updated")
        .withLevel(1)
        .withParent(aDivision().withId(UUID.randomUUID()).build())
        .build();
    
    // Set up expectations
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(existingDivision);
    when(divisionConstraintChecker.canEditLevel(existingDivision)).thenReturn(false);
    when(divisionRepository.update(any(Division.class))).thenAnswer(returnsFirstArg());
    
    // Exercise SUT
    Division returnedDivision = divisionCRUDService.updateDivision(updatedDivision);
    
    // Verify
    verify(divisionRepository).update(argThat(hasSameStateAsDivision(updatedDivision)));
    assertThat(returnedDivision, hasSameStateAsDivision(updatedDivision));
  }
}
