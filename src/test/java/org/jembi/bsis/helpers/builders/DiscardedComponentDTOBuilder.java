package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.BloodTestResultDTO;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class DiscardedComponentDTOBuilder extends AbstractBuilder<BloodTestResultDTO> {
  
  private BloodTest bloodTest;
  private Gender gender;
  private long count;
  private Location venue;
  private String result;
  
  public DiscardedComponentDTOBuilder withBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
    return this;
  }
  
  public DiscardedComponentDTOBuilder withResult(String result) {
    this.result = result;
    return this;
  }

  public DiscardedComponentDTOBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public DiscardedComponentDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  public DiscardedComponentDTOBuilder withVenue(Location venue) {
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

  public static DiscardedComponentDTOBuilder aBloodTestResultDTO() {
    return new DiscardedComponentDTOBuilder();
  }


}
