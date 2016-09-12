package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.dto.DonorExportDTO;

public class DonorExportDTOMatcher extends TypeSafeMatcher<DonorExportDTO> {

  private DonorExportDTO expected;

  public DonorExportDTOMatcher(DonorExportDTO expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donor with the following state:")
        .appendText("\nDonor number: ").appendValue(expected.getDonorNumber())
        .appendText("\nTitle: ").appendValue(expected.getTitle())
        .appendText("\nFirst name: ").appendValue(expected.getFirstName())
        .appendText("\nMiddle name: ").appendValue(expected.getMiddleName())
        .appendText("\nLast name: ").appendValue(expected.getLastName())
        .appendText("\nCalling name: ").appendValue(expected.getCallingName())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nBirthDate: ").appendValue(expected.getBirthDate().getTime())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nPreferred Language: ").appendValue(expected.getPreferredLanguage())
        .appendText("\nVenue: ").appendValue(expected.getVenue())
        .appendText("\nBlood ABO: ").appendValue(expected.getBloodABO())
        .appendText("\nBlood Rh: ").appendValue(expected.getBloodRh())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nID type: ").appendValue(expected.getIdType())
        .appendText("\nID number: ").appendValue(expected.getIdNumber())
        .appendText("\nDate of first donation: ").appendValue(expected.getDateOfFirstDonation().getTime())
        .appendText("\nDate of last donation: ").appendValue(expected.getDateOfLastDonation().getTime())
        .appendText("\nDue to donate: ").appendValue(expected.getDueToDonate().getTime())
        .appendText("\nContact method type: ").appendValue(expected.getContactMethodType())
        .appendText("\nMobile number: ").appendValue(expected.getMobileNumber())
        .appendText("\nHome number: ").appendValue(expected.getHomeNumber())
        .appendText("\nWork number: ").appendValue(expected.getWorkNumber())
        .appendText("\nEmail: ").appendValue(expected.getEmail())
        .appendText("\nPreferred address type: ").appendValue(expected.getPreferredAddressType())
        .appendText("\nHome address line 1: ").appendValue(expected.getHomeAddressLine1())
        .appendText("\nHome address line 2: ").appendValue(expected.getHomeAddressLine2())
        .appendText("\nHome address city: ").appendValue(expected.getHomeAddressCity())
        .appendText("\nHome address province: ").appendValue(expected.getHomeAddressProvince())
        .appendText("\nHome address district: ").appendValue(expected.getHomeAddressDistrict())
        .appendText("\nHome address state: ").appendValue(expected.getHomeAddressState())
        .appendText("\nHome address country: ").appendValue(expected.getHomeAddressCountry())
        .appendText("\nHome address zipcode: ").appendValue(expected.getHomeAddressZipcode())
        .appendText("\nWork address line 1: ").appendValue(expected.getWorkAddressLine1())
        .appendText("\nWork address line 2: ").appendValue(expected.getWorkAddressLine2())
        .appendText("\nWork address city: ").appendValue(expected.getWorkAddressCity())
        .appendText("\nWork address province: ").appendValue(expected.getWorkAddressProvince())
        .appendText("\nWork address district: ").appendValue(expected.getWorkAddressDistrict())
        .appendText("\nWork address state: ").appendValue(expected.getWorkAddressState())
        .appendText("\nWork address country: ").appendValue(expected.getWorkAddressCountry())
        .appendText("\nWork address zipcode: ").appendValue(expected.getWorkAddressZipcode())
        .appendText("\nPostal address line 1: ").appendValue(expected.getPostalAddressLine1())
        .appendText("\nPostal address line 2: ").appendValue(expected.getPostalAddressLine2())
        .appendText("\nPostal address city: ").appendValue(expected.getPostalAddressCity())
        .appendText("\nPostal address province: ").appendValue(expected.getPostalAddressProvince())
        .appendText("\nPostal address district: ").appendValue(expected.getPostalAddressDistrict())
        .appendText("\nPostal address state: ").appendValue(expected.getPostalAddressState())
        .appendText("\nPostal address country: ").appendValue(expected.getPostalAddressCountry())
        .appendText("\nPostal address zipcode: ").appendValue(expected.getPostalAddressZipcode());
  }

  @Override
  public boolean matchesSafely(DonorExportDTO actual) {
    return Objects.equals(actual.getDonorNumber(), expected.getDonorNumber()) &&
        Objects.equals(actual.getTitle(), expected.getTitle()) &&
        Objects.equals(actual.getFirstName(), expected.getFirstName()) &&
        Objects.equals(actual.getMiddleName(), expected.getMiddleName()) &&
        Objects.equals(actual.getLastName(), expected.getLastName()) &&
        Objects.equals(actual.getCallingName(), expected.getCallingName()) &&
        Objects.equals(actual.getGender(), expected.getGender()) &&
        Objects.equals(actual.getBirthDate().getTime(), expected.getBirthDate().getTime()) &&
        Objects.equals(actual.getPreferredLanguage(), expected.getPreferredLanguage()) &&
        Objects.equals(actual.getVenue(), expected.getVenue()) &&
        Objects.equals(actual.getBloodABO(), expected.getBloodABO()) &&
        Objects.equals(actual.getBloodRh(), expected.getBloodRh()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getIdType(), expected.getIdType()) &&
        Objects.equals(actual.getIdNumber(), expected.getIdNumber()) &&
        Objects.equals(actual.getDateOfFirstDonation().getTime(), expected.getDateOfFirstDonation().getTime()) &&
        Objects.equals(actual.getDateOfLastDonation().getTime(), expected.getDateOfLastDonation().getTime()) &&
        Objects.equals(actual.getDueToDonate().getTime(), expected.getDueToDonate().getTime()) &&
        Objects.equals(actual.getContactMethodType(), expected.getContactMethodType()) &&
        Objects.equals(actual.getHomeNumber(), expected.getHomeNumber()) &&
        Objects.equals(actual.getWorkNumber(), expected.getWorkNumber()) &&
        Objects.equals(actual.getMobileNumber(), expected.getMobileNumber()) &&
        Objects.equals(actual.getEmail(), expected.getEmail()) &&
        Objects.equals(actual.getPreferredAddressType(), expected.getPreferredAddressType()) &&
        Objects.equals(actual.getHomeAddressLine1(), expected.getHomeAddressLine1()) &&
        Objects.equals(actual.getHomeAddressLine2(), expected.getHomeAddressLine2()) &&
        Objects.equals(actual.getHomeAddressCity(), expected.getHomeAddressCity()) &&
        Objects.equals(actual.getHomeAddressProvince(), expected.getHomeAddressProvince()) &&
        Objects.equals(actual.getHomeAddressDistrict(), expected.getHomeAddressDistrict()) &&
        Objects.equals(actual.getHomeAddressState(), expected.getHomeAddressState()) &&
        Objects.equals(actual.getHomeAddressCountry(), expected.getHomeAddressCountry()) &&
        Objects.equals(actual.getHomeAddressZipcode(), expected.getHomeAddressZipcode()) &&
        Objects.equals(actual.getWorkAddressLine1(), expected.getWorkAddressLine1()) &&
        Objects.equals(actual.getWorkAddressLine2(), expected.getWorkAddressLine2()) &&
        Objects.equals(actual.getWorkAddressCity(), expected.getWorkAddressCity()) &&
        Objects.equals(actual.getWorkAddressProvince(), expected.getWorkAddressProvince()) &&
        Objects.equals(actual.getWorkAddressDistrict(), expected.getWorkAddressDistrict()) &&
        Objects.equals(actual.getWorkAddressState(), expected.getWorkAddressState()) &&
        Objects.equals(actual.getWorkAddressCountry(), expected.getWorkAddressCountry()) &&
        Objects.equals(actual.getWorkAddressZipcode(), expected.getWorkAddressZipcode()) &&
        Objects.equals(actual.getPostalAddressLine1(), expected.getPostalAddressLine1()) &&
        Objects.equals(actual.getPostalAddressLine2(), expected.getPostalAddressLine2()) &&
        Objects.equals(actual.getPostalAddressCity(), expected.getPostalAddressCity()) &&
        Objects.equals(actual.getPostalAddressProvince(), expected.getPostalAddressProvince()) &&
        Objects.equals(actual.getPostalAddressDistrict(), expected.getPostalAddressDistrict()) &&
        Objects.equals(actual.getPostalAddressState(), expected.getPostalAddressState()) &&
        Objects.equals(actual.getPostalAddressCountry(), expected.getPostalAddressCountry()) &&
        Objects.equals(actual.getPostalAddressZipcode(), expected.getPostalAddressZipcode());
  }

  public static DonorExportDTOMatcher hasSameStateAsDonorExport(DonorExportDTO expected) {
    return new DonorExportDTOMatcher(expected);
  }

}
