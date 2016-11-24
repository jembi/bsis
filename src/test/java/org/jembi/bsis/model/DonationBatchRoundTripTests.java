package org.jembi.bsis.model;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;

public class DonationBatchRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistValidDonationBatch() {
    aDonationBatch()
      .withVenue(aLocation().build())
      .withDonationBatchDate(new Date())
      .buildAndPersist(entityManager);
  }

  @Test (expected = ConstraintViolationException.class)
  public void testPersistDonationBatchDateWithNoDonationBatchDate_ShouldThrow()  {
    aDonationBatch().withDonationBatchDate(null).buildAndPersist(entityManager);
  }

  @Test (expected = ConstraintViolationException.class)
  public void testPersistDonationBatchWithNoLocation_ShouldThrow()  {
    aDonationBatch().withVenue(null).buildAndPersist(entityManager);
  }
}
