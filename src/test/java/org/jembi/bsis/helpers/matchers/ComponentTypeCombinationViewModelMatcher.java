package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;

public class ComponentTypeCombinationViewModelMatcher extends TypeSafeMatcher<ComponentTypeCombinationViewModel> {

  private ComponentTypeCombinationViewModel expected;
  
  public ComponentTypeCombinationViewModelMatcher(ComponentTypeCombinationViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A ComponentTypeCombinationViewModel with the following state:")
    .appendText("\nId: ").appendValue(expected.getId())
    .appendText("\nCombination name: ").appendValue(expected.getCombinationName())
    .appendText("\nIs deleted: ").appendValue(expected.getIsDeleted()); 
  }

  @Override
  protected boolean matchesSafely(ComponentTypeCombinationViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getCombinationName(), expected.getCombinationName()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }
  
  public static ComponentTypeCombinationViewModelMatcher hasSameStateAsComponentTypeCombinationViewModel(
      ComponentTypeCombinationViewModel expected) {
    return new ComponentTypeCombinationViewModelMatcher(expected);
  }

}
