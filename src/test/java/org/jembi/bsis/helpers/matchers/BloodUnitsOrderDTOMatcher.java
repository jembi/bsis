package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.dto.BloodUnitsOrderDTO;

public class BloodUnitsOrderDTOMatcher extends TypeSafeMatcher<BloodUnitsOrderDTO>{
  private BloodUnitsOrderDTO expected;
  
  public BloodUnitsOrderDTOMatcher (BloodUnitsOrderDTO expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A Component with the following state:")
    .appendText("\nDistribution site: ").appendValue(expected.getDistributionSite())
    .appendText("\nComponent type: ").appendValue(expected.getComponentType())
    .appendText("\nCount: ").appendValue(expected.getCount());
  }

  @Override
  protected boolean matchesSafely(BloodUnitsOrderDTO actual) {
    return Objects.equals(actual.getDistributionSite(), expected.getDistributionSite()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getCount(), expected.getCount());
  }
  
  public static BloodUnitsOrderDTOMatcher hasSameStateAsBloodUnitsOrderDTO(BloodUnitsOrderDTO expected) {
    return new BloodUnitsOrderDTOMatcher(expected);
  }
}
