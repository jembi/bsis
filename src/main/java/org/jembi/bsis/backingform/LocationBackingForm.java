
package org.jembi.bsis.backingform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationBackingForm {

  private UUID id;
  private String name;
  private String notes;
  private DivisionBackingForm divisionLevel3;

  @JsonProperty("isVenue")
  private boolean venue;
  @JsonProperty("isMobileSite")
  private boolean mobileSite;
  @JsonProperty("isProcessingSite")
  private boolean processingSite;
  @JsonProperty("isDistributionSite")
  private boolean distributionSite;
  @JsonProperty("isTestingSite")
  private boolean testingSite;
  @JsonProperty("isUsageSite")
  private boolean usageSite;
  @JsonProperty("isReferralSite")
  private boolean referralSite;
  @JsonProperty("isDeleted")
  private boolean deleted;
}