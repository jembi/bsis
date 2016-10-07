package org.jembi.bsis.dto;

import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;

import java.util.Objects;

public class DiscardedComponentDTO {

  private String componentType;
  private String componentStatusChangeReason;
  private Long count;
  private String venue;

  public DiscardedComponentDTO() {
    // Default constructor
  }

  public DiscardedComponentDTO(String componentType, String componentStatusChangeReason, String venue, Long count) {
    this.componentType = componentType;
    this.componentStatusChangeReason = componentStatusChangeReason;
    this.venue = venue;
    this.count = count;
  }

  public String getComponentType() {
    return componentType;
  }

  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }

  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public String getComponentStatusChangeReason() {
    return componentStatusChangeReason;
  }

  public void setComponentStatusChangeReason(String componentStatusChangeReason) {
    this.componentStatusChangeReason = componentStatusChangeReason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DiscardedComponentDTO)) return false;

    DiscardedComponentDTO other = (DiscardedComponentDTO) o;

    return Objects.equals(getComponentType(), other.getComponentType()) &&
            Objects.equals(getComponentStatusChangeReason(), other.getComponentStatusChangeReason()) &&
            Objects.equals(getCount(), other.getCount()) &&
            Objects.equals(getVenue(), other.getVenue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getComponentType(), getComponentStatusChangeReason(), getCount(), getVenue());
  }
}
