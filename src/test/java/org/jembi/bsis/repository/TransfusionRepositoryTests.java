package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.matchers.TransfusionMatcher.hasSameStateAsTransfusion;

import java.util.List;
import java.util.Arrays;

import org.hamcrest.core.IsNull;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
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
  public void testFindTransfusionsByDINAndCodeWithoutFlagCharacters_shouldReturnCorrectFields() {
    //Set up fixture
    String  donationIdentificationNumber = "1234567";
    String componentCode = "011-022";
    List<Transfusion> expectedTranfusions = Arrays.asList(
        aTransfusion()
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
  
  
}