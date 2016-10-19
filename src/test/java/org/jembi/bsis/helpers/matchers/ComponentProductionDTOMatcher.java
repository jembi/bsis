package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.dto.ComponentProductionDTO;

public class ComponentProductionDTOMatcher extends TypeSafeMatcher<ComponentProductionDTO>{
  private ComponentProductionDTO expected;
  
  public ComponentProductionDTOMatcher (ComponentProductionDTO expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A Component with the following state:")
    .appendText("\nProcessing site: ").appendValue(expected.getProcessingSite())
    .appendText("\nComponent name: ").appendValue(expected.getComponentTypeName())
    .appendText("\nBlood RBO: ").appendValue(expected.getBloodAbo())
    .appendText("\nBlood RH: ").appendValue(expected.getBloodRh())
    .appendText("\nCount: ").appendValue(expected.getCount());
  }

  @Override
  protected boolean matchesSafely(ComponentProductionDTO actual) {
    return Objects.equals(actual.getProcessingSite(), expected.getProcessingSite()) &&
        Objects.equals(actual.getComponentTypeName(), expected.getComponentTypeName()) &&
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo()) &&
        Objects.equals(actual.getBloodRh(), expected.getBloodRh()) &&
        Objects.equals(actual.getCount(), expected.getCount());
  }
  
  public static ComponentProductionDTOMatcher hasSameStateAsComponentProductionDTO(ComponentProductionDTO expected) {
    return new ComponentProductionDTOMatcher(expected);
  }
}
