package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentViewModelBuilder.aComponentViewModel;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aUsageSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.PatientBackingFormBuilder.aPatientBackingForm;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.PatientViewModelBuilder.aPatientViewModel;
import static org.jembi.bsis.helpers.builders.TransfusionBackingFormBuilder.aTransfusionBackingForm;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.builders.TransfusionFullViewModelBuilder.aTransfusionFullViewModel;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBackingFormBuilder.aTransfusionReactionTypeBackingForm;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeViewModelBuilder.aTransfusionReactionTypeViewModel;
import static org.jembi.bsis.helpers.builders.TransfusionViewModelBuilder.aTransfusionViewModel;
import static org.jembi.bsis.helpers.matchers.TransfusionFullViewModelMatcher.hasSameStateAsTransfusionFullViewModel;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;
import static org.jembi.bsis.helpers.matchers.TransfusionViewModelMatcher.hasSameStateAsTransfusionViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.PatientBackingForm;
import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PatientViewModel;
import org.jembi.bsis.viewmodel.TransfusionFullViewModel;
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
  private ComponentRepository componentRepository;
  @Mock
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private ComponentFactory componentFactory;
  @Mock
  private TransfusionReactionTypeFactory transfusionReactionTypeFactory;
  @Mock
  private LocationFactory locationFactory;

  @Test
  public void testCreateEntityUsingComponentCode_shouldReturnEntityInCorrectState() {
    String componentTypeCode = "123";
    String din = "123456";
    Date transfusionDate = new Date();

    PatientBackingForm patientForm = aPatientBackingForm().withId(1L).build();
    LocationBackingForm receivedFromForm = aUsageSiteBackingForm().withId(1L).build();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withId(1L)
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentTypeCode)
        .withReceivedFrom(receivedFromForm)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patientForm)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .build();

    Patient patient = aPatient().withId(1L).build();
    Component component = aComponent()
        .withId(1L)
        .withComponentCode(componentTypeCode)
        .withDonation(aDonation().withId(1L).withDonationIdentificationNumber(din).build())
        .build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion expectedEntity = aTransfusion()
        .withId(1L)
        .withComponent(component)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(receivedFrom);
    when(componentRepository.findComponentByCodeAndDIN(componentTypeCode, din)).thenReturn(component);
    when(patientFactory.createEntity(patientForm)).thenReturn(patient);

    Transfusion returnedEntity = transfusionFactory.createEntity(form);

    assertThat(returnedEntity, hasSameStateAsTransfusion(expectedEntity));
  }

  @Test
  public void testCreateEntityWithReaction_shouldReturnEntityInCorrectState() {
    String componentTypeCode = "123";
    String din = "123456";
    Date transfusionDate = new Date();

    PatientBackingForm patientForm = aPatientBackingForm().withId(1L).build();
    TransfusionReactionTypeBackingForm transfusionReactionTypeForm = aTransfusionReactionTypeBackingForm().withId(1L).build();
    LocationBackingForm receivedFromForm = aUsageSiteBackingForm().withId(1L).build();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withId(1L)
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentTypeCode)
        .withReceivedFrom(receivedFromForm)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
        .withPatient(patientForm)
        .withTransfusionReactionType(transfusionReactionTypeForm)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .build();

    TransfusionReactionType transfusionReactionType = aTransfusionReactionType().withId(1L).build();
    Patient patient = aPatient().withId(1L).build();
    Component component = aComponent()
        .withId(1L)
        .withComponentCode(componentTypeCode)
        .withDonation(aDonation().withId(1L).withDonationIdentificationNumber(din).build())
        .build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion expectedEntity = aTransfusion()
        .withId(1L)
        .withComponent(component)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withNotes("notes")
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(receivedFrom);
    when(componentRepository.findComponentByCodeAndDIN(componentTypeCode, din)).thenReturn(component);
    when(patientFactory.createEntity(patientForm)).thenReturn(patient);
    when(transfusionReactionTypeRepository.findById(1L)).thenReturn(transfusionReactionType);

    Transfusion returnedEntity = transfusionFactory.createEntity(form);

    assertThat(returnedEntity, hasSameStateAsTransfusion(expectedEntity));
  }

  @Test
  public void testCreateFullViewModel_shouldReturnViewModelWithCorrectState() {
    Date transfusionDate = new Date();

    TransfusionReactionType transfusionReactionType = aTransfusionReactionType().withId(1L).build();
    Patient patient = aPatient().withId(1L).build();
    Component component = aComponent().withId(1L).build();
    Location receivedFrom = aUsageSite().withId(1L).build();
    Transfusion transfusion = aTransfusion()
        .withId(1L)
        .withComponent(component)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    // setup expectations
    ComponentViewModel componentViewModel = aComponentViewModel().withId(1L).build();
    LocationViewModel receivedFromViewModel = aLocationViewModel().withId(1L).build();
    PatientViewModel patientViewModel = aPatientViewModel().withId(1L).build();
    TransfusionReactionTypeViewModel transfusionReactionTypeViewModel = aTransfusionReactionTypeViewModel()
        .withId(1L)
        .build();

    TransfusionFullViewModel expectedViewModel = aTransfusionFullViewModel()
        .withId(1L)
        .withComponent(componentViewModel)
        .withUsageSite(receivedFromViewModel)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patientViewModel)
        .withTransfusionReactionType(transfusionReactionTypeViewModel)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    // Setup mock
    when(componentFactory.createComponentViewModel(component)).thenReturn(componentViewModel);
    when(transfusionReactionTypeFactory.createTransfusionReactionTypeViewModel(transfusionReactionType))
        .thenReturn(transfusionReactionTypeViewModel);
    when(patientFactory.createViewModel(patient)).thenReturn(patientViewModel);
    when(locationFactory.createViewModel(receivedFrom)).thenReturn(receivedFromViewModel);

    // Run test
    TransfusionFullViewModel returnedViewModel = transfusionFactory.createFullViewModel(transfusion);

    //Verify
    assertThat(returnedViewModel, hasSameStateAsTransfusionFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateFullViewModels_returnsCollection() {
    Date transfusionDate = new Date();

    TransfusionReactionType transfusionReactionType = aTransfusionReactionType().withId(1L).build();
    Patient patient = aPatient().withId(1L).build();
    Component component = aComponent().withId(1L).build();
    Location receivedFrom = aUsageSite().withId(1L).build();
    List<Transfusion> transfusions = new ArrayList<>();
    transfusions.add(aTransfusion()
        .withId(1L)
        .withComponent(component)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build());
    transfusions.add(aTransfusion()
        .withId(2L)
        .withComponent(component)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.UNKNOWN)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build());

    //Run test
    List<TransfusionFullViewModel> returnedViewModels = transfusionFactory.createFullViewModels(transfusions);

    Assert.assertNotNull("View models have been created ", returnedViewModels);
    Assert.assertEquals("Correct number of view models is returned", 2 , returnedViewModels.size());
  }

  @Test
  public void testCreateFullViewModelsWithNull_returnsEmptyCollection() {
    //Run test
    List<TransfusionFullViewModel> viewModels = transfusionFactory.createFullViewModels(null);

    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertTrue("No view models", viewModels.isEmpty());
  }

  @Test
  public void testCreateTransfusionViewModel_shouldReturnViewModelWithCorrectState() {
    Date transfusionDate = new Date();
    String donationIdentificationNumber = "1234567";
    String componentTypeName = "componentType";
    String componentCode = "1234";
    Donation donation = DonationBuilder.aDonation()
        .withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component component = aComponent()
        .withId(1L)
        .withComponentType(aComponentType()
            .withComponentTypeName(componentTypeName)
            .build())
        .withComponentCode(componentCode)
        .withDonation(donation)
        .build();

    Location receivedFrom = aUsageSite().withId(1L).build();
    Transfusion transfusion = aTransfusion()
        .withId(1L)
        .withComponent(component)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    // setup expectations
    LocationViewModel receivedFromViewModel = aLocationViewModel().withId(1L).build();

    TransfusionViewModel expectedViewModel = aTransfusionViewModel()
        .withId(1L)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withComponentCode(componentCode)
        .withComponentType(componentTypeName)
        .withUsageSite(receivedFromViewModel)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withDateTransfused(transfusionDate)
        .build();

    // Setup mock
    when(locationFactory.createViewModel(receivedFrom)).thenReturn(receivedFromViewModel);

    // Run test
    TransfusionViewModel returnedViewModel = transfusionFactory.createViewModel(transfusion);

    //Verify
    assertThat(returnedViewModel, hasSameStateAsTransfusionViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTransfusionViewModels_returnsCollection() {
    Date transfusionDate = new Date();
    String donationIdentificationNumber = "1234567";
    TransfusionReactionType transfusionReactionType = aTransfusionReactionType().withId(1L).build();
    Patient patient = aPatient().withId(1L).build();

    Donation donation = DonationBuilder.aDonation()
        .withDonationIdentificationNumber(donationIdentificationNumber).build();
    String componentTypeName = "componentType";
    String componentCode = "1234";
    Component component = aComponent()
        .withId(1L)
        .withComponentType(aComponentType()
            .withComponentTypeName(componentTypeName)
            .build())
        .withComponentCode(componentCode)
        .withDonation(donation)
        .build();

    Location receivedFrom = aUsageSite().withId(1L).build();
    List<Transfusion> transfusions = new ArrayList<>();
    transfusions.add(aTransfusion()
        .withId(1L)
        .withComponent(component)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build());
    transfusions.add(aTransfusion()
        .withId(2L)
        .withComponent(component)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(patient)
        .withTransfusionReactionType(transfusionReactionType)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build());

    LocationViewModel receivedFromViewModel = aLocationViewModel().withId(1L).build();

    TransfusionViewModel expectedViewModel1 = aTransfusionViewModel()
        .withId(1L)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withComponentCode(componentCode)
        .withComponentType(componentTypeName)
        .withUsageSite(receivedFromViewModel)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withDateTransfused(transfusionDate)
        .build();
    
    TransfusionViewModel expectedViewModel2 = aTransfusionViewModel()
        .withId(2L)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withComponentCode(componentCode)
        .withComponentType(componentTypeName)
        .withUsageSite(receivedFromViewModel)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withDateTransfused(transfusionDate)
        .build();

    when(locationFactory.createViewModel(receivedFrom)).thenReturn(receivedFromViewModel);

    //Run test
    List<TransfusionViewModel> returnedViewModels = transfusionFactory.createViewModels(transfusions);

    Assert.assertNotNull("View models have been created ", returnedViewModels);
    Assert.assertEquals("Correct number of view models is returned", 2 , returnedViewModels.size());
    // Verify
    assertThat(returnedViewModels.get(0), hasSameStateAsTransfusionViewModel(expectedViewModel1));
    // Verify
    assertThat(returnedViewModels.get(1), hasSameStateAsTransfusionViewModel(expectedViewModel2));
  }

  @Test
  public void testCreateViewModelsWithNull_returnsEmptyCollection() {
    // Run test
    List<TransfusionViewModel> viewModels = transfusionFactory.createViewModels(null);

    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertTrue("No view models", viewModels.isEmpty());
  }
}