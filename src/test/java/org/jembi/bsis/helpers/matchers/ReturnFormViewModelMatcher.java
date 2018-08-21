package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.ReturnFormViewModel;

public class ReturnFormViewModelMatcher extends AbstractTypeSafeMatcher<ReturnFormViewModel> {

  public ReturnFormViewModelMatcher(ReturnFormViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, ReturnFormViewModel returnFormViewModel) {
    description.appendText("An ReturnFormViewModel with the following state:")
        .appendText("\nId: ").appendValue(returnFormViewModel.getId())
        .appendText("\nStatus: ").appendValue(returnFormViewModel.getStatus())
        .appendText("\nReturn Date: ").appendValue(returnFormViewModel.getReturnDate())
        .appendText("\nReturned From: ").appendValue(returnFormViewModel.getReturnedFrom())
        .appendText("\nReturned To: ").appendValue(returnFormViewModel.getReturnedTo());
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
