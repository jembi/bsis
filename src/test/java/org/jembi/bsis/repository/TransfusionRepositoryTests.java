package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.TransfusionSummaryDTOBuilder.aTransfusionSummaryDTO;
import static org.jembi.bsis.helpers.matchers.TransfusionSummaryDTOMatcher.hasSameStateAsTransfusionSummaryDTO;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.jembi.bsis.dto.TransfusionSummaryDTO;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransfusionRepositoryTests extends SecurityContextDependentTestSuite {
  
  @Autowired
  private TransfusionRepository transfusionRepository;
  
  @Test
  public void testSaveTransfusion_shouldAlsoPersistPatient() {
    
    Transfusion transfusion = aTransfusion()
        .withDonationIdentificationNumber("1234567")
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
  public void testFindTransfusionSummaryRecordedForUsageSiteForPeriod_shouldReturnRightDtos() {
    
    Date startDate = new DateTime().minusDays(60).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();
    TransfusionReactionType transfusionReactionType = aTransfusionReactionType()
        .buildAndPersist(entityManager);
    
    aTransfusion()
        .withDonationIdentificationNumber("1234567")
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
    
    aTransfusion()
    .withDonationIdentificationNumber("1234598")
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
    
    
    
    Long receivedFromId = null;
    
    List<TransfusionSummaryDTO> transfusionSummaryDTOs = transfusionRepository.findTransfusionSummaryRecordedForUsageSiteForPeriod(receivedFromId, startDate, endDate);
    
    // check that the transfusion summary count returned is equal to persisted transfusions count
    assertThat(transfusionSummaryDTOs.size(), is(2));
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
        .withDonationIdentificationNumber("1234567")
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
    .withDonationIdentificationNumber("1234581")
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
  public void testFindTransfusionSummaryRecordedForUsageSiteForPeriod_shouldReturnDTOSWithInPeriodRange() {
    
    Date startDate = new DateTime().minusDays(60).toDate();
    Date endDate = new DateTime().plusDays(4).toDate();
    Location receivedFrom = aUsageSite()
        .withName("Harare")
        .buildAndPersist(entityManager);
    
    Transfusion expectedTransfusion1 = aTransfusion()
        .withDonationIdentificationNumber("1234567")
        .withDateTransfused(new Date())
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .build())
        .withReceivedFrom(receivedFrom)
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
        .withTransfusionReactionType(aTransfusionReactionType().withName("Reaction Name1").buildAndPersist(entityManager))
        .buildAndPersist(entityManager);
    
    Transfusion expectedTransfusion2 = aTransfusion()
      .withDonationIdentificationNumber("1234581")
      .withDateTransfused(new Date())
      .withPatient(aPatient()
          .withName1("Name 2")
          .withName2("Name 2")
          .build())
      .withReceivedFrom(receivedFrom)
      .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
      .withTransfusionReactionType(null)
      .withComponent(aComponent()
          .buildAndPersist(entityManager))
      .buildAndPersist(entityManager);
    
    // exclude by transfusion date
    aTransfusion()
      .withDonationIdentificationNumber("1234576")
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
    
    List<TransfusionSummaryDTO> expectedTransfusionSummaryDTOs = new ArrayList<>();

    expectedTransfusionSummaryDTOs.add(aTransfusionSummaryDTO()
        .withCount(1)
        .withTransfusionOutcome(expectedTransfusion2.getTransfusionOutcome())
        .withTransfusionReactionType(expectedTransfusion2.getTransfusionReactionType())
        .withTransfusionSite(expectedTransfusion2.getReceivedFrom())
        .build());  
    expectedTransfusionSummaryDTOs.add(aTransfusionSummaryDTO()
        .withCount(1)
        .withTransfusionOutcome(expectedTransfusion1.getTransfusionOutcome())
        .withTransfusionReactionType(expectedTransfusion1.getTransfusionReactionType())
        .withTransfusionSite(expectedTransfusion1.getReceivedFrom())
        .build());  
    
    List<TransfusionSummaryDTO> returnedTransfusionSummaryDTOs = transfusionRepository.findTransfusionSummaryRecordedForUsageSiteForPeriod(receivedFrom.getId(), startDate, endDate);
    
    // check that the transfusion summary count returned is for transfusions within range
    assertThat(returnedTransfusionSummaryDTOs.size(), is(2));
    // check that returned DTOs are same as expected
    assertThat(returnedTransfusionSummaryDTOs.get(0), hasSameStateAsTransfusionSummaryDTO(expectedTransfusionSummaryDTOs.get(0)));
    assertThat(returnedTransfusionSummaryDTOs.get(1), hasSameStateAsTransfusionSummaryDTO(expectedTransfusionSummaryDTOs.get(1)));
  }
}