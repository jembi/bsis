package org.jembi.bsis.repository.constant;

public class TestBatchNamedQueryConstants {
  public static final String NAME_FIND_TEST_BATCHES_BY_STATUSES_PERIOD_AND_LOCATION =
      "TestBatch.findTestBatchesByStatusesPeriodAndLocation";

  public static final String QUERY_FIND_TEST_BATCHES_BY_STATUSES_PERIOD_AND_LOCATION = 
      "SELECT t FROM TestBatch t "
      + "INNER JOIN t.location l "
      + "WHERE t.isDeleted = :deleted "
      + "AND (:includeAllStatuses is true OR t.status IN (:statuses)) "
      + "AND (:includeStartDateCheck is false OR t.testBatchDate >= :startDate) "
      + "AND (:includeEndDateCheck is false OR t.testBatchDate <= :endDate) "
      + "AND (:includeAllLocations is true OR l.id = :locationId)";
}
