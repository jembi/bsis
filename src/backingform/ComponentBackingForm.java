package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.component.ComponentStatus;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.inventory.InventoryStatus;

public class ComponentBackingForm {

  private Long id;

  private LocationBackingForm location;

  private InventoryStatus inventoryStatus;

  private ComponentStatus status;

  private Donation donation;

  private ComponentType componentType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocationBackingForm getLocation() {
    return location;
  }

  public void setLocation(LocationBackingForm location) {
    this.location = location;
  }

  public InventoryStatus getInventoryStatus() {
    return inventoryStatus;
  }

  public void setInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
  }

  public ComponentStatus getStatus() {
    return status;
  }

  public void setStatus(ComponentStatus status) {
    this.status = status;
  }

  public Donation getDonation() {
    return donation;
  }

  public void setDonation(Donation donation) {
    this.donation = donation;
  }

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  @JsonIgnore
  public void setBloodAbo(String bloodAbo) {
    // Ignore
  }

  @JsonIgnore
  public void setBloodRh(String bloodRh) {
    // Ignore
  }

}