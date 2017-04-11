package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.jembi.bsis.helpers.builders.TransfusionSummaryDTOBuilder.aTransfusionSummaryDTO;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;
import static org.jembi.bsis.helpers.matchers.TransfusionSummaryDTOMatcher.hasSameStateAsTransfusionSummaryDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.hamcrest.core.IsNull;
import org.jembi.bsis.dto.TransfusionSummaryDTO;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransfusionRepositoryTests extends SecurityContextDependentTestSuite {
  
  @Autowired
  private TransfusionRepository transfusionRepository;
  
  @Test
  public void testSaveTransfusion_shouldAlsoPersistPatient() {
    
    Transfusion transfusion = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withReceivedFrom(aUsageSite()
            .withName("Received From")
            .buildAndPersist(entityManager))
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .build();
    
    Transfusion savedTransfusion = transfusionRepository.update(transfusion);

    // check that the transfusion entity was persisted correctly
    assertThat(savedTransfusion.getId(), is(IsNull.notNullValue()));
    // check that the patient entity has been persisted in a cascade event
    assertThat(savedTransfusion.getPatient(), is(IsNull.notNullValue()));
    assertThat(savedTransfusion.getPatient().getId(), is(IsNull.notNullValue()));

    Transfusion retrievedTransfusion = entityManager
        .createQuery("SELECT t from Transfusion t where t.id = :id", Transfusion.class)
        .setParameter("id", savedTransfusion.getId())
        .getSingleResult();

    assertThat(retrievedTransfusion, hasSameStateAsTransfusion(savedTransfusion));
  }

  @Test
  public void testFindTransfusionByIdWithExistingTransfusion_shouldReturnExistingTransfusion() {
    // Set up
    Transfusion transfusion = aTransfusion()
        .withDateTransfused(new Date())
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withReceivedFrom(aUsageSite().buildAndPersist(entityManager))
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .buildAndPersist(entityManager);

    // Test
    Transfusion returnedTransfusion = transfusionRepository.findTransfusionById(transfusion.getId());

    // Verify
    Assert.assertEquals("Transfusion was found", transfusion, returnedTransfusion);
  }

  @Test(expected = NoResultException.class)
  public void testFindTransfusionByIdWithNoExistingTransfusion_shouldThrow() {
    // Test
    transfusionRepository.findTransfusionById(UUID.randomUUID());
  }

  @Test(expected = NoResultException.class)
  public void testFindTransfusionByIdThatIsDeleted_shouldThrow() {
    // Set up
    Transfusion transfusion = aTransfusion()
        .withDateTransfused(new Date())
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withReceivedFrom(aUsageSite().buildAndPersist(entityManager))
        .thatIsDeleted()
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .buildAndPersist(entityManager);

    // Test
    transfusionRepository.findTransfusionById(transfusion.getId());
  }
  
  @Test
  public void testFindTransfusionSummaryRecordedForUsageSiteForPeriod_shouldReturnRightDtos() {
    
    Date startDate = new DateTime().minusDays(60).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();
    TransfusionReactionType transfusionReactionType = aTransfusionReactionType()
        .buildAndPersist(entityManager);
    Location receivedFrom = aUsageSite()
        .withName("Harare")
        .buildAndPersist(entityManager);
    
    Transfusion expectedTransfusion1 = aTransfusion()
        .withDateTransfused(startDate)
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withTransfusionReactionType(null)
        .withReceivedFrom(aUsageSite()
            .withName("Received From")
            .buildAndPersist(entityManager))
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .buildAndPersist(entityManager);
    
    Transfusion expectedTransfusion2 = aTransfusion()
        .withDateTransfused(startDate)
        .withPatient(aPatient()
            .withName1("Name 2")
            .withName2("Name 2")
            .build())
        .withTransfusionReactionType(transfusionReactionType)
        .withReceivedFrom(aUsageSite()
            .withName("Received From Station")
            .buildAndPersist(entityManager))
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .buildAndPersist(entityManager);
    
    //  exclude by transfusion date
    aTransfusion()
      .withDateTransfused(new DateTime().plusDays(1000).toDate())
      .withPatient(aPatient()
          .withName1("Name 3")
          .withName2("Name 3")
          .build())
      .withReceivedFrom(receivedFrom)
      .withTransfusionOutcome(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
      .withTransfusionReactionType(aTransfusionReactionType().withName("Reaction Name2").buildAndPersist(entityManager))
      .withComponent(aComponent()
          .buildAndPersist(entityManager))
      .buildAndPersist(entityManager);
    
    UUID receivedFromId = null;
    
    List<TransfusionSummaryDTO> expectedTransfusionSummaryDTOs = new ArrayList<>();
    expectedTransfusionSummaryDTOs.add(aTransfusionSummaryDTO()
        .withCount(1)
        .withTransfusionOutcome(expectedTransfusion1.getTransfusionOutcome())
        .withTransfusionReactionType(expectedTransfusion1.getTransfusionReactionType())
        .withTransfusionSite(expectedTransfusion1.getReceivedFrom())
        .build());
    expectedTransfusionSummaryDTOs.add(aTransfusionSummaryDTO()
        .withCount(1)
        .withTransfusionOutcome(expectedTransfusion2.getTransfusionOutcome())
        .withTransfusionReactionType(expectedTransfusion2.getTransfusionReactionType())
        .withTransfusionSite(expectedTransfusion2.getReceivedFrom())
        .build());    
    
    List<TransfusionSummaryDTO> returnedTransfusionSummaryDTOs = transfusionRepository.findTransfusionSummaryRecordedForUsageSiteForPeriod(receivedFromId, startDate, endDate);
    
    // check that the transfusion summary count returned is for transfusions within range
    assertThat(returnedTransfusionSummaryDTOs.size(), is(2));
    // check that returned DTOs are same as expected
    assertThat(returnedTransfusionSummaryDTOs.get(0), hasSameStateAsTransfusionSummaryDTO(expectedTransfusionSummaryDTOs.get(0)));
    assertThat(returnedTransfusionSummaryDTOs.get(1), hasSameStateAsTransfusionSummaryDTO(expectedTransfusionSummaryDTOs.get(1)));
  }
  
  @Test
  public void testFindTransfusionSummaryRecordedForUsageSiteForPeriodWithUsageSite_shouldReturnRightDtos() {

    Date startDate = new DateTime().minusDays(60).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();
    TransfusionReactionType transfusionReactionType = aTransfusionReactionType()
        .buildAndPersist(entityManager);    
    Location receivedFrom1 = aUsageSite()
        .withName("Harare")
        .buildAndPersist(entityManager);    
    Location receivedFrom2 = aUsageSite()
        .withName("Masvingo")
        .buildAndPersist(entityManager);

    aTransfusion()
        .withDateTransfused(startDate)
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withTransfusionReactionType(transfusionReactionType)
        .withReceivedFrom(receivedFrom1)
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .buildAndPersist(entityManager);

    aTransfusion()
      .withDateTransfused(startDate)
      .withPatient(aPatient()
          .withName1("Name 2")
          .withName2("Name 2")
          .build())
      .withTransfusionReactionType(transfusionReactionType)
      .withReceivedFrom(receivedFrom2)
      .withComponent(aComponent()
          .buildAndPersist(entityManager))
      .buildAndPersist(entityManager);
    
    List<TransfusionSummaryDTO> transfusionSummaryDTOs = transfusionRepository.findTransfusionSummaryRecordedForUsageSiteForPeriod(receivedFrom2.getId(), startDate, endDate);

    // check that the transfusion summary count returned is equal to persisted transfusions count for the usageSite
    assertThat(transfusionSummaryDTOs.size(), is(1));
  }

  @Test
  public void testFindTransfusionByDINAndCodeWithoutFlagCharacters_shouldReturnCorrectFields() {
    //Set up fixture
    String  donationIdentificationNumber = "1234567";
    String componentCode = "011-022";
    Transfusion expectedTranfusion =  aTransfusion()
        .withComponent(aComponent()
            .withComponentCode(componentCode)
            .withDonation(aDonation()
                .withDonationIdentificationNumber(donationIdentificationNumber)
                .build()).buildAndPersist(entityManager))
        .withPatient(aPatient() 
           .withName1("Name 1")
           .withName2("Name 2")
           .buildAndPersist(entityManager))
       .withReceivedFrom(aUsageSite()
           .withName("Tranfusion site")
           .buildAndPersist(entityManager))
       .buildAndPersist(entityManager);

        // Excluded by donationIdentificationNumber    
        aTransfusion()
            .withComponent(aComponent()
                .withComponentCode(componentCode)
                .withDonation(aDonation()
                    .withDonationIdentificationNumber("2345734")
                    .build()).buildAndPersist(entityManager))
            .withPatient(aPatient() 
               .withName1("Name3")
               .withName2("Name4")
               .buildAndPersist(entityManager))
           .withReceivedFrom(aUsageSite()
               .withName("Transfusion site")
               .buildAndPersist(entityManager))
           .buildAndPersist(entityManager);

        // Excluded by component code    
        aTransfusion()
            .withComponent(aComponent()
                .withComponentCode("2011")
                .withDonation(aDonation()
                    .withDonationIdentificationNumber("765754")
                    .build()).buildAndPersist(entityManager))
            .withPatient(aPatient() 
               .withName1("Name5")
               .withName2("Name6")
               .build())
           .withReceivedFrom(aUsageSite()
               .withName("Transfusion site")
               .buildAndPersist(entityManager))
           .buildAndPersist(entityManager);

    Transfusion returnedTransfusion = transfusionRepository
        .findTransfusionByDINAndComponentCode(donationIdentificationNumber, componentCode);

    assertThat(returnedTransfusion, hasSameStateAsTransfusion(expectedTranfusion));
  }

  @Test
  public void testFindTransfusionByDINAndCodeWithFlagCharacters_shouldReturnCorrectFields() {

    String  donationIdentificationNumber = "1234567";
    String flagCharacter = "BR";
    String componentCode = "011-022";
    Transfusion expectedTranfusion =  aTransfusion()   
        .withComponent(aComponent()
            .withComponentCode(componentCode)
            .withDonation(aDonation()
                .withDonationIdentificationNumber(donationIdentificationNumber)
                .withFlagCharacters("BR")
                .build()).buildAndPersist(entityManager))
        .withPatient(aPatient() 
           .withName1("Name 1")
           .withName2("Name 2")
           .build())
       .withReceivedFrom(aUsageSite()
           .withName("Tranfusion site")
           .buildAndPersist(entityManager))
       .buildAndPersist(entityManager);

    Transfusion returnedTransfusion = transfusionRepository
        .findTransfusionByDINAndComponentCode(donationIdentificationNumber + flagCharacter, componentCode);

    assertThat(returnedTransfusion, hasSameStateAsTransfusion(expectedTranfusion));
  }

  @Test
  public void testFindTransfusions_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Location receiveFrom = aUsageSite().buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    Component component = aComponent().withComponentType(componentType).buildAndPersist(entityManager);

    Transfusion transfusion1 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(startDate)
        .withReceivedFrom(receiveFrom)
        .withComponent(component)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);
    Transfusion transfusion2 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(component)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    // Excluded for TransfusionOutcome
    aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(component)
        .withTransfusionOutcome(TransfusionOutcome.NOT_TRANSFUSED)
        .buildAndPersist(entityManager);

    //Excluded for dateTransfused
    aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new DateTime().plusDays(30).toDate())
        .withReceivedFrom(receiveFrom)
        .withComponent(component)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    //Excluded for componentType
    aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(aComponentType().build())
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    //Excluded for site
    aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(aUsageSite().buildAndPersist(entityManager))
        .withComponent(component)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    //Excluded for isDeleted
    aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(component)
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .thatIsDeleted()
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusions(
        componentType.getId(), receiveFrom.getId(), TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, startDate, endDate);

    // Verify
    assertThat(returnedTransfusions.size(), is(2));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion1)));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion2)));
  }

  @Test
  public void testFindTransfusionsWithNullOutcome_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Location receiveFrom = aUsageSite().buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    Component component = aComponent().withComponentType(componentType).buildAndPersist(entityManager);

    Transfusion transfusion1 = aTransfusion()
         .withPatient(aPatient()
             .withName1("Name 1")
             .withName2("Name 1")
             .build())
         .withDateTransfused(startDate)
         .withReceivedFrom(receiveFrom)
         .withComponent(component)
         .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
         .buildAndPersist(entityManager);
    Transfusion transfusion2 = aTransfusion()
         .withPatient(aPatient()
             .withName1("Name 1")
             .withName2("Name 1")
             .build())
         .withDateTransfused(new Date())
         .withReceivedFrom(receiveFrom)
         .withComponent(component)
         .withTransfusionOutcome(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
         .buildAndPersist(entityManager);

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusions(
        componentType.getId(), receiveFrom.getId(), null, startDate, endDate);

    // Verify
    assertThat(returnedTransfusions.size(), is(2));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion1)));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion2)));
  }

  @Test
  public void testFindTransfusionsWithNullComponentTypeId_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Location receiveFrom = aUsageSite().buildAndPersist(entityManager);

    Transfusion transfusion1 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(startDate)
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(aComponentType().build())
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);
    Transfusion transfusion2 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(aComponentType().build())
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusions(
        null, receiveFrom.getId(), TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, startDate, endDate);

    // Verify
    assertThat(returnedTransfusions.size(), is(2));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion1)));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion2)));
  }

  @Test
  public void testFindTransfusionsWithNullReceivedFromId_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);

    Transfusion transfusion1 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(startDate)
        .withReceivedFrom(aUsageSite().buildAndPersist(entityManager))
        .withComponent(aComponent()
            .withComponentType(componentType)
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);
    Transfusion transfusion2 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(aUsageSite().buildAndPersist(entityManager))
        .withComponent(aComponent()
            .withComponentType(componentType)
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusions(
        componentType.getId(), null, TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, startDate, endDate);

    // Verify
    assertThat(returnedTransfusions.size(), is(2));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion1)));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion2)));
  }

  @Test
  public void testFindTransfusionsWithNullStartDate_shouldReturnCorrectRecords() {

    //Set up
    Date endDate = new DateTime().plusDays(2).toDate();
    Location receiveFrom = aUsageSite().buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);

    Transfusion transfusion1 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new DateTime().minusDays(7).toDate())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(componentType)
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);
    Transfusion transfusion2 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(componentType)
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusions(
        componentType.getId(), receiveFrom.getId(), TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, null, endDate);

    // Verify
    assertThat(returnedTransfusions.size(), is(2));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion1)));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion2)));
  }

  @Test
  public void testFindTransfusionsWithNullEndDate_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Location receiveFrom = aUsageSite().buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);

    Transfusion transfusion1 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(startDate)
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(componentType)
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);
    Transfusion transfusion2 = aTransfusion()
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(componentType)
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusions(
        componentType.getId(), receiveFrom.getId(), TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, startDate, null);

    // Verify
    assertThat(returnedTransfusions.size(), is(2));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion1)));
    assertThat(returnedTransfusions, hasItem(hasSameStateAsTransfusion(transfusion2)));
  }
}