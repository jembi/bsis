package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.GeneralConfigViewModel;

public class GeneralConfigViewModelMatcher extends TypeSafeMatcher<GeneralConfigViewModel>{
  
  private GeneralConfigViewModel expected;
  
  public GeneralConfigViewModelMatcher(GeneralConfigViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A GeneralConfigViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nDescription: ").appendValue(expected.getDescription())
        .appendText("\nDataType: ").appendValue(expected.getDataType())
        .appendText("\nValue: ").appendValue(expected.getValue());
  }

  @Override
  public boolean matchesSafely(GeneralConfigViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getName(), expected.getName()) &&
        Objects.equals(actual.getDescription(), expected.getDescription()) &&
        Objects.equals(actual.getDataType(), expected.getDataType()) &&
        Objects.equals(actual.getValue(), expected.getValue());
  }
  
  public static GeneralConfigViewModelMatcher hasSameStateAsGeneralConfigViewModel(GeneralConfigViewModel expected) {
    return new GeneralConfigViewModelMatcher(expected);
  }
}
