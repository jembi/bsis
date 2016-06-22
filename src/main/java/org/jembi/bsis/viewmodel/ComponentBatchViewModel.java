package org.jembi.bsis.viewmodel;

import java.util.Date;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentBatchViewModel {

  private Long id;
  private String status;
  private Date deliveryDate;
  private Location location;
  private Date collectionDate;
  private DonationBatchViewModel donationBatch;
  private int numberOfComponents;
  private int numberOfBoxes;
  
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

  public LocationViewModel getLocation() {
    return new LocationViewModel(location);
  }

  public void setLocation(Location location) {
    this.location = location;
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

  public int getNumberOfComponents() {
    return numberOfComponents;
  }

  public void setNumberOfComponents(int numberOfComponents) {
    this.numberOfComponents = numberOfComponents;
  }

  public int getNumberOfBoxes() {
    return numberOfBoxes;
  }

  public void setNumberOfBoxes(int numberOfBoxes) {
    this.numberOfBoxes = numberOfBoxes;
  }  


}
