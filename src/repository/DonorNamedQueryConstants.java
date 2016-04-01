package repository;

public class DonorNamedQueryConstants {

  public static final String NAME_GET_ALL_DUPLICATE_DONORS = "Donor.getAllDuplicateDonors";
  public static final String QUERY_GET_ALL_DUPLICATE_DONORS =
      "SELECT NEW dto.DuplicateDonorDTO(MIN(d.donorNumber), d.firstName, d.lastName, d.birthDate, d.gender, COUNT(d)) " +
      "FROM Donor d " +
      "WHERE d.donorStatus <> 'MERGED' AND d.isDeleted <> 1 " +
      "GROUP BY d.firstName, d.lastName, d.birthDate, d.gender " +
      "HAVING COUNT(d.firstName) > 1";
  
  public static final String NAME_GET_DUPLICATE_DONORS = "Donor.getDuplicateDonors";
  public static final String QUERY_GET_DUPLICATE_DONORS =
      "SELECT d FROM Donor d " +
      "WHERE d.donorStatus <> 'MERGED' AND d.isDeleted <> 1 " +
      "AND d.firstName = :firstName AND d.lastName = :lastName " +
      "AND d.birthDate = :birthDate AND d.gender = :gender ";
  
  public static final String NAME_MOBILE_CLINIC_LOOKUP = "Donor.mobileClinicLookup";
  public static final String QUERY_MOBILE_CLINIC_LOOKUP = 
      "SELECT NEW dto.MobileClinicDonorDTO(d.id, d.donorNumber, d.firstName, d.lastName, d.gender, d.bloodAbo, d.bloodRh,"
      + "d.donorStatus, d.birthDate, d.venue, d.isDeleted) FROM Donor d " +
          "WHERE d.venue.id = :venueId " +
          "AND d.isDeleted = :isDeleted " +
          "AND d.donorStatus NOT IN :excludedStatuses " +
          "ORDER BY d.lastName asc, d.firstName asc";
}
