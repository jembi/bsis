package org.jembi.bsis.repository;

public class DonorNamedQueryConstants {

  public static final String NAME_GET_ALL_DUPLICATE_DONORS = "Donor.getAllDuplicateDonors";
  public static final String QUERY_GET_ALL_DUPLICATE_DONORS =
      "SELECT NEW org.jembi.bsis.dto.DuplicateDonorDTO(MIN(d.donorNumber), d.firstName, d.lastName, d.birthDate, d.gender, COUNT(d)) " +
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
      "SELECT NEW org.jembi.bsis.dto.MobileClinicDonorDTO(d.id, d.donorNumber, d.firstName, d.lastName, d.gender, d.bloodAbo, d.bloodRh,"
      + "d.donorStatus, d.birthDate, d.venue, d.isDeleted) FROM Donor d " +
          "WHERE d.venue.id = :venueId " +
          "AND d.isDeleted = :isDeleted " +
          "AND d.donorStatus NOT IN :excludedStatuses " +
          "ORDER BY d.lastName asc, d.firstName asc";

  public static final String NAME_FIND_DONOR_BY_DONOR_NUMBER = "Donor.findDonorByDonorNumber";
  public static final String QUERY_FIND_DONOR_BY_DONOR_NUMBER =
      "SELECT d " +
      "FROM Donor d " +
      "WHERE d.donorNumber = :donorNumber AND d.isDeleted = :isDeleted AND d.donorStatus NOT IN :excludedStatuses";

  public static final String NAME_FIND_DONOR_BY_DONATION_IDENTIFICATION_NUMBER =
      "Donor.findDonorByDonationIdentificationNumber";
  public static final String QUERY_FIND_DONOR_BY_DONATION_IDENTIFICATION_NUMBER =
      "SELECT d.donor " +
      "FROM Donation d " +
      "WHERE (d.donationIdentificationNumber = :donationIdentificationNumber " +
      "OR CONCAT(d.donationIdentificationNumber, d.flagCharacters) = :donationIdentificationNumber) " +
      "AND d.donor.isDeleted = :isDeleted " +
      "AND d.isDeleted = :isDonationDeleted " +
      "AND d.donor.donorStatus NOT IN :excludedStatuses";

  public static final String NAME_COUNT_DONOR_WITH_ID = "Donor.countDonorWithId";
  public static final String QUERY_COUNT_DONOR_WITH_ID =
      "SELECT count(*) FROM Donor d WHERE d.id=:id AND d.isDeleted = :isDeleted AND d.donorStatus NOT IN :excludedStatuses";
  
  public static final String NAME_FIND_DONORS_FOR_EXPORT =
      "Donor.findDonorsForExport";
  public static final String QUERY_FIND_DONORS_FOR_EXPORT =
      "SELECT NEW org.jembi.bsis.dto.DonorExportDTO(d.donorNumber, "
      + "d.modificationTracker.createdDate, d.modificationTracker.createdBy.username, "
      + "d.modificationTracker.lastUpdated, d.modificationTracker.lastUpdatedBy.username, "
      + "d.title, d.firstName, d.middleName, d.lastName, d.callingName, d.gender, "
      + "d.birthDate, d.preferredLanguage.preferredLanguage, d.venue.name, "
      + "d.bloodAbo, d.bloodRh, d.notes, "
      + "d.idType.idType, d.idNumber, "
      + "d.dateOfFirstDonation, d.dateOfLastDonation, d.dueToDonate, "
      + "d.contactMethodType.contactMethodType, d.contact.mobileNumber, d.contact.homeNumber, d.contact.workNumber, d.contact.email, "
      + "d.addressType.preferredAddressType, "
      + "d.address.homeAddressLine1, d.address.homeAddressLine2, d.address.homeAddressCity, d.address.homeAddressProvince, "
      + "d.address.homeAddressDistrict, d.address.homeAddressCountry, d.address.homeAddressState, d.address.homeAddressZipcode, "
      + "d.address.workAddressLine1, d.address.workAddressLine2, d.address.workAddressCity, d.address.workAddressProvince, "
      + "d.address.workAddressDistrict, d.address.workAddressCountry, d.address.workAddressState, d.address.workAddressZipcode, "
      + "d.address.postalAddressLine1, d.address.postalAddressLine2, d.address.postalAddressCity, d.address.postalAddressProvince, "
      + "d.address.postalAddressDistrict, d.address.postalAddressCountry, d.address.postalAddressState, d.address.postalAddressZipcode) "
      + "FROM Donor d "
      // ensures that even if the Donor doesn't have these entities, they will still be included in the result
      + "LEFT JOIN d.addressType "
      + "LEFT JOIN d.address "
      + "LEFT JOIN d.contactMethodType "
      + "LEFT JOIN d.contact "
      + "LEFT JOIN d.idType "
      + "LEFT JOIN d.preferredLanguage "
      + "WHERE d.isDeleted = :deleted "
      + "ORDER BY d.modificationTracker.createdDate ASC";

    public static final String NAME_FIND_MOBILE_CLINIC_DONORS_BY_VENUES =
      "Donor.findMobileClinicDonorsByVenues";
    public static final String QUERY_FIND_MOBILE_CLINIC_DONORS_BY_VENUES =
      "SELECT NEW org.jembi.bsis.dto.MobileClinicDonorDTO(d.id, d.donorNumber, d.firstName, " +
      "d.lastName, d.gender, d.bloodAbo, d.bloodRh, d.donorStatus, d.birthDate, d.venue, d.isDeleted) " +
      "FROM Donor d WHERE d.isDeleted = :isDeleted AND d.donorStatus NOT IN :excludedStatuses " +
      "AND (d.venue.id IN (:venueIds) OR :includeAllVenues = TRUE) " +
      "ORDER BY d.lastName asc, d.firstName asc";
}
