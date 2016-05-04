package viewmodel;

import java.util.Date;
import java.util.List;

import utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentBatchViewModel {

  private Long id;
  private String status;
  private Date deliveryDate;
  private Date collectionDate;
  private DonationBatchViewModel donationBatch;
  private List<BloodTransportBoxViewModel> bloodTransportBoxes;
  private List<ComponentViewModel> components;
  
  public ComponentBatchViewModel() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCollectionDate() {
    return collectionDate;
  }

  public void setCollectionDate(Date collectionDate) {
    this.collectionDate = collectionDate;
  }

  public DonationBatchViewModel getDonationBatch() {
    return donationBatch;
  }

  public void setDonationBatch(DonationBatchViewModel donationBatch) {
    this.donationBatch = donationBatch;
  }

  public List<BloodTransportBoxViewModel> getBloodTransportBoxes() {
    return bloodTransportBoxes;
  }

  public void setBloodTransportBoxes(List<BloodTransportBoxViewModel> bloodTransportBoxes) {
    this.bloodTransportBoxes = bloodTransportBoxes;
  }

  public List<ComponentViewModel> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentViewModel> components) {
    this.components = components;
  }  
}
