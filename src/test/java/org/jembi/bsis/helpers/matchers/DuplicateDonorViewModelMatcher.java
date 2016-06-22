package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DuplicateDonorViewModel;

public class DuplicateDonorViewModelMatcher extends TypeSafeMatcher<DuplicateDonorViewModel> {

  private DuplicateDonorViewModel expected;

  public DuplicateDonorViewModelMatcher(DuplicateDonorViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A duplicate donor view model with the following state:")
        .appendText("\nBirth date: ").appendValue(expected.getBirthDate())
        .appendText("\nCount: ").appendValue(expected.getCount())
        .appendText("\nFirst name: ").appendValue(expected.getFirstName())
        .appendText("\nLast name: ").appendValue(expected.getLastName())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nGroup key: ").appendValue(expected.getGroupKey());
  }

  @Override
  public boolean matchesSafely(DuplicateDonorViewModel actual) {
    return Objects.equals(actual.getBirthDate(), expected.getBirthDate())
        && Objects.equals(actual.getCount(), expected.getCount())
        && Objects.equals(actual.getFirstName(), expected.getFirstName())
        && Objects.equals(actual.getLastName(), expected.getLastName())
        && Objects.equals(actual.getGender(), expected.getGender())
        && Objects.equals(actual.getGroupKey(), expected.getGroupKey());
  }

  public static DuplicateDonorViewModelMatcher hasSameStateAsDuplicateDonorViewModel(DuplicateDonorViewModel expected) {
    return new DuplicateDonorViewModelMatcher(expected);
  }

}
