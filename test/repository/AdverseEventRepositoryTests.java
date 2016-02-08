package repository;

import static helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import model.donor.Donor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.ContextDependentTestSuite;

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
