package org.jembi.bsis.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBackingFormBuilder.anAdverseEventTypeBackingForm;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeViewModelBuilder.anAdverseEventTypeViewModel;
import static org.jembi.bsis.helpers.matchers.AdverseEventTypeBackingFormMatcher.hasSameStateAsAdverseEventTypeBackingForm;
import static org.jembi.bsis.helpers.matchers.AdverseEventTypeMatcher.hasSameStateAsAdverseEventType;
import static org.jembi.bsis.helpers.matchers.AdverseEventTypeViewModelMatcher.hasSameStateAsAdverseEventTypeViewModel;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.AdverseEventTypeBackingForm;
import org.jembi.bsis.factory.AdverseEventTypeViewModelFactory;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.jembi.bsis.service.AdverseEventTypeCRUDService;
import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
            .withId(UUID.randomUUID())
            .withName("irrelevant.name")
            .withDescription("irrelevant.description")
            .build(),
        anAdverseEventTypeViewModel()
            .withId(UUID.randomUUID())
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
    UUID irrelevantId = UUID.randomUUID();
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

  @Test
  public void testUpdateAdverseEventType_shouldUpdateAndReturnAdverseEventType() {
    UUID irrelevantId = UUID.randomUUID();
    String irrelevantName = "test name";
    String irrelevantDescription = "test description";

    AdverseEventTypeBackingForm backingForm = anAdverseEventTypeBackingForm()
        .withId(irrelevantId)
        .withName(irrelevantName)
        .withDescription(irrelevantDescription)
        .build();
    AdverseEventType updatedAdverseEventType = anAdverseEventType()
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
        .updateAdverseEventType(eq(irrelevantId), argThat(hasSameStateAsAdverseEventTypeBackingForm(backingForm))))
        .thenReturn(updatedAdverseEventType);

    when(adverseEventTypeViewModelFactory
        .createAdverseEventTypeViewModel(argThat(hasSameStateAsAdverseEventType(updatedAdverseEventType))))
        .thenReturn(expectedViewModel);

    AdverseEventTypeViewModel returnedViewModel = adverseEventController.updateAdverseEventType(irrelevantId,
        backingForm);

    assertThat(returnedViewModel, hasSameStateAsAdverseEventTypeViewModel(expectedViewModel));
  }

  @Test
  public void testFindAdverseEventTypeById_shouldFindAndReturnAdverseEventType() {
    UUID irrelevantId = UUID.randomUUID();
    String irrelevantName = "test name";
    String irrelevantDescription = "test description";

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

    when(adverseEventTypeRepository.findById(irrelevantId)).thenReturn(adverseEventType);
    when(adverseEventTypeViewModelFactory
        .createAdverseEventTypeViewModel(argThat(hasSameStateAsAdverseEventType(adverseEventType))))
        .thenReturn(expectedViewModel);

    AdverseEventTypeViewModel returnedViewModel = adverseEventController.findAdverseEventTypeById(irrelevantId);

    assertThat(returnedViewModel, hasSameStateAsAdverseEventTypeViewModel(expectedViewModel));
  }

}
