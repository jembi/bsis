package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.DonorsAdverseEventsDTOBuilder.aDonorsAdverseEventsDTO;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.matchers.DonorsAdverseEventsDTOMatcher.hasSameStateAsDonorsAdverseEventsDTO;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.DonorsAdverseEventsDTO;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.joda.time.DateTime;
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

  @Test
  public void testCountAdverseEventsForAllVenues_shouldReturnCorrectDTOs() {
    Location venueB = aVenue().withName("b venue").build();
    Location venueA = aVenue().withName("a venue").build();
    Location venueC = aVenue().withName("c venue").thatIsDeleted().build();
    AdverseEventType adverseEventTypeSomething = anAdverseEventType().withName("something").build();
    AdverseEventType adverseEventTypeSomethingReallyBad = anAdverseEventType().withName("something really bad").build();
    AdverseEventType adverseEventTypeDeleted = anAdverseEventType().withName("deleted").thatIsDeleted().build();
    Date startDate = new DateTime().minusDays(10).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();

    // In the date range
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(5).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomething).build())
      .buildAndPersist(entityManager);
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(6).toDate())
      .withVenue(venueA)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomething).build())
      .buildAndPersist(entityManager);
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(4).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(8).toDate())
      .withVenue(venueA)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(3).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation() // deleted donation
      .thatIsDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(4).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation() // has no adverse event
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(2).toDate())
      .withVenue(venueB)
      .buildAndPersist(entityManager);
    aDonation() // deleted adverse event type
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(4).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeDeleted).build())
      .buildAndPersist(entityManager);
    aDonation() // deleted venue
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(3).toDate())
      .withVenue(venueC)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);

    // Not in the date range
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(23).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomething).build())
      .buildAndPersist(entityManager);
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(69).toDate())
      .withVenue(venueA)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().toDate())
      .withVenue(venueA)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomething).build())
      .buildAndPersist(entityManager);

    // Expected results
    DonorsAdverseEventsDTO dtoAVenueSomething = aDonorsAdverseEventsDTO()
        .withVenue(venueA)
        .withAdverseEventType(adverseEventTypeSomething)
        .withCount(1L)
        .build();
    DonorsAdverseEventsDTO dtoAVenueSomethingReallyBad = aDonorsAdverseEventsDTO()
        .withVenue(venueA)
        .withAdverseEventType(adverseEventTypeSomethingReallyBad)
        .withCount(1L)
        .build();
    DonorsAdverseEventsDTO dtoBVenueSomething = aDonorsAdverseEventsDTO()
        .withVenue(venueB)
        .withAdverseEventType(adverseEventTypeSomething)
        .withCount(1L)
        .build();
    DonorsAdverseEventsDTO dtoBVenueSomethingReallyBad = aDonorsAdverseEventsDTO()
        .withVenue(venueB)
        .withAdverseEventType(adverseEventTypeSomethingReallyBad)
        .withCount(2L)
        .build();

    List<DonorsAdverseEventsDTO> dtos = adverseEventRepository.countAdverseEvents(null, startDate, endDate);

    // verify results
    assertThat("Correct number of DTOs are returned", dtos.size(), is(4));
    assertThat("Correct DTO returned", dtos.get(0), hasSameStateAsDonorsAdverseEventsDTO(dtoBVenueSomething));
    assertThat("Correct DTO returned", dtos.get(1), hasSameStateAsDonorsAdverseEventsDTO(dtoBVenueSomethingReallyBad));
    assertThat("Correct DTO returned", dtos.get(2), hasSameStateAsDonorsAdverseEventsDTO(dtoAVenueSomethingReallyBad));
    assertThat("Correct DTO returned", dtos.get(3), hasSameStateAsDonorsAdverseEventsDTO(dtoAVenueSomething));
  }

  @Test
  public void testCountAdverseEventsForOneVenues_shouldReturnCorrectDTOs() {
    Location venueB = aVenue().withName("b venue").build();
    Location venueA = aVenue().withName("a venue").build();
    AdverseEventType adverseEventTypeSomething = anAdverseEventType().withName("something").build();
    AdverseEventType adverseEventTypeSomethingReallyBad = anAdverseEventType().withName("something really bad").build();
    Date startDate = new DateTime().minusDays(10).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();

    // In the date range
    aDonation() // expected
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(5).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomething).build())
      .buildAndPersist(entityManager);
    aDonation() // other venue
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(6).toDate())
      .withVenue(venueA)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomething).build())
      .buildAndPersist(entityManager);
    aDonation() // expected
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(4).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation() // other venue
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(8).toDate())
      .withVenue(venueA)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation() // expected
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(3).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation() // deleted donation
      .thatIsDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(4).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomethingReallyBad).build())
      .buildAndPersist(entityManager);
    aDonation() // has no adverse event
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(2).toDate())
      .withVenue(venueB)
      .buildAndPersist(entityManager);

    // Not in the date range
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().minusDays(23).toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomething).build())
      .buildAndPersist(entityManager);
    aDonation()
      .thatIsNotDeleted()
      .withDonor(aDonor().build())
      .withDonationDate(new DateTime().toDate())
      .withVenue(venueB)
      .withAdverseEvent(anAdverseEvent().withType(adverseEventTypeSomething).build())
      .buildAndPersist(entityManager);

    // Expected results
    DonorsAdverseEventsDTO dtoBVenueSomething = aDonorsAdverseEventsDTO()
        .withVenue(venueB)
        .withAdverseEventType(adverseEventTypeSomething)
        .withCount(1L)
        .build();
    DonorsAdverseEventsDTO dtoBVenueSomethingReallyBad = aDonorsAdverseEventsDTO()
        .withVenue(venueB)
        .withAdverseEventType(adverseEventTypeSomethingReallyBad)
        .withCount(2L)
        .build();

    List<DonorsAdverseEventsDTO> dtos = adverseEventRepository.countAdverseEvents(venueB.getId(), startDate, endDate);

    // verify results
    assertThat("Correct number of DTOs are returned", dtos.size(), is(2));
    assertThat("Correct DTO returned", dtos.get(0), hasSameStateAsDonorsAdverseEventsDTO(dtoBVenueSomething));
    assertThat("Correct DTO returned", dtos.get(1), hasSameStateAsDonorsAdverseEventsDTO(dtoBVenueSomethingReallyBad));
  }
}
