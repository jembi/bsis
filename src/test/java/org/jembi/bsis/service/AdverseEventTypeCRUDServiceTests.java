package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBackingFormBuilder.anAdverseEventTypeBackingForm;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.matchers.AdverseEventTypeMatcher.hasSameStateAsAdverseEventType;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.backingform.AdverseEventTypeBackingForm;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdverseEventTypeCRUDServiceTests {

  @InjectMocks
  private AdverseEventTypeCRUDService adverseEventTypeCRUDService;
  @Mock
  private AdverseEventTypeRepository adverseEventTypeRepository;

  @Test
  public void testCreateAdverseEventType_shouldPersistAndReturnAdverseEventTypeWithTheCorrectFields() {
    String irrelevantName = "some name";
    String irrelevantDescription = "irrelevant description";

    AdverseEventTypeBackingForm backingForm = anAdverseEventTypeBackingForm()
        .thatIsDeleted()
        .withName(irrelevantName)
        .withDescription(irrelevantDescription)
        .build();
    AdverseEventType expectedAdverseEventType = anAdverseEventType()
        .thatIsDeleted()
        .withName(irrelevantName)
        .withDescription(irrelevantDescription)
        .build();

    AdverseEventType returnedAdverseEventType = adverseEventTypeCRUDService.createAdverseEventType(backingForm);

    verify(adverseEventTypeRepository).save(argThat(hasSameStateAsAdverseEventType(expectedAdverseEventType)));
    assertThat(returnedAdverseEventType, hasSameStateAsAdverseEventType(expectedAdverseEventType));
  }

  @Test
  public void testUpdateAdverseEventType_shouldUpdateAndReturnAdverseEventTypeWithTheCorrectFields() {
    UUID irrelevantId = UUID.randomUUID();
    String irrelevantName = "some name";
    String irrelevantDescription = "irrelevant description";

    AdverseEventTypeBackingForm backingForm = anAdverseEventTypeBackingForm()
        .thatIsDeleted()
        .withId(irrelevantId)
        .withName(irrelevantName)
        .withDescription(irrelevantDescription)
        .build();
    AdverseEventType existingAdverseEventType = anAdverseEventType()
        .thatIsNotDeleted()
        .withId(irrelevantId)
        .withName("old name")
        .withDescription("old description")
        .build();
    AdverseEventType expectedAdverseEventType = anAdverseEventType()
        .thatIsDeleted()
        .withId(irrelevantId)
        .withName(irrelevantName)
        .withDescription(irrelevantDescription)
        .build();

    when(adverseEventTypeRepository.findById(irrelevantId)).thenReturn(existingAdverseEventType);
    when(adverseEventTypeRepository.update(argThat(hasSameStateAsAdverseEventType(expectedAdverseEventType))))
        .thenReturn(expectedAdverseEventType);

    AdverseEventType returnedAdverseEventType = adverseEventTypeCRUDService.updateAdverseEventType(irrelevantId,
        backingForm);

    verify(adverseEventTypeRepository).update(argThat(hasSameStateAsAdverseEventType(expectedAdverseEventType)));
    assertThat(returnedAdverseEventType, hasSameStateAsAdverseEventType(expectedAdverseEventType));
  }

}
