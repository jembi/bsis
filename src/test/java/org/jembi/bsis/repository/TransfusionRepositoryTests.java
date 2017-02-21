package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.TransfusionBuilder.aTransfusion;

import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
        .withDonationIdentificationNumber("1234567")
        .withPatient(aPatient()
            .withName1("Name 1")
            .withName2("Name 1")
            .buildAndPersist(entityManager))
        .withReceivedFrom(aUsageSite()
            .withName("Received From")
            .buildAndPersist(entityManager))
        .withComponent(aComponent()
            .buildAndPersist(entityManager))
        .build();
    
    Transfusion savedTransfusion = transfusionRepository.update(transfusion);
    assertThat(savedTransfusion.getPatient(), is(transfusion.getPatient()));
    assertThat(savedTransfusion.getId(), is(IsNull.notNullValue()));
    assertThat(savedTransfusion.getPatient().getId(), is(IsNull.notNullValue()));
    assertThat(savedTransfusion.getReceivedFrom().getId(), is(IsNull.notNullValue()));
    
  }
}