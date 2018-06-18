package org.jembi.bsis.viewmodel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.utils.DateTimeSerialiser;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodTestResultViewModel {

  private UUID id;
  private String testName;
  private BloodTestCategory testCategory;
  private String result;
  private Date testedOn;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getTestedOn() {
    return testedOn;
  }
}