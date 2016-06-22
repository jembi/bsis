package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.BloodTestResultDTO;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class BloodTestResultDTOBuilder extends AbstractBuilder<BloodTestResultDTO> {
  
  private BloodTest bloodTest;
  private Gender gender;
  private long count;
  private Location venue;
  private String result;
  
  public BloodTestResultDTOBuilder withBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
    return this;
  }
  
  public BloodTestResultDTOBuilder withResult(String result) {
    this.result = result;
    return this;
  }

  public BloodTestResultDTOBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public BloodTestResultDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  public BloodTestResultDTOBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public BloodTestResultDTO build() {
    BloodTestResultDTO bloodTestResultDTO = new BloodTestResultDTO();
    bloodTestResultDTO.setBloodTest(bloodTest);
    bloodTestResultDTO.setResult(result);
    bloodTestResultDTO.setGender(gender);
    bloodTestResultDTO.setCount(count);
    bloodTestResultDTO.setVenue(venue);
    
    return bloodTestResultDTO;
  }

  public static BloodTestResultDTOBuilder aBloodTestResultDTO() {
    return new BloodTestResultDTOBuilder();
  }


}
