package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.DonorsAdverseEventsDTO;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.model.location.Location;

public class DonorsAdverseEventsDTOBuilder extends AbstractBuilder<DonorsAdverseEventsDTO> {
  
  private AdverseEventType adverseEventType;
  private Location venue;
  private long count;
  
  public DonorsAdverseEventsDTOBuilder withAdverseEventType(AdverseEventType adverseEventType) {
    this.adverseEventType = adverseEventType;
    return this;
  }

  public DonorsAdverseEventsDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  public DonorsAdverseEventsDTOBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public DonorsAdverseEventsDTO build() {
    DonorsAdverseEventsDTO donorsAdverseEventsDTO = new DonorsAdverseEventsDTO();
    donorsAdverseEventsDTO.setAdverseEvent(adverseEventType);
    donorsAdverseEventsDTO.setCount(count);
    donorsAdverseEventsDTO.setVenue(venue);
    return donorsAdverseEventsDTO;
  }

  public static DonorsAdverseEventsDTOBuilder aDonorsAdverseEventsDTO() {
    return new DonorsAdverseEventsDTOBuilder();
  }


}
