package org.jembi.bsis.dto;

import java.util.Objects;

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
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof DonorsAdverseEventsDTO)) {
      return false;
    }

    DonorsAdverseEventsDTO other = (DonorsAdverseEventsDTO) obj;

    return Objects.equals(getAdverseEventType(), other.getAdverseEventType()) &&
        Objects.equals(getCount(), other.getCount()) &&
        Objects.equals(getVenue(), other.getVenue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getAdverseEventType(), getVenue(), getCount());
  }

  @Override
  public String toString() {
    return "DonorsAdverseEventsDTO [adverseEventType=" + adverseEventType.getName() + ", venue=" + venue.getName() + ", count=" + count
        + "]";
  }
  
  
}
