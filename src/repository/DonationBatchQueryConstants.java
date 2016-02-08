package repository;

public class DonationBatchQueryConstants {

  public static final String NAME_COUNT_DONATION_BATCHES =
      "DonationBatch.countDonationBatches";

  public static final String QUERY_COUNT_DONATION_BATCHES =
      "SELECT COUNT(db) " +
          "FROM DonationBatch db " +
          "WHERE db.isClosed = :closed " +
          "AND db.isDeleted = :deleted ";

}
