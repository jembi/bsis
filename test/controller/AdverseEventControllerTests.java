package controller;

import static helpers.builders.AdverseEventTypeBackingFormBuilder.anAdverseEventTypeBackingForm;
import static helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static helpers.builders.AdverseEventTypeViewModelBuilder.anAdverseEventTypeViewModel;
import static helpers.matchers.AdverseEventTypeBackingFormMatcher.hasSameStateAsAdverseEventTypeBackingForm;
import static helpers.matchers.AdverseEventTypeMatcher.hasSameStateAsAdverseEventType;
import static helpers.matchers.AdverseEventTypeViewModelMatcher.hasSameStateAsAdverseEventTypeViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import model.adverseevent.AdverseEventType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.AdverseEventTypeRepository;
import service.AdverseEventTypeCRUDService;
import viewmodel.AdverseEventTypeViewModel;
import backingform.AdverseEventTypeBackingForm;
import factory.AdverseEventTypeViewModelFactory;

@RunWith(MockitoJUnitRunner.class)
public class AdverseEventControllerTests {
    
    @InjectMocks
    private AdverseEventController adverseEventController;
    @Mock
    private AdverseEventTypeRepository adverseEventTypeRepository;
    @Mock
    private AdverseEventTypeCRUDService adverseEventTypeCRUDService;
    @Mock
    private AdverseEventTypeViewModelFactory adverseEventTypeViewModelFactory;
    
    @Test
    public void testFindAdverseEventTypes_shouldDelegateToAdverseEventTypeRepository() {
        
        List<AdverseEventTypeViewModel> expectedViewModels = Arrays.asList(
                anAdverseEventTypeViewModel()
                        .withId(7L)
                        .withName("irrelevant.name")
                        .withDescription("irrelevant.description")
                        .build(),
                anAdverseEventTypeViewModel()
                        .withId(86L)
                        .withName("another.irrelevant.name")
                        .withDescription("another.irrelevant.description")
                        .build()
        );
        
        when(adverseEventTypeRepository.findAdverseEventTypeViewModels()).thenReturn(expectedViewModels);
        
        List<AdverseEventTypeViewModel> returnedViewModels = adverseEventController.findAdverseEventTypes();
        
        assertThat(returnedViewModels, is(expectedViewModels));
    }
    
    @Test
    public void testCreateAdverseEventType_shouldCreateAndReturnAdverseEventType() {
        Long irrelevantId = 2L;
        String irrelevantName = "test name";
        String irrelevantDescription = "test description";
        
        AdverseEventTypeBackingForm backingForm = anAdverseEventTypeBackingForm()
                .withName(irrelevantName)
                .withDescription(irrelevantDescription)
                .build();
        
        AdverseEventType adverseEventType = anAdverseEventType()
                .withId(irrelevantId)
                .withName(irrelevantName)
                .withDescription(irrelevantDescription)
                .build();
        
        AdverseEventTypeViewModel expectedViewModel = anAdverseEventTypeViewModel()
                .withId(irrelevantId)
                .withName(irrelevantName)
                .withDescription(irrelevantDescription)
                .build();
        
        when(adverseEventTypeCRUDService
                .createAdverseEventType(argThat(hasSameStateAsAdverseEventTypeBackingForm(backingForm))))
                .thenReturn(adverseEventType);
        when(adverseEventTypeViewModelFactory
                .createAdverseEventTypeViewModel(argThat(hasSameStateAsAdverseEventType(adverseEventType))))
                .thenReturn(expectedViewModel);
        
        AdverseEventTypeViewModel returnedViewModel = adverseEventController.createAdverseEventType(backingForm);
        
        assertThat(returnedViewModel, hasSameStateAsAdverseEventTypeViewModel(expectedViewModel));
    }

}
