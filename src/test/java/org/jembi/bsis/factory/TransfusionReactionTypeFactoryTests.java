package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBackingFormBuilder.aTransfusionReactionTypeBackingForm;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeManagementViewModelBuilder.aTransfusionReactionTypeManagementViewModel;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeViewModelBuilder.aTransfusionReactionTypeViewModel;
import static org.jembi.bsis.helpers.matchers.TransfusionReactionTypeManagementViewModelMatcher.hasSameStateAsTransfusionReactionTypeManagementViewModel;
import static org.jembi.bsis.helpers.matchers.TransfusionReactionTypeMatcher.hasSameStateAsTransfusionReactionType;
import static org.jembi.bsis.helpers.matchers.TransfusionReactionTypeViewModelMatcher.hasSameStateAsTransfusionReactionTypeViewModel;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeManagementViewModel;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

public class TransfusionReactionTypeFactoryTests extends UnitTestSuite {
  
  @Spy
  @InjectMocks
  private TransfusionReactionTypeFactory transfusionReactionTypeFactory;

  @Test
  public void testCreateTransfusionReactionTypeViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID transfusionReactionTypeId = UUID.randomUUID();
    String transfusionReactionTypeName = "Some transfusionReactionType";
    TransfusionReactionType transfusionReactionType = aTransfusionReactionType()
        .withId(transfusionReactionTypeId)
        .withName(transfusionReactionTypeName)
        .build();

    // Set up expectations
    TransfusionReactionTypeViewModel expectedViewModel = aTransfusionReactionTypeViewModel()
        .withId(transfusionReactionTypeId)
        .withName(transfusionReactionTypeName)
        .build();
    
