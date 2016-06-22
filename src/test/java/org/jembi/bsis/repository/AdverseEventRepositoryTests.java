package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;

import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.AdverseEventRepository;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AdverseEventRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private AdverseEventRepository adverseEventRepository;

  @Test
  public void testCountAdverseEventsForDonor_shouldReturnCorrectCount() {

    Donor donor = aDonor().build();

    // Expected
    aDonation()
        .thatIsNotDeleted()
        .withDonor(donor)
        .withAdverseEvent(anAdverseEvent().build())
        .buildAndPersist(entityManager);

    // Excluded by donor
    aDonation()
        .thatIsNotDeleted()
        .withDonor(aDonor().build())
        .withAdverseEvent(anAdverseEvent().build())
        .buildAndPersist(entityManager);

    // Excluded by no adverse event
    aDonation()
        .thatIsNotDeleted()
        .withDonor(donor)
        .buildAndPersist(entityManager);

    // Excluded by deleted flag
    aDonation()
        .thatIsDeleted()
        .withDonor(donor)
        .withAdverseEvent(anAdverseEvent().build())
        .buildAndPersist(entityManager);

    // Expected
    aDonation()
        .thatIsNotDeleted()
        .withDonor(donor)
        .withAdverseEvent(anAdverseEvent().build())
        .buildAndPersist(entityManager);

    int returnedCount = adverseEventRepository.countAdverseEventsForDonor(donor);

    assertThat(returnedCount, is(2));
  }

}
