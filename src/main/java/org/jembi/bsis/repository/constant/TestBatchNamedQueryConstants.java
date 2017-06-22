package org.jembi.bsis.repository.constant;

public class TestBatchNamedQueryConstants {
  public static final String NAME_FIND_TEST_BATCHES_BY_STATUSES_PERIOD_AND_LOCATION =
      "TestBatch.findTestBatchesByStatusesPeriodAndLocation";

  public static final String QUERY_FIND_TEST_BATCHES_BY_STATUSES_PERIOD_AND_LOCATION = 
      "SELECT t FROM TestBatch t "
      + "INNER JOIN t.location l "
      + "WHERE t.isDeleted = :deleted "
      + "AND (:includeStatuses is false OR t.status IN (:statuses)) "
      + "AND (:includeStartDate is false OR t.modificationTracker.createdDate >= :startDate) "
      + "AND (:includeEndDate is false OR t.modificationTracker.createdDate <= :endDate) "
      + "AND (:includeLocation is false OR l.id = :locationId)";
}
