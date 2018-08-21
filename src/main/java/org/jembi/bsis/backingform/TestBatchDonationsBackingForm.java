package org.jembi.bsis.backingform;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestBatchDonationsBackingForm {
  private UUID testBatchId;
  private List<UUID> donationIds;
}
