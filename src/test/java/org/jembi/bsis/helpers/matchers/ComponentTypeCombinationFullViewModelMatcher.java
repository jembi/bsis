package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationFullViewModel;

public class ComponentTypeCombinationFullViewModelMatcher extends TypeSafeMatcher<ComponentTypeCombinationFullViewModel> {

  private ComponentTypeCombinationFullViewModel expected;
  
  public ComponentTypeCombinationFullViewModelMatcher(ComponentTypeCombinationFullViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A ComponentTypeCombinationFullViewModel with the following state:")
    .appendText("\nId: ").appendValue(expected.getId())
    .appendText("\nCombination name: ").appendValue(expected.getCombinationName())
    .appendText("\nIs deleted: ").appendValue(expected.getIsDeleted())
    .appendText("\nSource components: ").appendValue(expected.getSourceComponentTypes())
    .appendText("\nProduced components: ").appendValue(expected.getComponentTypes()); 
  }

  @Override
  protected boolean matchesSafely(ComponentTypeCombinationFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getCombinationName(), expected.getCombinationName()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getComponentTypes(), expected.getComponentTypes()) &&
        Objects.equals(actual.getSourceComponentTypes(), expected.getSourceComponentTypes());
  }
  
  public static ComponentTypeCombinationFullViewModelMatcher hasSameStateAsComponentTypeCombinationFullViewModel(
      ComponentTypeCombinationFullViewModel expected) {
    return new ComponentTypeCombinationFullViewModelMatcher(expected);
  }

}
