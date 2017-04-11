package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.ComponentBatchPersister;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.BloodTransportBox;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.util.RandomTestDate;

public class ComponentBatchBuilder extends AbstractEntityBuilder<ComponentBatch> {

  private UUID id;
  private Date deliveryDate = new RandomTestDate();
  private Date collectionDate = new RandomTestDate();
  private Set<Component> components = new HashSet<>();
  private Set<BloodTransportBox> bloodTransportBoxes = new HashSet<>();
  private ComponentBatchStatus status = ComponentBatchStatus.OPEN;
  private boolean deleted;
  private DonationBatch donationBatch = aDonationBatch().build();
  private Location location = aProcessingSite().build();

  public ComponentBatchBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ComponentBatchBuilder withBloodTransportBox(BloodTransportBox bloodTransportBox) {
    bloodTransportBoxes.add(bloodTransportBox);
    return this;
  }
  
  public ComponentBatchBuilder withComponent(Component component) {
    components.add(component);
    return this;
  }
  
  public ComponentBatchBuilder withStatus(ComponentBatchStatus status) {
    this.status = status;
    return this;
  }
  
  public ComponentBatchBuilder withDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
    return this;
  }
  
  public ComponentBatchBuilder withCollectionDate(Date collectionDate) {
    this.collectionDate = collectionDate;
    return this;
  }

  public ComponentBatchBuilder withDonationBatch(DonationBatch donationBatch) {
    this.donationBatch = donationBatch;
    return this;
  }
  
  public ComponentBatchBuilder withLocation(Location location) {
    this.location = location;
    return this;
  }

  public ComponentBatchBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  @Override
  public AbstractEntityPersister<ComponentBatch> getPersister() {
    return new ComponentBatchPersister();
  }

  @Override
  public ComponentBatch build() {
    ComponentBatch componentBatch = new ComponentBatch();
    componentBatch.setId(id);
    componentBatch.setStatus(status);
    componentBatch.setDeliveryDate(deliveryDate);
    componentBatch.setCollectionDate(collectionDate);
    componentBatch.setComponents(components);
    componentBatch.setBloodTransportBoxes(bloodTransportBoxes);
    componentBatch.setIsDeleted(deleted);
    componentBatch.setDonationBatch(donationBatch);
    componentBatch.setLocation(location);
    return componentBatch;
  }

  public static ComponentBatchBuilder aComponentBatch() {
    return new ComponentBatchBuilder();
  }

}
