package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.DonorDeferralBuilder.aDonorDeferral;

import javax.persistence.PersistenceException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class DonorDeferralRoundTripTests extends ContextDependentTestSuite {
  
  @Test
  public void testPersistDeferralWithValidDeferral() {
    aDonorDeferral().buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistDeferralWithNoDeferralDate_shouldThrow() {
    aDonorDeferral().withDeferralDate(null).buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistDeferralWithNoDeferredDonor_shouldThrow() {
    aDonorDeferral().withDeferredDonor(null).buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistDeferralWithNoVenue_shouldThrow() {
    aDonorDeferral().withVenue(null).buildAndPersist(entityManager);
  }

}
