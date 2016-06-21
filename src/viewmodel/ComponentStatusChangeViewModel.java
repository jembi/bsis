package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChange;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeType;
import model.user.User;
import utils.CustomDateFormatter;

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

  public Long getId() {
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

  public ComponentStatusChangeType getStatusChangeType() {
    return componentStatusChange.getStatusChangeType();
  }

}
