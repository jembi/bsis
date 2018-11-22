package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TestBatchFullDonationViewModel {

  private UUID id;
  private Date testBatchDate;
  private List<DonationFullViewModel> donations;

  public List<DonationFullViewModel> getDonations() {
    return donations;
  }

  public void setDonations(List<DonationFullViewModel> donations) {
    this.donations = donations;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getTestBatchDate() {
    return testBatchDate;
  }

  public void setTestBatchDate(Date testBatchDate) {
    this.testBatchDate = testBatchDate;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

}
