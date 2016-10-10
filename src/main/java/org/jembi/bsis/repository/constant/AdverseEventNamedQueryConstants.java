package org.jembi.bsis.repository.constant;

public class AdverseEventNamedQueryConstants {

  public static final String NAME_COUNT_ADVERSE_EVENTS_FOR_DONOR =
      "AdverseEvent.countAdverseEventsForDonor";
  public static final String QUERY_COUNT_ADVERSE_EVENTS_FOR_DONOR =
      "SELECT COUNT(d) " +
          "FROM Donation d " +
          "WHERE d.adverseEvent IS NOT NULL " +
          "AND d.isDeleted = :deleted " +
          "AND d.donor = :donor ";

  public static final String NAME_COUNT_ADVERSE_EVENTS =
      "AdverseEvent.countAdverseEvents";
  public static final String QUERY_COUNT_ADVERSE_EVENTS =
      "SELECT NEW org.jembi.bsis.dto.DonorsAdverseEventsDTO(d.adverseEvent.type, d.venue, COUNT(d.id)) " +
      "FROM Donation d " +
      "WHERE d.adverseEvent IS NOT NULL " +
      "AND d.isDeleted = :deleted " +
      "AND d.donationDate BETWEEN :startDate AND :endDate " +
      "AND (d.venue.id = :venueId OR :venueId = NULL) " +
      "GROUP BY d.adverseEvent.type,  d.venue " +
      "ORDER BY d.venue";
}
