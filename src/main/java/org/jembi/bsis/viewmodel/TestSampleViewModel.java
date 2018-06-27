package org.jembi.bsis.viewmodel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSampleViewModel {

  private String din;
  private String venue;
  private Date donationDate;
  private String bloodGroup;
  private String packType;
  private TTIStatus ttiStatus;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private String testingSite;
  private Date testingDate;
  private List<BloodTestResultViewModel> testOutcomes;
  
  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDonationDate() {
    return donationDate;
  }
  
  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getTestingDate() {
    return testingDate;
  }
}