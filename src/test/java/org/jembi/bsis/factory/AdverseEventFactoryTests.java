package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeViewModelBuilder.anAdverseEventTypeViewModel;
import static org.jembi.bsis.helpers.builders.AdverseEventViewModelBuilder.anAdverseEventViewModel;
import static org.jembi.bsis.helpers.matchers.AdverseEventViewModelMatcher.hasSameStateAsAdverseEventViewModel;
import static org.mockito.Mockito.when;

import org.jembi.bsis.factory.AdverseEventTypeViewModelFactory;
import org.jembi.bsis.factory.AdverseEventFactory;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
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

  @Test
  public void testCreateAdverseEventTypeViewModel_shouldReturnViewModelWithTheCorrectState() {

    Long irrelevantAdverseEventId = 77L;
    Long irrelevantAdverseEventTypeId = 89L;
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
