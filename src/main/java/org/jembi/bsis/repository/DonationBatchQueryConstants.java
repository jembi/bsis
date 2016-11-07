package org.jembi.bsis.repository;

public class DonationBatchQueryConstants {

  public static final String NAME_COUNT_DONATION_BATCHES =
      "DonationBatch.countDonationBatches";
  public static final String QUERY_COUNT_DONATION_BATCHES =
      "SELECT COUNT(db) " +
          "FROM DonationBatch db " +
          "WHERE db.isClosed = :closed " +
          "AND db.isDeleted = :deleted ";
  
  public static final String NAME_VERIFY_DONATION_BATCH_WITH_ID_EXISTS =
      "DonationBatch.verifyDonationBatchExists";
  public static final String QUERY_VERIFY_DONATION_BATCH_WITH_ID_EXISTS =
      "SELECT count(db) > 0 " +
          "FROM DonationBatch db " +
          "WHERE db.id = :id " +
          "AND db.isDeleted = :deleted ";
  
  public static final String NAME_FIND_UNASSIGNED_DONATION_BATCHES_WITH_COMPONENTS =
      "DonationBatch.findUnassignedDonationBatchWithComponentBatch";
  public static final String QUERY_FIND_UNASSIGNED_DONATION_BATCHES_WITH_COMPONENTS =
      "SELECT DISTINCT db " +
          "FROM DonationBatch db " +
          "LEFT JOIN FETCH db.donations d " +
          "WHERE d.components is not empty " +
          "AND db.componentBatch is null " +
          "AND db.isDeleted = false ";
  public static final String NAME_FIND_COMPONENTBATCH_BY_DONATIONBATCH_ID =
      "DonationBatch.findComponentBatchByDonationbatchId";
  public static final String QUERY_FIND_COMPONENTBATCH_BY_DONATIONBATCH_ID =
       "SELECT db.componentBatch " +
           "FROM DonationBatch db " +
           "WHERE db.id = :donationBatchId ";
}