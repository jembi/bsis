package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;

import java.util.Date;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.jembi.bsis.dto.TransfusionSummaryDTO;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.Transfusion;
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
  public void testSearchTransfusionsWithTransfusionSiteNULL_shouldReturnAllTransfusions() {
    
    aTransfusion()
        .withDonationIdentificationNumber("1234567")
        .withDateTransfused(new DateTime().minusDays(25).toDate())
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
    
    aTransfusion()
        .withDonationIdentificationNumber("1234568")
        .withDateTransfused(new DateTime().minusDays(10).toDate())
        .withPatient(aPatient()
            .withName1("Name 2")
            .withName2("Name 2")
            .build())
        .withReceivedFrom(aUsageSite()
            .withName("Received From2")
            .buildAndPersist(entityManager))
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .build();
    
    aTransfusion()
        .withDonationIdentificationNumber("1234569")
        .withDateTransfused(new DateTime().minusDays(15).toDate())
        .withPatient(aPatient()
            .withName1("Name 3")
            .withName2("Name 3")
            .build())
        .withReceivedFrom(aUsageSite()
            .withName("Received From3")
            .buildAndPersist(entityManager))
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .build();
    
    Location transfusionSite = null;
    Date startDate = new DateTime().minusDays(30).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();
    
    List<TransfusionSummaryDTO> transfusionSummaryDTOs = transfusionRepository.findTransfusionsRecorded(transfusionSite, startDate, endDate);
    
    // check that the transfusion summary count returned is equal to persisted transfusions count
    assertThat(transfusionSummaryDTOs.size(), is(3));
  }
}