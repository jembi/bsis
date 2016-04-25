package backingform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import model.componentbatch.ComponentBatchStatus;

public class ComponentBatchBackingForm {
  
  private Long id;
  private ComponentBatchStatus componentBatchStatus;
  private DonationBatchBackingForm donationBatch;
  private List<BloodTransportBoxBackingForm> bloodTransportBoxes = new ArrayList<>();
  private Date deliveryDate;
  private boolean isDeleted = false;

  public ComponentBatchBackingForm() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ComponentBatchStatus getComponentBatchStatus() {
    return componentBatchStatus;
  }

  public void setComponentBatchStatus(ComponentBatchStatus componentBatchStatus) {
    this.componentBatchStatus = componentBatchStatus;
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

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }  
}
