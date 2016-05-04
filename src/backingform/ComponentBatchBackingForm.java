package backingform;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.componentbatch.BloodTransportBox;
import model.componentbatch.ComponentBatch;
import model.componentbatch.ComponentBatchStatus;
import model.donationbatch.DonationBatch;
import utils.DateTimeSerialiser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentBatchBackingForm {
  
  private Long id;
  private ComponentBatchStatus status;
  private DonationBatchBackingForm donationBatch;
  private List<BloodTransportBoxBackingForm> bloodTransportBoxes = new ArrayList<>();
  private Date deliveryDate;

  public ComponentBatchBackingForm() {
    super();
  }
  
  @JsonIgnore
  public ComponentBatch getComponentBatch() {
    ComponentBatch componentBatch = new ComponentBatch();
    
    componentBatch.setId(getId());
    componentBatch.setStatus(getStatus());
    componentBatch.setDeliveryDate(getDeliveryDate());
    
    Set<BloodTransportBox> boxes = new HashSet<>();
    for (BloodTransportBoxBackingForm boxForm : getBloodTransportBoxes()) {
      BloodTransportBox box = boxForm.getBloodTransportBox();
      box.setComponentBatch(componentBatch);
      boxes.add(box);
    }
    componentBatch.setBloodTransportBoxCount(boxes.size());
    componentBatch.setBloodTransportBoxes(boxes);

    if (getDonationBatch() != null) {
      DonationBatch donationBatch = getDonationBatch().getDonationBatch();
      componentBatch.setDonationBatch(donationBatch);
      donationBatch.setComponentBatch(componentBatch);
    }

    return componentBatch;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
}
