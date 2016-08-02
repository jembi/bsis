package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.BloodTestTotalDTO;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class BloodTestTotalDTOBuilder extends AbstractBuilder<BloodTestTotalDTO> {
  
  private Gender gender;
  private long total;
  private Location venue;

  public BloodTestTotalDTOBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public BloodTestTotalDTOBuilder withTotal(long total) {
    this.total = total;
    return this;
  }

  public BloodTestTotalDTOBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public BloodTestTotalDTO build() {
    BloodTestTotalDTO dto = new BloodTestTotalDTO();
    dto.setGender(gender);
    dto.setTotal(total);
    dto.setVenue(venue);
    return dto;
  }

  public static BloodTestTotalDTOBuilder aBloodTestTotalDTO() {
    return new BloodTestTotalDTOBuilder();
  }


}
