package org.jembi.bsis.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;

import java.util.HashSet;
import java.util.Set;

import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.jembi.bsis.util.RandomTestDate;
import org.junit.Test;

public class TestBatchRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistTestBatchWithTwoDonations_shouldSave() {
    Location venueB = aVenue().withName("b venue").build();

    Location location = LocationBuilder.aTestingSite().build();

    Set<Donation> donations = new HashSet<Donation>();
    Donation donation1 = aDonation()
        .thatIsNotDeleted()
        .withDonor(aDonor().build())
        .withDonationDate(new RandomTestDate())
        .withVenue(venueB)
        .withAdverseEvent(null)
        .buildAndPersist(entityManager);

    donations.add(donation1);

    Donation donation2 = aDonation()
        .thatIsNotDeleted()
        .withDonor(aDonor().build())
        .withDonationDate(new RandomTestDate())
        .withVenue(venueB)
        .withAdverseEvent(null)
        .buildAndPersist(entityManager);

    donations.add(donation2);

    TestBatch testBatch = aTestBatch()
        .withLocation(location)
        .withDonations(donations)
        .withTestBatchDate(new RandomTestDate())
        .buildAndPersist(entityManager);

    TestBatch result = entityManager.find(TestBatch.class, testBatch.getId());

    Set<Donation> resultDonations = result.getDonations();

    assertThat(resultDonations, hasItem(donation1));
    assertThat(resultDonations, hasItem(donation2));
  }

}
