package org.jembi.bsis.viewmodel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jembi.bsis.utils.DateTimeSerialiser;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodTestResultFullViewModel {

  private UUID id;
  private BloodTestFullViewModel bloodTest;
  @Builder.Default
  private Boolean reEntryRequired = false;
  private String result;
  private Date testedOn;
  private Map<String, Boolean> permissions;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getTestedOn() {
    return testedOn;
  }

}