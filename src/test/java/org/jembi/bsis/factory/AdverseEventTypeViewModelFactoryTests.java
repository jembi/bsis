package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeViewModelBuilder.anAdverseEventTypeViewModel;
import static org.jembi.bsis.helpers.matchers.AdverseEventTypeViewModelMatcher.hasSameStateAsAdverseEventTypeViewModel;

import java.util.UUID;

import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdverseEventTypeViewModelFactoryTests {

  @InjectMocks
  private AdverseEventTypeViewModelFactory adverseEventTypeViewModelFactory;

  @Test
  public void testCreateAdverseEventViewModel_shouldReturnViewModelWithTheCorrectState() {
    UUID irrelevantId = UUID.randomUUID();
    String irrelevantName = "name";
    String irrelevantDescription = "description";

    AdverseEventType adverseEventType = anAdverseEventType()
        .thatIsDeleted()
        .withId(irrelevantId)
        .withName(irrelevantName)
        .withDescription(irrelevantDescription)
        .build();

    AdverseEventTypeViewModel expectedViewModel = anAdverseEventTypeViewModel()
        .thatIsDeleted()
        .withId(irrelevantId)
        .withName(irrelevantName)
        .withDescription(irrelevantDescription)
        .build();

    AdverseEventTypeViewModel returnedViewModel = adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(
        adverseEventType);

    assertThat(returnedViewModel, hasSameStateAsAdverseEventTypeViewModel(expectedViewModel));
  }

}
