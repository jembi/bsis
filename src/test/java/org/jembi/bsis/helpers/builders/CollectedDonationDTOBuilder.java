package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class CollectedDonationDTOBuilder extends AbstractBuilder<CollectedDonationDTO> {

  private DonationType donationType;
  private Gender gender;
  private String bloodAbo;
  private String bloodRh;
  private long count;
  private Location venue;

  public CollectedDonationDTOBuilder withDonationType(DonationType donationType) {
    this.donationType = donationType;
    return this;
  }

  public CollectedDonationDTOBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public CollectedDonationDTOBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public CollectedDonationDTOBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public CollectedDonationDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  public CollectedDonationDTOBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public CollectedDonationDTO build() {
    CollectedDonationDTO collectedDonationDTO = new CollectedDonationDTO();
    collectedDonationDTO.setDonationType(donationType);
    collectedDonationDTO.setGender(gender);
    collectedDonationDTO.setBloodAbo(bloodAbo);
    collectedDonationDTO.setBloodRh(bloodRh);
    collectedDonationDTO.setCount(count);
    collectedDonationDTO.setVenue(venue);
    return collectedDonationDTO;
  }

  public static CollectedDonationDTOBuilder aCollectedDonationDTO() {
    return new CollectedDonationDTOBuilder();
  }

}
