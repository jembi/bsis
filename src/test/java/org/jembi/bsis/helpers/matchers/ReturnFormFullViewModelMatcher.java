package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.ReturnFormFullViewModel;

public class ReturnFormFullViewModelMatcher extends AbstractTypeSafeMatcher<ReturnFormFullViewModel> {

  public ReturnFormFullViewModelMatcher(ReturnFormFullViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, ReturnFormFullViewModel returnFormFullViewModel) {
    description.appendText("An ReturnFormFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(returnFormFullViewModel.getId())
        .appendText("\nStatus: ").appendValue(returnFormFullViewModel.getStatus())
        .appendText("\nReturn Date: ").appendValue(returnFormFullViewModel.getReturnDate())
        .appendText("\nReturned From: ").appendValue(returnFormFullViewModel.getReturnedFrom())
        .appendText("\nReturned To: ").appendValue(returnFormFullViewModel.getReturnedTo())
        .appendText("\nComponents: ").appendValue(returnFormFullViewModel.getComponents())
        .appendText("\nPermissions: ").appendValue(returnFormFullViewModel.getPermissions());
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
