package service;

import static helpers.builders.AdverseEventTypeBackingFormBuilder.anAdverseEventTypeBackingForm;
import static helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static helpers.matchers.AdverseEventTypeMatcher.hasSameStateAsAdverseEventType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import model.adverseevent.AdverseEventType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.AdverseEventTypeRepository;
import backingform.AdverseEventTypeBackingForm;

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
                .withName(irrelevantName)
                .withDescription(irrelevantDescription)
                .build();
        AdverseEventType expectedAdverseEventType = anAdverseEventType()
                .withName(irrelevantName)
                .withDescription(irrelevantDescription)
                .build();
        
        AdverseEventType returnedAdverseEventType = adverseEventTypeCRUDService.createAdverseEventType(backingForm);
        
        verify(adverseEventTypeRepository).save(argThat(hasSameStateAsAdverseEventType(expectedAdverseEventType)));
        assertThat(returnedAdverseEventType, hasSameStateAsAdverseEventType(expectedAdverseEventType));
    }

}
