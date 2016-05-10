package helpers.builders;

import static helpers.builders.LocationBuilder.aVenue;
import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.ComponentPersister;
import model.component.Component;
import model.component.ComponentStatus;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.inventory.InventoryStatus;
import model.location.Location;

public class ComponentBuilder extends AbstractEntityBuilder<Component> {

  private Long id;
  private ComponentStatus status;
  private Donation donation;
  private ComponentType componentType;
  private InventoryStatus inventoryStatus = InventoryStatus.NOT_LABELLED;
  private Location location = aVenue().build();
  
  public ComponentBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentBuilder withStatus(ComponentStatus status) {
    this.status = status;
    return this;
  }

  public ComponentBuilder withDonation(Donation donation) {
    this.donation = donation;
    return this;
  }
  
  public ComponentBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public ComponentBuilder withLocation(Location location) {
    this.location = location;
    return this;
  }
  
  public ComponentBuilder withInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
    return this;
  }

  @Override
  public Component build() {
    Component component = new Component();
    component.setId(id);
    component.setStatus(status);
    component.setDonation(donation);
    component.setComponentType(componentType);
    component.setLocation(location);
    component.setInventoryStatus(inventoryStatus);
    return component;
  }

  @Override
  public AbstractEntityPersister<Component> getPersister() {
    return new ComponentPersister();
  }

  public static ComponentBuilder aComponent() {
    return new ComponentBuilder();
  }

}
