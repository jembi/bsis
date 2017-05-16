package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.jembi.bsis.helpers.matchers.TransfusionReactionTypeMatcher.hasSameStateAsTransfusionReactionType;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TransfusionReactionTypeCRUDServiceTests extends UnitTestSuite {

  @InjectMocks
  private TransfusionReactionTypeCRUDService transfusionReactionTypeCRUDService;

  @Mock
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;
  
  @Test
  public void testCreateTransfusionReactionType_shouldSave() {
    // Set up data
    TransfusionReactionType transfusionReactionType = aTransfusionReactionType()
        .withName("Test Name")
        .withDescription("Test Description")
        .build();

    // Run test
    TransfusionReactionType createdTransfusionReactionType = transfusionReactionTypeCRUDService.createTransfusionReactionType(transfusionReactionType);
    
    // Verify
    verify(transfusionReactionTypeRepository).save(argThat(hasSameStateAsTransfusionReactionType(transfusionReactionType)));
    assertThat(createdTransfusionReactionType, is(transfusionReactionType));
  }
  
  @Test
  public void testUpdateTransfusionReactionType_shouldUpdateCorrectly() {
    UUID id = UUID.randomUUID();
    TransfusionReactionType existingTransfusionReactionType = aTransfusionReactionType()
      .withId(id)
      .withName("Test Name")
      .withDescription("Test Description")
      .build();

    TransfusionReactionType updatedTransfusionReactionType = aTransfusionReactionType()
        .withId(id)
        .withName("UpdatedName")
        .withDescription("Description")
        .build();

    when(transfusionReactionTypeRepository.findById(id)).thenReturn(existingTransfusionReactionType);
    when(transfusionReactionTypeRepository.update(existingTransfusionReactionType))
        .thenReturn(updatedTransfusionReactionType);

    TransfusionReactionType returnedTransfusionReactionType =
        transfusionReactionTypeCRUDService.updateTransfusionReactionType(updatedTransfusionReactionType);

    verify(transfusionReactionTypeRepository)
        .update(argThat(hasSameStateAsTransfusionReactionType(updatedTransfusionReactionType)));
    assertThat(returnedTransfusionReactionType, is(updatedTransfusionReactionType));
  }
}
