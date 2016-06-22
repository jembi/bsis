package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.returnform.ReturnForm;

public class ReturnFormMatcher extends TypeSafeMatcher<ReturnForm> {

  private ReturnForm expected;

  public ReturnFormMatcher(ReturnForm expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An order form entity with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nCreated Date: ").appendValue(expected.getCreatedDate())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nReturn Date: ").appendValue(expected.getReturnDate())
        .appendText("\nReturned From: ").appendValue(expected.getReturnedFrom())
        .appendText("\nReturned To: ").appendValue(expected.getReturnedTo())
        .appendText("\nComponents: ").appendValue(expected.getComponents());
  }
  
  @Override
  public boolean matchesSafely(ReturnForm actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getReturnDate(), expected.getReturnDate()) &&
        Objects.equals(actual.getReturnedFrom(), expected.getReturnedFrom()) &&
        Objects.equals(actual.getReturnedTo(), expected.getReturnedTo()) &&
        Objects.equals(actual.getComponents(), expected.getComponents());
  }

  public static ReturnFormMatcher hasSameStateAsReturnForm(ReturnForm expected) {
    return new ReturnFormMatcher(expected);
  }

}
