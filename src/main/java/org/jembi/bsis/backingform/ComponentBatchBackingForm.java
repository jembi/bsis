package org.jembi.bsis.backingform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentBatchBackingForm {
  
  private UUID id;
  private ComponentBatchStatus status;
  private DonationBatchBackingForm donationBatch;
  private List<BloodTransportBoxBackingForm> bloodTransportBoxes = new ArrayList<>();
  private Date deliveryDate;
  private LocationBackingForm location;

  public ComponentBatchBackingForm() {
    super();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ComponentBatchStatus getStatus() {
    return status;
  }

  public void setStatus(ComponentBatchStatus status) {
    this.status = status;
  }

  public DonationBatchBackingForm getDonationBatch() {
    return donationBatch;
  }

  public void setDonationBatch(DonationBatchBackingForm donationBatch) {
    this.donationBatch = donationBatch;
  }

  public List<BloodTransportBoxBackingForm> getBloodTransportBoxes() {
    return bloodTransportBoxes;
  }

  public void setBloodTransportBoxes(List<BloodTransportBoxBackingForm> bloodTransportBoxes) {
    this.bloodTransportBoxes = bloodTransportBoxes;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDeliveryDate() {
    return deliveryDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public LocationBackingForm getLocation() {
    return location;
  }

  public void setLocation(LocationBackingForm location) {
    this.location = location;
  }
}
