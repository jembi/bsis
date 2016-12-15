package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.dto.DiscardedComponentDTO;

public class DiscardedComponentDTOMatcher extends TypeSafeMatcher<DiscardedComponentDTO> {

  private DiscardedComponentDTO expected;

  public DiscardedComponentDTOMatcher(DiscardedComponentDTO expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donor with the following state:")
        .appendText("\nComponentType: ").appendValue(expected.getComponentType())
        .appendText("\nStatus Change Reason: ").appendValue(expected.getComponentStatusChangeReason())
        .appendText("\nVenue: ").appendValue(expected.getVenue())
        .appendText("\nCount: ").appendValue(expected.getCount());
  }

  @Override
  public boolean matchesSafely(DiscardedComponentDTO actual) {
    return Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getComponentStatusChangeReason(), expected.getComponentStatusChangeReason()) &&
        Objects.equals(actual.getVenue(), expected.getVenue()) &&
        Objects.equals(actual.getCount(), expected.getCount());
  }

  public static DiscardedComponentDTOMatcher hasSameStateAsDiscardedComponentDTO(DiscardedComponentDTO expected) {
    return new DiscardedComponentDTOMatcher(expected);
  }

}
