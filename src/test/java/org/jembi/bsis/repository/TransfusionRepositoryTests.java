package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;

import java.util.Date;
import java.util.List;
import java.util.Arrays;

import org.hamcrest.core.IsNull;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
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
  public void testFindTransfusionsByDINAndCodeWithoutFlagCharacters_shouldReturnCorrectFields() {
    
    String  donationIdentificationNumber = "1234567";
    String componentCode = "011-022";
    List<Transfusion> expectedTranfusions = Arrays.asList(
        aTransfusion()
            .withDonationIdentificationNumber(donationIdentificationNumber)
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
           .buildAndPersist(entityManager),
       
        // Excluded by donationIdentificationNumber    
        aTransfusion()
            .withDonationIdentificationNumber("6543219")
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
           .buildAndPersist(entityManager),
          
        // Excluded by component code    
        aTransfusion()
            .withDonationIdentificationNumber("4242424")
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
           .buildAndPersist(entityManager)
    );
    
    List<Transfusion> returnedTransfusions = transfusionRepository
        .findTransfusionsByDINAndComponentCode(donationIdentificationNumber, componentCode);
    
    assertThat(returnedTransfusions.size(), is(1));
    assertThat(returnedTransfusions.get(0), hasSameStateAsTransfusion(expectedTranfusions.get(0)));
  }
  
  @Test
  public void testFindTransfusionsByDINAndCodeWithFlagCharacters_shouldReturnCorrectFields() {
    
    String  donationIdentificationNumber = "1234567BE";
    String componentCode = "011-022";
    List<Transfusion> expectedTranfusions = Arrays.asList(
        aTransfusion()
            .withDonationIdentificationNumber(donationIdentificationNumber)
            .withComponent(aComponent()
                .withComponentCode(componentCode)
                .withDonation(aDonation()
                    .withDonationIdentificationNumber(donationIdentificationNumber)
                    .withFlagCharacters("BE")
                    .build()).buildAndPersist(entityManager))
            .withPatient(aPatient() 
               .withName1("Name 1")
               .withName2("Name 2")
               .build())
           .withReceivedFrom(aUsageSite()
               .withName("Tranfusion site")
               .buildAndPersist(entityManager))
           .buildAndPersist(entityManager)
    );
    
    List<Transfusion> returnedTransfusions = transfusionRepository
        .findTransfusionsByDINAndComponentCode(donationIdentificationNumber, componentCode);
    
    assertThat(returnedTransfusions.size(), is(expectedTranfusions.size()));
    assertThat(returnedTransfusions.get(0), hasSameStateAsTransfusion(expectedTranfusions.get(0)));
  }

  @Test
  public void testfindTransfusionByComponentTypeAndSiteAndOutcome_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Location receiveFrom = aUsageSite().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);

    List<Transfusion> expectedTransfusions = Arrays.asList(
        aTransfusion()
            .withDateTransfused(startDate)
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .build(),
        aTransfusion()
            .withDateTransfused(new Date())
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .buildAndPersist(entityManager)
        );

    // Excluded for TransfusionOutcome
    aTransfusion()
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(componentType)
            .build())
        .withTransfusionOutcome(TransfusionOutcome.NOT_TRANSFUSED)
        .buildAndPersist(entityManager);

    //Excluded for dateTransfused
    aTransfusion()
        .withDateTransfused(new DateTime().plusDays(30).toDate())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(componentType)
            .build())
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    //Excluded for componentType
    aTransfusion()
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(aComponentType().build())
            .build())
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    //Excluded for site
    aTransfusion()
        .withDateTransfused(new Date())
        .withReceivedFrom(aUsageSite().build())
        .withComponent(aComponent()
            .withComponentType(componentType)
            .build())
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .buildAndPersist(entityManager);

    //Excluded for isDeleted
    aTransfusion()
        .withDateTransfused(new Date())
        .withReceivedFrom(receiveFrom)
        .withComponent(aComponent()
            .withComponentType(componentType)
            .build())
        .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
        .thatIsDeleted()
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(
        componentType.getId(), receiveFrom.getId(), TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, startDate, endDate);

    // Verify
    assertThat(returnedTransfusions, is(expectedTransfusions));
  }

  @Test
  public void testfindTransfusionByComponentTypeAndSiteAndOutcomeWithNullOutcome_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Location receiveFrom = aUsageSite().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);

    List<Transfusion> expectedTransfusions = Arrays.asList(
        aTransfusion()
            .withDateTransfused(startDate)
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .build(),
        aTransfusion()
            .withDateTransfused(new Date())
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
            .buildAndPersist(entityManager)
    );

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(
        componentType.getId(), receiveFrom.getId(), null, startDate, endDate);

    // Verify
    assertThat(returnedTransfusions, is(expectedTransfusions));
  }

  @Test
  public void testfindTransfusionByComponentTypeAndSiteAndOutcomeWithNullComponentTypeId_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Location receiveFrom = aUsageSite().build();

    List<Transfusion> expectedTransfusions = Arrays.asList(
        aTransfusion()
            .withDateTransfused(startDate)
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(aComponentType().build())
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .build(),
        aTransfusion()
            .withDateTransfused(new Date())
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(aComponentType().build())
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .buildAndPersist(entityManager)
    );

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(
        null, receiveFrom.getId(), TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, startDate, endDate);

    // Verify
    assertThat(returnedTransfusions, is(expectedTransfusions));
  }

  @Test
  public void testfindTransfusionByComponentTypeAndSiteAndOutcomeWithNullReceivedFromId_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);

    List<Transfusion> expectedTransfusions = Arrays.asList(
        aTransfusion()
            .withDateTransfused(startDate)
            .withReceivedFrom(aUsageSite().build())
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .build(),
        aTransfusion()
            .withDateTransfused(new Date())
            .withReceivedFrom(aUsageSite().build())
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .buildAndPersist(entityManager)
    );

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(
        componentType.getId(), null, TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, startDate, endDate);

    // Verify
    assertThat(returnedTransfusions, is(expectedTransfusions));
  }

  @Test
  public void testfindTransfusionByComponentTypeAndSiteAndOutcomeWithNullStartDate_shouldReturnCorrectRecords() {

    //Set up
    Date endDate = new DateTime().plusDays(2).toDate();
    Location receiveFrom = aUsageSite().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);

    List<Transfusion> expectedTransfusions = Arrays.asList(
        aTransfusion()
            .withDateTransfused(new DateTime().minusDays(7).toDate())
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .build(),
        aTransfusion()
            .withDateTransfused(new Date())
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .buildAndPersist(entityManager)
    );

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(
        componentType.getId(), receiveFrom.getId(), TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, null, endDate);

    // Verify
    assertThat(returnedTransfusions, is(expectedTransfusions));
  }

  @Test
  public void testfindTransfusionByComponentTypeAndSiteAndOutcomeWithNullEndDate_shouldReturnCorrectRecords() {

    //Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Location receiveFrom = aUsageSite().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);

    List<Transfusion> expectedTransfusions = Arrays.asList(
        aTransfusion()
            .withDateTransfused(startDate)
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .build(),
        aTransfusion()
            .withDateTransfused(new Date())
            .withReceivedFrom(receiveFrom)
            .withComponent(aComponent()
                .withComponentType(componentType)
                .build())
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .buildAndPersist(entityManager)
    );

    // Exercise SUT
    List<Transfusion> returnedTransfusions = transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(
        componentType.getId(), receiveFrom.getId(), TransfusionOutcome.TRANSFUSED_UNEVENTFULLY, startDate, null);

    // Verify
    assertThat(returnedTransfusions, is(expectedTransfusions));
  }
}