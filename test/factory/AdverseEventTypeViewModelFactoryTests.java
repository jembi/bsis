package factory;

import model.adverseevent.AdverseEventType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import viewmodel.AdverseEventTypeViewModel;

import static helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static helpers.builders.AdverseEventTypeViewModelBuilder.anAdverseEventTypeViewModel;
import static helpers.matchers.AdverseEventTypeViewModelMatcher.hasSameStateAsAdverseEventTypeViewModel;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AdverseEventTypeViewModelFactoryTests {

  @InjectMocks
  private AdverseEventTypeViewModelFactory adverseEventTypeViewModelFactory;

  @Test
  public void testCreateAdverseEventViewModel_shouldReturnViewModelWithTheCorrectState() {
    Long irrelevantId = 1L;
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
