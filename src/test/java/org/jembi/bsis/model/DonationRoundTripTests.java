package org.jembi.bsis.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;

import javax.persistence.PersistenceException;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.jembi.bsis.util.RandomTestDate;
import org.junit.Test;

public class DonationRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistWithDefaultsFromBuilder_shouldSave() {
    aDonation().buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistDonationWithNullPackType_shouldThrow() {
    aDonation().withPackType(null).buildAndPersist(entityManager);
  }

  @Test
  public void testPersistDonationWithTestBatch_shouldSave() {
    Location testingLocation = aTestingSite().buildAndPersist(entityManager);
    TestBatch testBatch = aTestBatch()
        .withLocation(testingLocation)
        .withTestBatchDate(new RandomTestDate())
        .buildAndPersist(entityManager);
    Donation donation = aDonation()
        .withTestBatch(testBatch)
        .buildAndPersist(entityManager);

    Donation savedDonation = entityManager.find(Donation.class, donation.getId());

    assertThat(savedDonation.getTestBatch(), hasSameStateAsTestBatch(testBatch));
  }
}
