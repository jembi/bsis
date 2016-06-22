package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.ReturnFormFullViewModel;

public class ReturnFormFullViewModelMatcher extends TypeSafeMatcher<ReturnFormFullViewModel> {

  private ReturnFormFullViewModel expected;

  public ReturnFormFullViewModelMatcher(ReturnFormFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An order form entity with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nReturn Date: ").appendValue(expected.getReturnDate())
        .appendText("\nReturned From: ").appendValue(expected.getReturnedFrom())
        .appendText("\nReturned To: ").appendValue(expected.getReturnedTo())
        .appendText("\nComponents: ").appendValue(expected.getComponents())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions());
  }
  
  @Override
  public boolean matchesSafely(ReturnFormFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getReturnDate(), expected.getReturnDate()) &&
        Objects.equals(actual.getReturnedFrom(), expected.getReturnedFrom()) &&
        Objects.equals(actual.getReturnedTo(), expected.getReturnedTo()) &&
        Objects.equals(actual.getComponents(), expected.getComponents()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions());
  }

  public static ReturnFormFullViewModelMatcher hasSameStateAsReturnFormFullViewModel(ReturnFormFullViewModel expected) {
    return new ReturnFormFullViewModelMatcher(expected);
  }

}
