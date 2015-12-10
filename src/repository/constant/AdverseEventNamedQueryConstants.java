package repository.constant;

public class AdverseEventNamedQueryConstants {

  public static final String NAME_COUNT_ADVERSE_EVENTS_FOR_DONOR =
          "AdverseEvent.countAdverseEventsForDonor";
  public static final String QUERY_COUNT_ADVERSE_EVENTS_FOR_DONOR =
          "SELECT COUNT(d) " +
                  "FROM Donation d " +
                  "WHERE d.adverseEvent IS NOT NULL " +
                  "AND d.isDeleted = :deleted " +
                  "AND d.donor = :donor ";

}
