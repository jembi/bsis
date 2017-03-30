package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.AdverseEventBackingFormBuilder.anAdverseEventBackingForm;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBackingFormBuilder.anAdverseEventTypeBackingForm;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeViewModelBuilder.anAdverseEventTypeViewModel;
import static org.jembi.bsis.helpers.builders.AdverseEventViewModelBuilder.anAdverseEventViewModel;
import static org.jembi.bsis.helpers.matchers.AdverseEventMatcher.hasSameStateAsAdverseEvent;
import static org.jembi.bsis.helpers.matchers.AdverseEventViewModelMatcher.hasSameStateAsAdverseEventViewModel;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.backingform.AdverseEventBackingForm;
import org.jembi.bsis.backingform.AdverseEventTypeBackingForm;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdverseEventFactoryTests {

  @InjectMocks
  private AdverseEventFactory adverseEventFactory;
  @Mock
  private AdverseEventTypeViewModelFactory adverseEventTypeViewModelFactory;
  @Mock
  private AdverseEventTypeRepository adverseEventTypeRepository;

  @Test
  public void testCreateEntity_shouldReturnCorrectEntity() {
    // Set up data
    UUID irrelevantAdveseEventTypeId = UUID.randomUUID();
    UUID irrelevantAdverseEventId = UUID.randomUUID();
    AdverseEventTypeBackingForm typeForm = anAdverseEventTypeBackingForm()
        .withId(irrelevantAdveseEventTypeId).build();
    AdverseEventBackingForm form = anAdverseEventBackingForm()
        .withId(irrelevantAdverseEventId).withComment("comment").withType(typeForm).build();
    AdverseEventType type = anAdverseEventType().withId(irrelevantAdveseEventTypeId).build();
    AdverseEvent expectedEntity = anAdverseEvent()
        .withId(irrelevantAdverseEventId).withComment("comment").withType(type).build();
    
    // Set up mocks
    when(adverseEventTypeRepository.findById(irrelevantAdveseEventTypeId)).thenReturn(type);
    
    // Run test
    AdverseEvent createdEntity = adverseEventFactory.createEntity(form);
    
    // Verify
    assertThat(createdEntity, hasSameStateAsAdverseEvent(expectedEntity));
  }


  @Test
  public void testCreateAdverseEventTypeViewModel_shouldReturnViewModelWithTheCorrectState() {

    UUID irrelevantAdverseEventId = UUID.randomUUID();
    UUID irrelevantAdverseEventTypeId = UUID.randomUUID();
    String irrelevantComment = "test";

    AdverseEventType adverseEventType = anAdverseEventType().withId(irrelevantAdverseEventTypeId).build();
    AdverseEvent adverseEvent = anAdverseEvent()
        .withId(irrelevantAdverseEventId)
        .withType(adverseEventType)
        .withComment(irrelevantComment)
        .build();

    AdverseEventTypeViewModel adverseEventTypeViewModel = anAdverseEventTypeViewModel().build();
    AdverseEventViewModel expectedAdverseEventViewModel = anAdverseEventViewModel()
        .withId(irrelevantAdverseEventId)
        .withType(adverseEventTypeViewModel)
        .withComment(irrelevantComment)
        .build();

    when(adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEventType))
        .thenReturn(adverseEventTypeViewModel);

    AdverseEventViewModel returnedAdverseEventViewModel = adverseEventFactory.createAdverseEventViewModel(
        adverseEvent);

    assertThat(returnedAdverseEventViewModel, hasSameStateAsAdverseEventViewModel(expectedAdverseEventViewModel));
  }

}
