package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;

public class ComponentTypeViewModelMatcher extends TypeSafeMatcher<ComponentTypeViewModel> {

  private ComponentTypeViewModel expected;
  
  public ComponentTypeViewModelMatcher(ComponentTypeViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A ComponentTypeViewModel with the following state:")
    .appendText("\nId: ").appendValue(expected.getId())
    .appendText("\nComponentTypeName: ").appendValue(expected.getComponentTypeName())
    .appendText("\nComponentTypeCode: ").appendValue(expected.getComponentTypeCode())
    .appendText("\nDescription: ").appendValue(expected.getDescription())
    .appendText("\nMaxBleedTime: ").appendValue(expected.getMaxBleedTime())
    .appendText("\nMaxTimeSinceDonation: ").appendValue(expected.getMaxTimeSinceDonation()); 
  }

  @Override
  protected boolean matchesSafely(ComponentTypeViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getComponentTypeName(), expected.getComponentTypeName()) &&
        Objects.equals(actual.getComponentTypeCode(), expected.getComponentTypeCode()) &&
        Objects.equals(actual.getDescription(), expected.getDescription()) &&
        Objects.equals(actual.getMaxBleedTime(), expected.getMaxBleedTime()) &&
        Objects.equals(actual.getMaxTimeSinceDonation(), expected.getMaxTimeSinceDonation());
  }
  
  public static ComponentTypeViewModelMatcher hasSameStateAsComponentTypeViewModel(
      ComponentTypeViewModel expected ) {
    return new ComponentTypeViewModelMatcher(expected);
  }

}
