package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.BloodTransportBoxBackingForm;
import org.jembi.bsis.backingform.ComponentBatchBackingForm;
import org.jembi.bsis.backingform.DonationBatchBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;

public class ComponentBatchBackingFormBuilder extends AbstractBuilder<ComponentBatchBackingForm> {

  private UUID id;
  private Date deliveryDate;
  private ComponentBatchStatus status;
  private List<BloodTransportBoxBackingForm> bloodTransportBoxes = new ArrayList<>();
  private DonationBatchBackingForm donationBatch;
  private LocationBackingForm location;

  public ComponentBatchBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ComponentBatchBackingFormBuilder withBloodTransportBox(BloodTransportBoxBackingForm bloodTransportBox) {
    bloodTransportBoxes.add(bloodTransportBox);
    return this;
  }
  
  public ComponentBatchBackingFormBuilder withStatus(ComponentBatchStatus status) {
    this.status = status;
    return this;
  }
  
  public ComponentBatchBackingFormBuilder withDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
    return this;
  }

  public ComponentBatchBackingFormBuilder withDonationBatch(DonationBatchBackingForm donationBatch) {
    this.donationBatch = donationBatch;
    return this;
  }
  
  public ComponentBatchBackingFormBuilder withLocation(LocationBackingForm location) {
    this.location = location;
    return this;
  }

  @Override
  public ComponentBatchBackingForm build() {
    ComponentBatchBackingForm componentBatch = new ComponentBatchBackingForm();
    componentBatch.setId(id);
    componentBatch.setStatus(status);
    componentBatch.setDeliveryDate(deliveryDate);
    componentBatch.setBloodTransportBoxes(bloodTransportBoxes);
    componentBatch.setDonationBatch(donationBatch);
    componentBatch.setLocation(location);
    return componentBatch;
  }

  public static ComponentBatchBackingFormBuilder aComponentBatchBackingForm() {
    return new ComponentBatchBackingFormBuilder();
  }
}