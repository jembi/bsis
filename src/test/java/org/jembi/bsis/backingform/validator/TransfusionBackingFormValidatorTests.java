package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aProcessingSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aUsageSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.PatientBackingFormBuilder.aPatientBackingForm;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.TransfusionBackingFormBuilder.aTransfusionBackingForm;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBackingFormBuilder.aTransfusionReactionTypeBackingForm;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class TransfusionBackingFormValidatorTests extends UnitTestSuite {
  
  @InjectMocks
  private TransfusionBackingFormValidator validator;

  @Mock
  private ComponentTypeRepository componentTypeRepository;

  @Mock
  private DonationRepository donationRepository;

  @Mock
  private ComponentRepository componentRepository;

  @Mock
  private LocationRepository locationRepository;

  @Mock
  private TransfusionRepository transfusionRepository;

  @Mock
  private DateGeneratorService dateGeneratorService;
  
  private static final UUID COMPONENT_ID = UUID.randomUUID();

  private void setupDateGeneratorServiceMocks(Date transfusedDate, Date componentCreatedOnDate, boolean includeComponentCreatedOnMock) {
    when(dateGeneratorService.generateLocalDate(transfusedDate)).thenReturn(new LocalDate(transfusedDate));
    when(dateGeneratorService.generateLocalDate()).thenReturn(new LocalDate(new Date()));
    if (includeComponentCreatedOnMock) {
      when(dateGeneratorService.generateLocalDate(componentCreatedOnDate))
        .thenReturn(new LocalDate(componentCreatedOnDate));
    }
  }

  private void setupDateGeneratorServiceMocks(Date transfusedDate, Date componentCreatedOnDate) {
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate, true);
  }

  private void setupDateGeneratorServiceMocks(Date transfusedDate) {
    setupDateGeneratorServiceMocks(transfusedDate, null, false);
  }

  @Test
  public void testValidateValidTransfusionFormWithDINAndComponentCode_shouldntGetErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateTransfusionUpdateFormWithComponentCodeAndInvalidComponentStatus_shoulGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();

    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    UUID transfusionId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withId(transfusionId)
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Transfusion existingTransfusion = aTransfusion()
        .withId(transfusionId)
        .withComponent(component)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite().withId(usageSiteId).build())
        .withPatient(aPatient()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    when(transfusionRepository.findTransfusionById(transfusionId)).thenReturn(existingTransfusion);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentCode").getCode(), is("errors.invalid.componentStatus"));
    assertThat(errors.getFieldError("componentCode").getDefaultMessage(), is("There is no component in TRANSFUSED state for specified donationIdentificationNumber and componentCode"));
  }

  @Test
  public void testValidateTransfusionUpdateFormWithComponentTypeAndInvalidComponentStatus_shoulGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();

    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    List<Component> components = Arrays.asList(component);

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    UUID transfusionId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withId(transfusionId)
        .withDonationIdentificationNumber(din)
        .withComponentType(aComponentTypeBackingForm().withId(componentTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Transfusion existingTransfusion = aTransfusion()
        .withId(transfusionId)
        .withComponent(component)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite().withId(usageSiteId).build())
        .withPatient(aPatient()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    when(transfusionRepository.findTransfusionById(transfusionId)).thenReturn(existingTransfusion);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentType").getCode(), is("errors.invalid.componentStatus"));
    assertThat(errors.getFieldError("componentType").getDefaultMessage(), is("There is no component in TRANSFUSED state for specified donationIdentificationNumber and componentType"));
  }

  @Test
  public void testValidateTransfusionUpdateFormWithDifferentComponentType_shoulGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();

    UUID differentComponentId = UUID.randomUUID();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.TRANSFUSED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    List<Component> components = Arrays.asList(component);

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    UUID transfusionId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withId(transfusionId)
        .withDonationIdentificationNumber(din)
        .withComponentType(aComponentTypeBackingForm().withId(componentTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Transfusion existingTransfusion = aTransfusion()
        .withId(transfusionId)
        .withComponent(aComponent().withId(differentComponentId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite().withId(usageSiteId).build())
        .withPatient(aPatient()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    when(transfusionRepository.findTransfusionById(transfusionId)).thenReturn(existingTransfusion);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("donationIdentificationNumber").getCode(), is("errors.invalid.componentCantBeEdited"));
    assertThat(errors.getFieldError("donationIdentificationNumber").getDefaultMessage(), is("The component cannot be modified"));
  }

  @Test
  public void testValidateValidTransfusionFormWithDINAndComponentType_shouldntGetErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    List<Component> components = new ArrayList<Component>();
    components.add(aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build());

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    UUID componentTypeId = UUID.randomUUID();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentType(aComponentTypeBackingForm().withId(componentTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidateValidTransfusionFormWithDINAndComponentCodeAndComponentType_shouldntGetErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();

    UUID componentTypeId = UUID.randomUUID();

    List<Component> components = new ArrayList<Component>();
    components.add(aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withComponentType(aComponentType().withId(componentTypeId).build())
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build());

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withComponentType(aComponentTypeBackingForm().withId(componentTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(components.get(0));
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateransfusionFormWithInvalidDIN_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenThrow(NoResultException.class);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("donationIdentificationNumber").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateTransfusionFormWithNoDIN_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String componentCode = "1234";

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(null)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("donationIdentificationNumber").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateTransfusionFormWithDINAndInvalidComponentType_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    
    List<Component> components = new ArrayList<Component>();
    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    UUID componentTypeId = UUID.randomUUID();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentType(aComponentTypeBackingForm().withId(componentTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(false);
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentType").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateTransfusionFormWithDINAndComponentTypeWithNoComponents_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    
    List<Component> components = new ArrayList<Component>();
    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    UUID componentTypeId = UUID.randomUUID();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentType(aComponentTypeBackingForm().withId(componentTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentType").getCode(), is("errors.invalid.noComponents"));
  }

  @Test
  public void testValidateTransfusionFormWithDINAndComponentTypeWithMultipleComponents_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";
    UUID componentId = UUID.randomUUID();

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    List<Component> components = new ArrayList<Component>();
    components.add(aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode + "-01")
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build());

    components.add(aComponent()
        .withId(componentId)
        .withComponentCode(componentCode + "-02")
        .withStatus(ComponentStatus.PROCESSED)
        .withCreatedOn(componentCreatedOnDate)
        .build());

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    UUID componentTypeId = UUID.randomUUID();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentType(aComponentTypeBackingForm().withId(componentTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentType").getCode(), is("errors.invalid.multipleComponents"));
  }

  @Test
  public void testValidateTransfusionFormWithDINAndNoComponentCodeOrComponentType_shouldGetTwoErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    
    List<Component> components = new ArrayList<Component>();
    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(2));
    assertThat(errors.getFieldError("componentType").getCode(), is("errors.required"));
    assertThat(errors.getFieldError("componentCode").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateTransfusionFormWithDINAndNoComponentCodeAndComponentTypeWithNullId_shouldGetTwoErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    
    List<Component> components = new ArrayList<Component>();
    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withDateTransfused(transfusedDate)
        .withComponentType(aComponentTypeBackingForm().withId(null).build())
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(2));
    assertThat(errors.getFieldError("componentType").getCode(), is("errors.required"));
    assertThat(errors.getFieldError("componentCode").getCode(), is("errors.required"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateTransfusionFormWithDINAndInvalidComponentCode_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();

    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation().withDonationIdentificationNumber(din).withComponent(component).build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm().withDonationIdentificationNumber(din)
        .withComponentCode(componentCode).withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY).withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm().withName1("name1").withName2("name2").build()).build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenThrow(NoResultException.class);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentCode").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateTransfusionFormWithDINAndComponentCodeAndDifferentComponentType_shouldGetOneErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();

    UUID componetTypeId = UUID.randomUUID();
    List<Component> components = new ArrayList<Component>();
    components.add(aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withComponentType(aComponentType().withId(componetTypeId).build())
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build());

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();
    UUID differentComponetTypeId = UUID.randomUUID();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withComponentType(aComponentTypeBackingForm().withId(differentComponetTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(differentComponetTypeId)).thenReturn(true);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withComponentType(aComponentType().withId(differentComponetTypeId).build())
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build());
    when(componentRepository.findComponentsByDINAndType(din, differentComponetTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentType").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateTransfusionFormWithDINAndComponentCodeAndInvalidComponentStatus_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.PROCESSED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentCode").getCode(), is("errors.invalid.componentStatus"));
  }

  @Test
  public void testValidateTransfusionFormWithDINAndComponentTypeAndInvalidComponentStatus_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    List<Component> components = new ArrayList<Component>();
    components.add(aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.PROCESSED)
        .withCreatedOn(componentCreatedOnDate)
        .build());

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponents(components)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    UUID componentTypeId = UUID.randomUUID();
    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentType(aComponentTypeBackingForm().withId(componentTypeId).build())
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(components);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("componentType").getCode(), is("errors.invalid.componentStatus"));
  }

  @Test
  public void testValidateTransfusionFormWithNoTransfusionOutcome_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("transfusionOutcome").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateTransfusionFormWithTransfusionOutcomeReactionWithNoTransfusionReactionType_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aUsageSite)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
        .withTransfusionReactionType(null)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("transfusionReactionType").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateTransfusionFormWithUneventfulTransfusionOutcomeWithTransfusionReactionType_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aUsageSite)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withTransfusionReactionType(aTransfusionReactionTypeBackingForm().build())
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("transfusionReactionType").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateTransfusionFormWithNotTransfusedTransfusionOutcomeWithTransfusionReactionType_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aUsageSite)
        .withTransfusionOutcome(TransfusionOutcome.NOT_TRANSFUSED)
        .withTransfusionReactionType(aTransfusionReactionTypeBackingForm().build())
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("transfusionReactionType").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateTransfusionFormWithNoTransfusionDate_shouldGetOneError() {
    // Set up data
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withReceivedFrom(aUsageSite)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("dateTransfused").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateTransfusionFormWithInvalidTransfusionDateInFuture_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).plusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withReceivedFrom(aUsageSite)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("dateTransfused").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateTransfusionFormWithValidTransfusionDateOnSameDateAsComponentCreatedOnDate_shouldGetNoErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(2).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(2).toDate();

    Component component = aComponent().withId(COMPONENT_ID).withComponentCode(componentCode).withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate).build();

    Donation donation = aDonation().withDonationIdentificationNumber(din).withComponent(component).build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm().withDonationIdentificationNumber(din)
        .withComponentCode(componentCode).withReceivedFrom(aUsageSite).withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(aPatientBackingForm().withName1("name1").withName2("name2").build()).build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateTransfusionFormWithValidTransfusionDateOnSameDateAsComponentCreatedOnDateOnCurrentDate_shouldGetNoErrors() {
    // Set up data
    Date transfusedDate = new Date();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = new Date();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withReceivedFrom(aUsageSite)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateTransfusionFormWithInvalidTransfusionDateBeforeComponentDate_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(20).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withReceivedFrom(aUsageSite)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("dateTransfused").getCode(), is("errors.invalid.dateTransfusedAfterComponentCreated"));
  }

  @Test
  public void testValidateTransfusionFormWithNoRecievedFromLocation_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("receivedFrom").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateTransfusionFormWithInvalidRecievedFromLocation_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID aProcessingSiteId = UUID.randomUUID();
    Location aProcessingSite = aProcessingSite().withId(aProcessingSiteId).build();
    LocationBackingForm aProcessingSiteForm = aProcessingSiteBackingForm().withId(aProcessingSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aProcessingSiteForm)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(aProcessingSiteId)).thenReturn(aProcessingSite);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("receivedFrom").getCode(), is("errors.invalid"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateTransfusionFormWithRecievedFromLocationWithInvalidId_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aUsageSite)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .withPatient(aPatientBackingForm()
            .withName1("name1")
            .withName2("name2")
            .build())
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenThrow(NoResultException.class);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("receivedFrom").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateTransfusionFormWithNoPatient_shouldGetOneError() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aUsageSite)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("patient").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateTransfusionFormWithPatientWithNoName1AndName2_shouldGetTwoErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
        .build())
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(2));

    assertThat(errors.getFieldError("patient.name1").getCode(), is("errors.required"));
    assertThat(errors.getFieldError("patient.name2").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateTransfusionFormWithPatientWithLongName1AndName2_shouldGetTwoErrors() {
    // Set up data
    Date transfusedDate = (new DateTime()).minusDays(5).toDate();
    String din = "12345";
    String componentCode = "1234";

    Date componentCreatedOnDate = (new DateTime()).minusDays(10).toDate();
    
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.ISSUED)
        .withCreatedOn(componentCreatedOnDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber(din)
        .withComponent(component)
        .build();

    UUID usageSiteId = UUID.randomUUID();
    Location aUsageSiteLocation = aUsageSite().withId(usageSiteId).build();
    LocationBackingForm aUsageSite = aUsageSiteBackingForm().withId(usageSiteId).build();

    TransfusionBackingForm form = aTransfusionBackingForm()
        .withDonationIdentificationNumber(din)
        .withComponentCode(componentCode)
        .withDateTransfused(transfusedDate)
        .withReceivedFrom(aUsageSite)
        .withPatient(aPatientBackingForm()
            .withName1("A very long name longer than 20 characters")
            .withName2("A very long name longer than 20 characters")
            .build())
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "transfusion");

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(componentRepository.findComponentByCodeAndDIN(componentCode, din)).thenReturn(component);
    when(locationRepository.getLocation(usageSiteId)).thenReturn(aUsageSiteLocation);
    setupDateGeneratorServiceMocks(transfusedDate, componentCreatedOnDate);

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(2));

    assertThat(errors.getFieldError("patient.name1").getCode(), is("errors.invalid"));
    assertThat(errors.getFieldError("patient.name2").getCode(), is("errors.invalid"));
  }
}