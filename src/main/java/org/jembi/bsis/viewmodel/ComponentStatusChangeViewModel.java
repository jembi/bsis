package org.jembi.bsis.viewmodel;

import java.util.UUID;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentStatusChangeViewModel {

  public ComponentStatusChangeViewModel() {
  }

  public ComponentStatusChangeViewModel(ComponentStatusChange componentStatusChange) {
    this.componentStatusChange = componentStatusChange;
  }

  @JsonIgnore
  private ComponentStatusChange componentStatusChange;

  public ComponentStatusChange getComponentStatusChange() {
    return componentStatusChange;
  }

  public void setComponentStatusChange(ComponentStatusChange componentStatusChange) {
    this.componentStatusChange = componentStatusChange;
  }

  public UUID getId() {
    return componentStatusChange.getId();
  }

  @JsonIgnore
  public Component getComponent() {
    return componentStatusChange.getComponent();
  }

  public String getStatusChangedOn() {
    return CustomDateFormatter.getDateTimeString(componentStatusChange.getStatusChangedOn());
  }

  public ComponentStatus getNewStatus() {
    return componentStatusChange.getNewStatus();
  }

  public String getChangedBy() {
    User user = componentStatusChange.getChangedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getStatusChangeReasonText() {
    return componentStatusChange.getStatusChangeReasonText();
  }

  public String getIssuedBy() {
    User user = componentStatusChange.getIssuedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getIssuedOn() {
    return CustomDateFormatter.getDateTimeString(componentStatusChange.getIssuedOn());
  }

  public ComponentStatusChangeReason getStatusChangeReason() {
    return componentStatusChange.getStatusChangeReason();
  }

}
