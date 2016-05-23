package helpers.builders;

import backingform.ComponentBackingForm;
import backingform.LocationBackingForm;
import model.component.ComponentStatus;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.inventory.InventoryStatus;

public class ComponentBackingFormBuilder {

  private Long id;
  private ComponentStatus status;
  private Donation donation;
  private ComponentType componentType;
  private InventoryStatus inventoryStatus = InventoryStatus.NOT_LABELLED;
  private LocationBackingForm location;
  
  public ComponentBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentBackingFormBuilder withStatus(ComponentStatus status) {
    this.status = status;
    return this;
  }

  public ComponentBackingFormBuilder withDonation(Donation donation) {
    this.donation = donation;
    return this;
  }
  
  public ComponentBackingFormBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public ComponentBackingFormBuilder withLocation(LocationBackingForm location) {
    this.location = location;
    return this;
  }
  
  public ComponentBackingFormBuilder withInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
    return this;
  }

  public ComponentBackingForm build() {
    ComponentBackingForm backingForm = new ComponentBackingForm();
    backingForm.setId(id);
    backingForm.setStatus(status);
    backingForm.setDonation(donation);
    backingForm.setComponentType(componentType);
    backingForm.setLocation(location);
    backingForm.setInventoryStatus(inventoryStatus);
    return backingForm;
  }

  public static ComponentBackingFormBuilder aComponentBackingForm() {
    return new ComponentBackingFormBuilder();
  }

}
