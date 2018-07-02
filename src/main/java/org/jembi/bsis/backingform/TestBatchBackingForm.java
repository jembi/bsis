package org.jembi.bsis.backingform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.jembi.bsis.model.testbatch.TestBatchStatus;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestBatchBackingForm {

  private UUID id;
  private TestBatchStatus status;
  private Date testBatchDate;
  private LocationBackingForm location;
  private boolean backEntry;
}
