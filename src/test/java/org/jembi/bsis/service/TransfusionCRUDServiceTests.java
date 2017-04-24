package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.TransfusionRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TransfusionCRUDServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private TransfusionCRUDService transfusionCRUDService;
  @Mock
  private ComponentCRUDService componentCRUDService;
  @Mock
  private TransfusionRepository transfusionRepository;
  @Mock
  private ComponentRepository componentRepository;
  
  private static final UUID COMPONENT_ID = UUID.randomUUID();

  @Test
  public void testCreateTransfusion_shouldCreateAndUpdateComponentStatus() throws Exception {
    Date transfusionDate = new Date();
    String din = "1234567";
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    UUID patientId = UUID.randomUUID();
    Patient patient = aPatient().withId(patientId).build();
    UUID componentTypeId = UUID.randomUUID();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    Component transfusedComponent = aComponent().withId(COMPONENT_ID).withComponentType(componentType).build();
    Location receivedFrom = aLocation().withId(UUID.randomUUID()).build();
    Transfusion transfusion = aTransfusion()
        .withId(UUID.randomUUID())
        .withComponent(transfusedComponent)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    transfusionCRUDService.createTransfusion(transfusion, din, null, componentTypeId);
    
    verify(componentCRUDService).transfuseComponent(argThat(hasSameStateAsComponent(transfusedComponent)));
    verify(transfusionRepository).save(argThat(hasSameStateAsTransfusion(transfusion)));
  }

  @Test
  public void testFindTransfusionsWithDinAndComponentCode_shouldReturnOneTransfusion() {
    Transfusion transfusion = aTransfusion().withId(UUID.randomUUID()).build();

    // set up mocks
    when(transfusionRepository.findTransfusionByDINAndComponentCode("1000000", "1001")).thenReturn(transfusion);

    // run test
    List<Transfusion> transfusions = transfusionCRUDService.findTransfusions("1000000", "1001", null, null, null, null, null);

    // assert and verify
    assertThat(transfusions.size(), is(1));
    assertThat(transfusions.get(0), hasSameStateAsTransfusion(transfusion));
    verify(transfusionRepository).findTransfusionByDINAndComponentCode("1000000", "1001");
  }

  @Test
  public void testFindTransfusionsWithDinAndComponentCodeThatDoesNotExist_shouldReturnEmptyResults() {
    // set up mocks
    when(transfusionRepository.findTransfusionByDINAndComponentCode("1000000", "1001")).thenThrow(new NoResultException());

    // run test
    List<Transfusion> transfusions = transfusionCRUDService.findTransfusions("1000000", "1001", null, null, null, null, null);

    // assert and verify
    assertThat(transfusions.size(), is(0));
    verify(transfusionRepository).findTransfusionByDINAndComponentCode("1000000", "1001");
  }

  @Test
  public void testCreateTransfusionUsingComponentType_shouldCreateAndUpdateComponentStatus() throws Exception {
    String donationIdentificationNumber = "1234567";
    Date transfusionDate = new Date();
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    UUID patientId = UUID.randomUUID();
    Patient patient = aPatient().withId(patientId).build();
    UUID componentTypeId = UUID.randomUUID();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    Location receivedFrom = aLocation().withId(UUID.randomUUID()).build();
    UUID transfusionId = UUID.randomUUID();
    Transfusion transfusion = aTransfusion()
        .withId(transfusionId)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    Component transfusedComponent = aComponent().withId(COMPONENT_ID).withComponentType(componentType).build();
    List<Component> returnedComponents = Arrays.asList(transfusedComponent);
    Transfusion expectedTransfusion = aTransfusion()
        .withId(transfusionId)
        .withComponent(transfusedComponent)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    when(componentCRUDService.findComponentsByDINAndType(donationIdentificationNumber, componentTypeId))
        .thenReturn(returnedComponents);

    transfusionCRUDService.createTransfusion(transfusion, donationIdentificationNumber, null, componentType.getId());

    verify(componentCRUDService).transfuseComponent(argThat(hasSameStateAsComponent(transfusedComponent)));
    verify(transfusionRepository).save(argThat(hasSameStateAsTransfusion(expectedTransfusion)));
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateTransfusionUsingComponentTypeThatHasMultiple_shouldThrow() throws Exception {
    String donationIdentificationNumber = "1234567";
    Date transfusionDate = new Date();
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    UUID patientId = UUID.randomUUID();
    UUID componentId = UUID.randomUUID();
    Patient patient = aPatient().withId(patientId).build();
    UUID componentTypeId = UUID.randomUUID();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    Location receivedFrom = aLocation().withId(UUID.randomUUID()).build();
    Transfusion transfusion = aTransfusion()
        .withId(UUID.randomUUID())
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    Component transfusedComponent1 = aComponent().withId(COMPONENT_ID).withComponentType(componentType).build();
    Component transfusedComponent2 = aComponent().withId(componentId).withComponentType(componentType).build();
    List<Component> returnedComponents = Arrays.asList(transfusedComponent1, transfusedComponent2);

    when(componentCRUDService.findComponentsByDINAndType(donationIdentificationNumber, componentTypeId))
        .thenReturn(returnedComponents);

    transfusionCRUDService.createTransfusion(transfusion, donationIdentificationNumber, null, componentType.getId());
  }

  @Test
  public void testCreateTransfusionUsingComponentCode_shouldCreateAndUpdateComponentStatus() throws Exception {
    String donationIdentificationNumber = "1234567";
    String transfusedComponentCode = "2001-01";
    Date transfusionDate = new Date();
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    UUID patientId = UUID.randomUUID();
    Patient patient = aPatient().withId(patientId).build();
    Location receivedFrom = aLocation().withId(UUID.randomUUID()).build();
    UUID transfusionId = UUID.randomUUID();
    Transfusion transfusion = aTransfusion()
        .withId(transfusionId)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    Component transfusedComponent = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(transfusedComponentCode)
        .build();
    Transfusion expectedTransfusion = aTransfusion()
        .withId(transfusionId)
        .withComponent(transfusedComponent)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    when(componentRepository.findComponentByCodeAndDIN(transfusedComponentCode, donationIdentificationNumber))
        .thenReturn(transfusedComponent);

    transfusionCRUDService.createTransfusion(transfusion, donationIdentificationNumber, transfusedComponentCode, null);

    verify(componentCRUDService).transfuseComponent(argThat(hasSameStateAsComponent(transfusedComponent)));
    verify(transfusionRepository).save(argThat(hasSameStateAsTransfusion(expectedTransfusion)));
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateTransfusionUsingComponentTypeThatHasNone_shouldThrow() throws Exception {
    String donationIdentificationNumber = "1234567";
    Date transfusionDate = new Date();
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    UUID patientId = UUID.randomUUID();
    Patient patient = aPatient().withId(patientId).build();
    UUID componentTypeId = UUID.randomUUID();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    Location receivedFrom = aLocation().withId(UUID.randomUUID()).build();
    Transfusion transfusion = aTransfusion()
        .withId(UUID.randomUUID())
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    List<Component> returnedComponents = new ArrayList<>();

    when(componentCRUDService.findComponentsByDINAndType(donationIdentificationNumber, componentTypeId))
        .thenReturn(returnedComponents);

    transfusionCRUDService.createTransfusion(transfusion, donationIdentificationNumber, null, componentType.getId());
  }

  @Test
  public void testFindTransfusionsWithNullDin_shouldDoFindTransfusionsSearch() {
    // set up mocks
    when(transfusionRepository.findTransfusions(null, null, null, null, null))
        .thenReturn(null);
    // run test
    transfusionCRUDService.findTransfusions(null, null, null, null, null, null, null);
    // verify
    verify(transfusionRepository).findTransfusions(null, null, null, null,
        null);
  }

  @Test
  public void testDeleteTransfusion_shouldMarkAsDeleted() {
    // Set up

    Transfusion transfusion = aTransfusion()
        .withId(UUID.randomUUID())
        .withDateTransfused(new Date())
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withReceivedFrom(aUsageSite().build())
        .withComponent(aComponent()
            .withId(COMPONENT_ID)
            .withStatus(ComponentStatus.TRANSFUSED)
            .build())
        .build();

    // Mocks
    when(transfusionRepository.findTransfusionById(transfusion.getId())).thenReturn(transfusion);
    // Test
    transfusionCRUDService.deleteTransfusion(transfusion.getId());

    // Assertions
    assertThat("Transfusion has been deleted", transfusion.getIsDeleted());
  }

  @Test(expected = IllegalStateException.class)
  public void testDeleteTransfusionDeleted_shouldThrow() {

    UUID transfusionId = UUID.randomUUID();
    // Mocks
    when(transfusionRepository.findTransfusionById(transfusionId)).thenReturn(null);

    // Test
    transfusionCRUDService.deleteTransfusion(transfusionId);
  }
}
