package org.jembi.bsis.service;

import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.jembi.bsis.helpers.matchers.TransfusionReactionTypeMatcher.hasSameStateAsTransfusionReactionType;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

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
    transfusionReactionType = transfusionReactionTypeCRUDService.createTransfusionReactionType(transfusionReactionType);
    
    // Verify
    verify(transfusionReactionTypeRepository).save(argThat(hasSameStateAsTransfusionReactionType(transfusionReactionType)));
  }
}
