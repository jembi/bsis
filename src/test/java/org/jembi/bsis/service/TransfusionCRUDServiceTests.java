package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
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

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
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

  @Test
  public void testCreateTransfusionUsingComponentType_shouldCreateAndUpdateComponentStatus() throws Exception {
    String donationIdentificationNumber = "1234567";
    Date transfusionDate = new Date();
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    Patient patient = aPatient().withId(1L).build();
    Long componentTypeId = 1L;
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion transfusion = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    Component transfusedComponent = aComponent().withId(1L).withComponentType(componentType).build();
    List<Component> returnedComponents = Arrays.asList(transfusedComponent);
    Transfusion expectedTransfusion = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withComponent(transfusedComponent)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    when(componentCRUDService.findComponentsByDINAndType(transfusion.getDonationIdentificationNumber(), componentTypeId))
        .thenReturn(returnedComponents);

    transfusionCRUDService.createTransfusion(transfusion, componentType.getId());

    verify(componentCRUDService).transfuseComponent(argThat(hasSameStateAsComponent(transfusedComponent)));
    verify(transfusionRepository).save(argThat(hasSameStateAsTransfusion(expectedTransfusion)));
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateTransfusionUsingComponentTypeThatHasMultiple_shouldThrow() throws Exception {
    String donationIdentificationNumber = "1234567";
    Date transfusionDate = new Date();
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    Patient patient = aPatient().withId(1L).build();
    Long componentTypeId = 1L;
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion transfusion = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    Component transfusedComponent1 = aComponent().withId(1L).withComponentType(componentType).build();
    Component transfusedComponent2 = aComponent().withId(2L).withComponentType(componentType).build();
    List<Component> returnedComponents = Arrays.asList(transfusedComponent1, transfusedComponent2);

    when(componentCRUDService.findComponentsByDINAndType(transfusion.getDonationIdentificationNumber(), componentTypeId))
        .thenReturn(returnedComponents);

    transfusionCRUDService.createTransfusion(transfusion, componentType.getId());
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateTransfusionUsingComponentTypeThatHasNone_shouldThrow() throws Exception {
    String donationIdentificationNumber = "1234567";
    Date transfusionDate = new Date();
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    Patient patient = aPatient().withId(1L).build();
    Long componentTypeId = 1L;
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion transfusion = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();
    List<Component> returnedComponents = new ArrayList<>();

    when(componentCRUDService.findComponentsByDINAndType(transfusion.getDonationIdentificationNumber(), componentTypeId))
        .thenReturn(returnedComponents);

    transfusionCRUDService.createTransfusion(transfusion, componentType.getId());
  }

  @Test
  public void testCreateTransfusion_shouldCreateAndUpdateComponentStatus() throws Exception {
    String donationIdentificationNumber = "1234567";
    Date transfusionDate = new Date();
    TransfusionOutcome transfusionOutcome = TransfusionOutcome.TRANSFUSED_UNEVENTFULLY;
    String notes = "notes";
    Patient patient = aPatient().withId(1L).build();
    Long componentTypeId = 1L;
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    Component transfusedComponent = aComponent().withId(1L).withComponentType(componentType).build();
    Location receivedFrom = aLocation().withId(1L).build();
    Transfusion transfusion = aTransfusion()
        .withId(1L)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withComponent(transfusedComponent)
        .withReceivedFrom(receivedFrom)
        .withTransfusionOutcome(transfusionOutcome)
        .withPatient(patient)
        .withNotes(notes)
        .withDateTransfused(transfusionDate)
        .thatIsNotDeleted()
        .build();

    transfusionCRUDService.createTransfusion(transfusion, componentType.getId());
    
    verify(componentCRUDService).transfuseComponent(argThat(hasSameStateAsComponent(transfusedComponent)));
    verify(transfusionRepository).save(argThat(hasSameStateAsTransfusion(transfusion)));
  }

  @Test
  public void testFindTransfusionsWithNotNullDin_shouldDoFindByDinSearch() {
    // set up mocks
    when(transfusionRepository.findTransfusionsByDINAndComponentCode("1000000", null)).thenReturn(null);
    // run test
    transfusionCRUDService.findTransfusions("1000000", null, null, null, null, null, null);
    // verify
    verify(transfusionRepository).findTransfusionsByDINAndComponentCode("1000000", null);
  }

  @Test
  public void testFindTransfusionsWithNullDin_shouldDoFindTransfusionsSearch() {
    // set up mocks
    when(transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(null, null, null, null, null))
        .thenReturn(null);
    // run test
    transfusionCRUDService.findTransfusions(null, null, null, null, null, null, null);
    // verify
    verify(transfusionRepository).findTransfusionByComponentTypeAndSiteAndOutcome(null, null, null, null,
        null);
  }

}
