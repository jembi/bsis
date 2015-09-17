package factory;

import static helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static helpers.builders.AdverseEventTypeViewModelBuilder.anAdverseEventTypeViewModel;
import static helpers.builders.AdverseEventViewModelBuilder.anAdverseEventViewModel;
import static helpers.matchers.AdverseEventViewModelMatcher.hasSameStateAsAdverseEventViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import model.adverseevent.AdverseEvent;
import model.adverseevent.AdverseEventType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import viewmodel.AdverseEventTypeViewModel;
import viewmodel.AdverseEventViewModel;

@RunWith(MockitoJUnitRunner.class)
public class AdverseEventViewModelFactoryTests {
    
    @InjectMocks
    private AdverseEventViewModelFactory adverseEventViewModelFactory;
    @Mock
    private AdverseEventTypeViewModelFactory adverseEventTypeViewModelFactory;
    
    @Test
    public void testCreateAdverseEventTypeViewModel_shouldReturnViewModelWithTheCorrectState() {
        
        Long irrelevantAdverseEventId = 77L;
        Long irrelevantAdverseEventTypeId = 89L;
        String irrelevantComment = "test";
        
        AdverseEventType adverseEventType = anAdverseEventType().withId(irrelevantAdverseEventTypeId).build();
        AdverseEvent adverseEvent =  anAdverseEvent()
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
        
        AdverseEventViewModel returnedAdverseEventViewModel = adverseEventViewModelFactory.createAdverseEventViewModel(
                adverseEvent);
        
        assertThat(returnedAdverseEventViewModel, hasSameStateAsAdverseEventViewModel(expectedAdverseEventViewModel));
    }

}
