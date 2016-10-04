package org.jembi.bsis.dto;

import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

import java.util.Objects;

public class DisdardedComponentsDTO {

  private ComponentType componentType;
  private ComponentStatusChangeReason componentStatusChangeReason;
  private long count;
  private Location venue;

  public DisdardedComponentsDTO() {
    // Default constructor
  }

  public DisdardedComponentsDTO(ComponentType componentType, ComponentStatusChangeReason componentStatusChangeReason, Location venue, long count) {
    this.venue = venue;
    this.count = count;
  }

}
