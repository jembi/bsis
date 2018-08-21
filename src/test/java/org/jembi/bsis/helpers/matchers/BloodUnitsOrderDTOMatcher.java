package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.dto.BloodUnitsOrderDTO;

public class BloodUnitsOrderDTOMatcher extends AbstractTypeSafeMatcher<BloodUnitsOrderDTO> {
  
  public BloodUnitsOrderDTOMatcher (BloodUnitsOrderDTO expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, BloodUnitsOrderDTO obj) {
    description.appendText("\nDistribution site: ").appendValue(obj.getDistributionSite())
        .appendText("\nComponent type: ").appendValue(obj.getComponentType())
        .appendText("\nOrder Type: ").appendValue(obj.getOrderType())
        .appendText("\nCount: ").appendValue(obj.getCount());
  }

  @Override
  protected boolean matchesSafely(BloodUnitsOrderDTO actual) {
    return Objects.equals(actual.getDistributionSite(), expected.getDistributionSite()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getOrderType(), expected.getOrderType()) &&
        Objects.equals(actual.getCount(), expected.getCount());
  }
  
  public static BloodUnitsOrderDTOMatcher hasSameStateAsBloodUnitsOrderDTO(BloodUnitsOrderDTO expected) {
    return new BloodUnitsOrderDTOMatcher(expected);
  }
}
