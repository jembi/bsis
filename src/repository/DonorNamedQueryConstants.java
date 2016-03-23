package repository;

public class DonorNamedQueryConstants {

  public static final String NAME_GET_DUPLICATE_DONORS = "Donor.getDuplicateDonors";
  public static final String QUERY_GET_DUPLICATE_DONORS =
      "SELECT NEW valueobject.DuplicateDonorValueObject(MIN(d.donorNumber), d.firstName, d.lastName, d.birthDate, d.gender, COUNT(d)) " +
      "FROM Donor d " +
      "WHERE d.donorStatus <> 'MERGED' and d.isDeleted <> 1 " +
      "GROUP BY d.firstName, d.lastName, d.birthDate, d.gender " +
      "HAVING COUNT(d.firstName) > 1";
}
