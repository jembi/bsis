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

  public static final String NAME_FIND_DONOR_BY_DONOR_NUMBER = "Donor.findDonorByDonorNumber";
  public static final String QUERY_FIND_DONOR_BY_DONOR_NUMBER =
      "SELECT d " +
      "FROM Donor d " +
      "WHERE d.donorNumber = :donorNumber ";

  public static final String NAME_FIND_DONOR_BY_DONATION_IDENTIFICATION_NUMBER =
      "Donor.findDonorByDonationIdentificationNumber";
  public static final String QUERY_FIND_DONOR_BY_DONATION_IDENTIFICATION_NUMBER =
      "SELECT d.donor " +
      "FROM Donation d " +
      "WHERE d.donationIdentificationNumber = :donationIdentificationNumber";

  public static final String NAME_COUNT_DONOR_WITH_ID = "Donor.countDonorWithId";
  public static final String QUERY_COUNT_DONOR_WITH_ID =
      "SELECT count(*) FROM Donor d WHERE d.id=:id AND d.isDeleted = false";
}
