package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.dto.DonorsAdverseEventsDTO;

public class DonorsAdverseEventsDTOMatcher extends TypeSafeMatcher<DonorsAdverseEventsDTO> {

  private DonorsAdverseEventsDTO expected;

  public DonorsAdverseEventsDTOMatcher(DonorsAdverseEventsDTO expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A Donors Adverse Events DTO with the following state:")
        .appendText("\nVenue: ").appendValue(expected.getVenue().getName())
        .appendText("\nAdverse Event Type: ").appendValue(expected.getAdverseEventType().getName())
        .appendText("\nCount: ").appendValue(expected.getCount());
  }

  @Override
  public boolean matchesSafely(DonorsAdverseEventsDTO actual) {
    return Objects.equals(actual.getVenue(), expected.getVenue()) &&
        Objects.equals(actual.getAdverseEventType(), expected.getAdverseEventType()) &&
        Objects.equals(actual.getCount(), expected.getCount());
  }

  public static DonorsAdverseEventsDTOMatcher hasSameStateAsDonorsAdverseEventsDTO(DonorsAdverseEventsDTO expected) {
    return new DonorsAdverseEventsDTOMatcher(expected);
  }

}
