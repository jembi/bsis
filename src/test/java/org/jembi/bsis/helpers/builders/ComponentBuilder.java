package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.ComponentPersister;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.user.User;

public class ComponentBuilder extends AbstractEntityBuilder<Component> {

  private UUID id;
  private ComponentStatus status = ComponentStatus.QUARANTINED;
  private Donation donation = DonationBuilder.aDonation().build();
  private ComponentType componentType;
  private InventoryStatus inventoryStatus = InventoryStatus.NOT_IN_STOCK;
  private Location location = aVenue().build();
  private boolean isDeleted = false;
  private String componentCode;
  private Date expiresOn;
  private Date createdOn;
  private Component parentComponent;
  private Integer weight;
  private SortedSet<ComponentStatusChange> statusChanges;
  private Date discardedOn;
  private Date issuedOn;
  private String notes;
  private ComponentBatch componentBatch;
  private Date createdDate;
  private User createdBy;
  private Date processedOn;
  
  public ComponentBuilder withId(UUID id) {
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

  public ComponentBuilder withIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }
  
  public ComponentBuilder thatIsDeleted() {
    isDeleted = true;
    return this;
  }

  public ComponentBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public ComponentBuilder withExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
    return this;
  }

  public ComponentBuilder withCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }
  
  public ComponentBuilder withParentComponent(Component parentComponent) {
    this.parentComponent = parentComponent;
    return this;
  }

  public ComponentBuilder withWeight(Integer weight) {
    this.weight = weight;
    return this;
  }
  
  public ComponentBuilder withComponentStatusChange(ComponentStatusChange statusChange) {
    if (this.statusChanges == null) {
      this.statusChanges = new TreeSet<>();
    }
    this.statusChanges.add(statusChange);
    return this;
  }

  public ComponentBuilder withDiscardedOn(Date discardedOn) {
    this.discardedOn = discardedOn;
    return this;
  }

  public ComponentBuilder withIssuedOn(Date issuedOn) {
    this.issuedOn = issuedOn;
    return this;
  }

  public ComponentBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public ComponentBuilder withComponentBatch(ComponentBatch componentBatch) {
    this.componentBatch = componentBatch;
    return this;
  }
  
  public ComponentBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }
  
  public ComponentBuilder withCreatedBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public ComponentBuilder withProcessedOn(Date processedOn) {
    this.processedOn = processedOn;
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
    component.setIsDeleted(isDeleted);
    component.setComponentCode(componentCode);
    component.setExpiresOn(expiresOn);
    component.setCreatedOn(createdOn);
    component.setParentComponent(parentComponent);
    component.setWeight(weight);
    component.setStatusChanges(statusChanges);
    component.setDiscardedOn(discardedOn);
    component.setIssuedOn(issuedOn);
    component.setComponentBatch(componentBatch);
    component.setNotes(notes);
    component.setCreatedDate(createdDate);
    component.setCreatedBy(createdBy);
    component.setProcessedOn(processedOn);
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
