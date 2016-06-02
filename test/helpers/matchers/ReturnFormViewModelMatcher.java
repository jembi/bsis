package helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import viewmodel.ReturnFormViewModel;

public class ReturnFormViewModelMatcher extends TypeSafeMatcher<ReturnFormViewModel> {

  private ReturnFormViewModel expected;

  public ReturnFormViewModelMatcher(ReturnFormViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An order form entity with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nReturn Date: ").appendValue(expected.getReturnDate())
        .appendText("\nReturned From: ").appendValue(expected.getReturnedFrom())
        .appendText("\nReturned To: ").appendValue(expected.getReturnedTo());
  }
  
  @Override
  public boolean matchesSafely(ReturnFormViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getReturnDate(), expected.getReturnDate()) &&
        Objects.equals(actual.getReturnedFrom(), expected.getReturnedFrom()) &&
        Objects.equals(actual.getReturnedTo(), expected.getReturnedTo());
  }

  public static ReturnFormViewModelMatcher hasSameStateAsReturnFormViewModel(ReturnFormViewModel expected) {
    return new ReturnFormViewModelMatcher(expected);
  }

}