    // Exercise SUT
    TransfusionReactionTypeViewModel returnedViewModel =
        transfusionReactionTypeFactory.createTransfusionReactionTypeViewModel(transfusionReactionType);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsTransfusionReactionTypeViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTransfusionReactionTypeViewModelWithNullTransfusionReactionType_shouldReturnEmptyViewModel() {
    // Set up expectations
    TransfusionReactionTypeViewModel expectedViewModel = aTransfusionReactionTypeViewModel()
        .build();

    // Exercise SUT
    TransfusionReactionTypeViewModel returnedViewModel =
        transfusionReactionTypeFactory.createTransfusionReactionTypeViewModel(null);

    // Verify
    assertThat(returnedViewModel, hasSameStateAsTransfusionReactionTypeViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTransfusionReactionTypeManagementViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID transfusionReactionTypeId = UUID.randomUUID();
    String transfusionReactionTypeName = "Some transfusionReactionType";
    String description = "Default description";

    TransfusionReactionType transfusionReactionType = aTransfusionReactionType()
        .withId(transfusionReactionTypeId)
        .withName(transfusionReactionTypeName)
        .withDescription(description)
        .build();

    // Set up expectations
    TransfusionReactionTypeManagementViewModel expectedViewModel = aTransfusionReactionTypeManagementViewModel()
        .withId(transfusionReactionTypeId)
        .withName(transfusionReactionTypeName)
        .withDescription(description)
        .build();
    
    // Exercise SUT
    TransfusionReactionTypeManagementViewModel returnedViewModel =
        transfusionReactionTypeFactory.createTransfusionReactionTypeManagementViewModel(transfusionReactionType);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsTransfusionReactionTypeManagementViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTransfusionReactionTypeManagementViewModelWithNullTransfusionReactionType_shouldReturnEmptyViewModel() {
    // Set up expectations
    TransfusionReactionTypeManagementViewModel expectedViewModel = aTransfusionReactionTypeManagementViewModel()
        .build();

    // Exercise SUT
    TransfusionReactionTypeManagementViewModel returnedViewModel =
        transfusionReactionTypeFactory.createTransfusionReactionTypeManagementViewModel(null);

    // Verify
    assertThat(returnedViewModel, hasSameStateAsTransfusionReactionTypeManagementViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTransfusionReactionTypeViewModels_shouldReturnExpectedViewModels() {
    // Set up fixture
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    TransfusionReactionType firstTransfusionReactionType = aTransfusionReactionType()
        .withId(id1)
        .withName("First")
        .build();
    TransfusionReactionType secondTransfusionReactionType = aTransfusionReactionType()
        .withId(id2)
        .withName("Second")
        .build();
    List<TransfusionReactionType> transfusionReactionTypes =
        Arrays.asList(firstTransfusionReactionType, secondTransfusionReactionType);

    // Set up expectations
    TransfusionReactionTypeViewModel firstViewModel = aTransfusionReactionTypeViewModel().withId(id1).withName("First").withIsDeleted(false).build();
    TransfusionReactionTypeViewModel secondViewModel = aTransfusionReactionTypeViewModel().withId(id2).withName("Second").withIsDeleted(false).build();
    List<TransfusionReactionTypeViewModel> expectedViewModels = Arrays.asList(firstViewModel, secondViewModel);

    doReturn(firstViewModel).when(transfusionReactionTypeFactory)
        .createTransfusionReactionTypeViewModel(firstTransfusionReactionType);
    doReturn(secondViewModel).when(transfusionReactionTypeFactory)
        .createTransfusionReactionTypeViewModel(secondTransfusionReactionType);

    // Exercise SUT
    List<TransfusionReactionTypeViewModel> returnedViewModels =
        transfusionReactionTypeFactory.createTransfusionReactionTypeViewModels(transfusionReactionTypes);

    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }
  
  @Test
  public void testCreateTransfusionReactionManagementTypeViewModels_shouldReturnExpectedViewModels() {
    // Set up fixture
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    String description1 = "Description 1";
    String firstName = "First";
    TransfusionReactionType firstTransfusionReactionType = aTransfusionReactionType()
        .withId(id1)
        .withName(firstName)
        .withDescription(description1)
        .build();
    String description2 = "Description 2";
    String secondName = "Second";
    TransfusionReactionType secondTransfusionReactionType = aTransfusionReactionType()
        .withId(id2)
        .withName(secondName)
        .withDescription(description2)
        .build();
    List<TransfusionReactionType> transfusionReactionTypes =
        Arrays.asList(firstTransfusionReactionType, secondTransfusionReactionType);

    // Set up expectations
    TransfusionReactionTypeManagementViewModel firstViewModel = aTransfusionReactionTypeManagementViewModel()
        .withId(id1)
        .withName(firstName)
        .withDescription(description1)
        .withIsDeleted(false)
        .build();
    TransfusionReactionTypeManagementViewModel secondViewModel = aTransfusionReactionTypeManagementViewModel()
        .withId(id2)
        .withName(secondName)
        .withDescription(description2)
        .withIsDeleted(false).build();
    List<TransfusionReactionTypeManagementViewModel> expectedViewModels =
        Arrays.asList(firstViewModel, secondViewModel);

    doReturn(firstViewModel).when(transfusionReactionTypeFactory)
        .createTransfusionReactionTypeViewModel(firstTransfusionReactionType);
    doReturn(secondViewModel).when(transfusionReactionTypeFactory)
        .createTransfusionReactionTypeViewModel(secondTransfusionReactionType);

    // Exercise SUT
    List<TransfusionReactionTypeManagementViewModel> returnedViewModels =
        transfusionReactionTypeFactory.createTransfusionReactionTypeManagementViewModels(transfusionReactionTypes);

    // Verify
    assertThat(returnedViewModels.get(0), hasSameStateAsTransfusionReactionTypeManagementViewModel(expectedViewModels.get(0)));
    assertThat(returnedViewModels.get(1), hasSameStateAsTransfusionReactionTypeManagementViewModel(expectedViewModels.get(1)));
  }

  @Test
  public void testCreateEntity_shouldReturnExpectedEntity() {
    // Set up fixture
    UUID id = UUID.randomUUID();
    String name = "reaction name";
    String description = "reaction description";
    TransfusionReactionTypeBackingForm transfusionReactionTypeForm = aTransfusionReactionTypeBackingForm()
        .withId(id)
        .withName(name)
        .withDescription(description)
        .thatIsDeleted()
        .build();
    // Set up expectations
    TransfusionReactionType expectedEntity = aTransfusionReactionType()
        .withId(id)
        .withName(name)
        .withDescription(description)
        .thatIsDeleted()
        .build();

    // Exercise SUT
    TransfusionReactionType returnedEntity = transfusionReactionTypeFactory.createEntity(transfusionReactionTypeForm);

    // Verify
    assertThat(returnedEntity, hasSameStateAsTransfusionReactionType(expectedEntity));
  }
}

