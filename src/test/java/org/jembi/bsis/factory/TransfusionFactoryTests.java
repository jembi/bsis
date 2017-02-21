package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.TransfusionBackingFormBuilder.aTransfusionBackingForm;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;
import static org.jembi.bsis.helpers.matchers.TransfusionViewModelMatcher.hasSameStateAsTransfusionViewModel;
import static org.jembi.bsis.helpers.builders.PatientBackingFormBuilder.aPatientBackingForm;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.TransfusionBackingFormBuilder.aTransfusionBackingForm;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBackingFormBuilder.aTransfusionReactionTypeBackingForm;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;
import static org.jembi.bsis.helpers.builders.TransfusionViewModelBuilder.aTransfusionViewModel;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.PatientViewModelBuilder.aPatientViewModel;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeViewModelBuilder.aTransfusionReactionTypeViewModel;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.PatientBackingForm;
import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PatientViewModel;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;
import org.jembi.bsis.viewmodel.TransfusionViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TransfusionFactoryTests extends UnitTestSuite {

  @InjectMocks
  private TransfusionFactory transfusionFactory;
  @Mock
  private PatientFactory patientFactory;
  @Mock
  private ComponentTypeRepository componentTypeRepository;
  @Mock
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private ComponentTypeFactory componentTypeFactory;
  @Mock
  private TransfusionReactionTypeFactory transfusionReactionTypeFactory;
  @Mock
  private LocationFactory locationFactory;

  @Test
  public void testCreateEntityWithoutReaction_shouldReturnEntityInCorrectState() {
    String componentTypeCode = "123";
    Date transfusionDate = new Date();

    PatientBackingForm patientForm = aPatientBackingForm().withId(1L).build();
    ComponentTypeBackingForm componentTypeForm = aComponentTypeBackingForm().withId(1L).withComponentTypeCode(componentTypeCode).build();
    LocationBackingForm receivedFromForm = aLocationBackingForm().withId(1L).build();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentType(componentTypeForm)
        .withReceivedFrom(receivedFromForm)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patientForm)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    Patient patient = aPatient().withId(1L).build();
    ComponentType componentType = aComponentType().withId(1L).build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion expectedEntity = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentType(componentType)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(receivedFrom);
    when(componentTypeRepository.findComponentTypeByCode(componentTypeCode)).thenReturn(componentType);
    when(patientFactory.createEntity(patientForm)).thenReturn(patient);

    Transfusion returnedEntity = transfusionFactory.createEntity(form);

    assertThat(returnedEntity, hasSameStateAsTransfusion(expectedEntity));
  }

  @Test
  public void testCreateEntityUsingComponentCode_shouldReturnEntityInCorrectState() {
    String componentTypeCode = "123";
    Date transfusionDate = new Date();

    PatientBackingForm patientForm = aPatientBackingForm().withId(1L).build();
    ///ComponentTypeBackingForm componentTypeForm = aComponentTypeBackingForm().withId(1L).withComponentTypeCode(componentTypeCode).build();
    LocationBackingForm receivedFromForm = aLocationBackingForm().withId(1L).build();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentCode(componentTypeCode)
        .withReceivedFrom(receivedFromForm)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patientForm)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    Patient patient = aPatient().withId(1L).build();
    ComponentType componentType = aComponentType().withId(1L).build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion expectedEntity = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentType(componentType)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(receivedFrom);
    when(componentTypeRepository.findComponentTypeByCode(argThat(is(componentTypeCode)))).thenReturn(componentType);
    when(patientFactory.createEntity(patientForm)).thenReturn(patient);

    Transfusion returnedEntity = transfusionFactory.createEntity(form);

    assertThat(returnedEntity, hasSameStateAsTransfusion(expectedEntity));
    verify(componentTypeRepository).findComponentTypeByCode(argThat(is(componentTypeCode)));
  }

  @Test
  public void testCreateEntityWithReaction_shouldReturnEntityInCorrectState() {
    String componentTypeCode = "123";
    Date transfusionDate = new Date();

    PatientBackingForm patientForm = aPatientBackingForm().withId(1L).build();
    TransfusionReactionTypeBackingForm transfusionReactionTypeForm = aTransfusionReactionTypeBackingForm().withId(1L).build();
    ComponentTypeBackingForm componentTypeForm = aComponentTypeBackingForm().withId(1L).withComponentTypeCode(componentTypeCode).build();
    LocationBackingForm receivedFromForm = aLocationBackingForm().withId(1L).build();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentType(componentTypeForm)
        .withReceivedFrom(receivedFromForm)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patientForm)
        .withTransfusionReactionType(transfusionReactionTypeForm)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    TransfusionReactionType transfusionReactionType = aTransfusionReactionType().withId(1L).build();
    Patient patient = aPatient().withId(1L).build();
    ComponentType componentType = aComponentType().withId(1L).build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion expectedEntity = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentType(componentType)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(receivedFrom);
    when(componentTypeRepository.findComponentTypeByCode(componentTypeCode)).thenReturn(componentType);
    when(patientFactory.createEntity(patientForm)).thenReturn(patient);
    when(transfusionReactionTypeRepository.findById(1L)).thenReturn(transfusionReactionType);

    Transfusion returnedEntity = transfusionFactory.createEntity(form);

    assertThat(returnedEntity, hasSameStateAsTransfusion(expectedEntity));
  }

  @Test
  public void testCreateViewModel_shouldReturnViewModelWithCorrectState() {
    String componentTypeCode = "123";
    Date transfusionDate = new Date();

    TransfusionReactionType transfusionReactionType = aTransfusionReactionType().withId(1L).build();
    Patient patient = aPatient().withId(1L).build();
    ComponentType componentType = aComponentType().withId(1L).build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion transfusion = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentType(componentType)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    // setup expectations
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel().withId(1L).build();
    LocationViewModel receivedFromViewModel = aLocationViewModel().withId(1L).build();
    PatientViewModel patientViewModel = aPatientViewModel().withId(1L).build();
    TransfusionReactionTypeViewModel transfusionReactionTypeViewModel = aTransfusionReactionTypeViewModel()
        .withId(1L)
        .build();

    TransfusionViewModel expectedViewModel = aTransfusionViewModel()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentType(componentTypeViewModel)
        .withUsageSite(receivedFromViewModel)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patientViewModel)
        .withTransfusionReactionType(transfusionReactionTypeViewModel)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    // Setup mock
    when(componentTypeFactory.createViewModel(componentType)).thenReturn(componentTypeViewModel);
    when(transfusionReactionTypeFactory.createTransfusionReactionTypeViewModel(transfusionReactionType))
        .thenReturn(transfusionReactionTypeViewModel);
    when(patientFactory.createViewModel(patient)).thenReturn(patientViewModel);
    when(locationFactory.createViewModel(receivedFrom)).thenReturn(receivedFromViewModel);

    // Run test
    TransfusionViewModel returnedViewModel = transfusionFactory.createViewModel(transfusion);

    //Verify
    assertThat(returnedViewModel, hasSameStateAsTransfusionViewModel(expectedViewModel));
  }

  @Test
  public void testCreateViewModels_returnsCollection() {
    String componentTypeCode = "123";
    Date transfusionDate = new Date();

    TransfusionReactionType transfusionReactionType = aTransfusionReactionType().withId(1L).build();
    Patient patient = aPatient().withId(1L).build();
    ComponentType componentType = aComponentType().withId(1L).build();
    Location receivedFrom = aLocation().withId(1L).build();
    List<Transfusion> transfusions = new ArrayList<>();
    transfusions.add(aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber("123456")
        .withComponentType(componentType)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build());
    transfusions.add(aTransfusion()
        .withId(2L)
        .withDonationIdentificationNumber("1234567")
        .withComponentType(componentType)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.UNKNOWN)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build());

    //Run test
    List<TransfusionViewModel> returnedViewModels = transfusionFactory.createViewModels(transfusions);

    Assert.assertNotNull("View models have been created ", returnedViewModels);
    Assert.assertEquals("Correct number of view models is returned", 2 , returnedViewModels.size());
  }

  @Test
  public void testCreateViewModelsWithNull_returnsEmptyCollection() {
    //Run test
    List<TransfusionViewModel> viewModels = transfusionFactory.createViewModels(null);

    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertTrue("No view models", viewModels.isEmpty());
  }
}
