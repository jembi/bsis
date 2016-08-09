package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.DeferredDonorsDTO;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class DeferredDonorsDTOBuilder extends AbstractBuilder<DeferredDonorsDTO> {
  
  private DeferralReason deferralReason;
  private Gender gender;
  private Location venue;
  private long count;
  
  public DeferredDonorsDTOBuilder withDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
    return this;
  }
  
  public DeferredDonorsDTOBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public DeferredDonorsDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  public DeferredDonorsDTOBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public DeferredDonorsDTO build() {
    DeferredDonorsDTO deferredDonorsDTO = new DeferredDonorsDTO();
    deferredDonorsDTO.setDeferralReason(deferralReason);
    deferredDonorsDTO.setGender(gender);
    deferredDonorsDTO.setCount(count);
    deferredDonorsDTO.setVenue(venue);
    
    return deferredDonorsDTO;
  }

  public static DeferredDonorsDTOBuilder aDeferredDonorsDTO() {
    return new DeferredDonorsDTOBuilder();
  }


}
