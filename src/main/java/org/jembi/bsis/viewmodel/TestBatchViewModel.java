package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestBatchViewModel {

  private UUID id;
  private Date testBatchDate;
  private Date lastUpdated;
  private TestBatchStatus status;
  private String batchNumber;
  private String notes;
  private Integer numSamples;
  private LocationViewModel location;
  private boolean backEntry;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getTestBatchDate() {
    return testBatchDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getLastUpdated() {
    return lastUpdated;
  }

}
