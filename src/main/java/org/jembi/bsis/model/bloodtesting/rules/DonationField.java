package org.jembi.bsis.model.bloodtesting.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.model.util.BloodAbo;
import org.jembi.bsis.model.util.BloodRh;

public enum DonationField {
  BLOODABO, BLOODRH, TTISTATUS, TITRE;

  /**
   * Get a subset of the relevant DonationFields given a BloodTestCategory.
   *
   * @param category BloodTestCategory
   * @return List of DonationFields, an empty list if the category is unknown
   */
  public static List<DonationField> getDonationFieldsForCategory(BloodTestCategory category) {
    if (category == BloodTestCategory.TTI) {
      return Arrays.asList(
          DonationField.TTISTATUS);
    } else if (category == BloodTestCategory.BLOODTYPING) {
      return Arrays.asList(
          DonationField.BLOODABO,
          DonationField.BLOODRH,
          DonationField.TITRE);
    } else {
      return new ArrayList<>();
    }
  }

  /**
   * Get a list of the possible new information values for a given DonationField.
   *
   * @param donationField DonationField
   * @return List of String values
   */
  public static List<String> getNewInformationForDonationField(DonationField donationField) {
    List<String> newInformation = new ArrayList<>();

    if (donationField == DonationField.BLOODABO) {
      for (BloodAbo abo : BloodAbo.values()) {
        newInformation.add(abo.getValue());
      }
    } else if (donationField == DonationField.BLOODRH) {
      for (BloodRh rh : BloodRh.values()) {
        newInformation.add(rh.getValue());
      }
    } else if (donationField == DonationField.TTISTATUS) {
      for (TTIStatus ttiStatus : TTIStatus.values()) {
        newInformation.add(ttiStatus.name());
      }
    } else if (donationField == DonationField.TITRE) {
      for (Titre titre : Titre.values()) {
        newInformation.add(titre.name());
      }
    }

    return newInformation;
  }
}
