package org.jembi.bsis.dto;

import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.model.location.Location;

public class DonorsAdverseEventsDTO {

  private AdverseEventType adverseEventType;
  private Location venue;
  private long count;

  public DonorsAdverseEventsDTO() {
    super();
  }

  public DonorsAdverseEventsDTO(AdverseEventType adverseEventType, Location venue, long count) {
    this.adverseEventType = adverseEventType;
    this.venue = venue;
    this.count = count;
  }

  public AdverseEventType getAdverseEventType() {
    return adverseEventType;
  }

  public void setAdverseEvent(AdverseEventType adverseEventType) {
    this.adverseEventType = adverseEventType;
  }

  public Location getVenue() {
    return venue;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
  }

  public long getCount() {
    return count;
  }
  
  public void setCount(long count) {
    this.count = count;
  } 
}
