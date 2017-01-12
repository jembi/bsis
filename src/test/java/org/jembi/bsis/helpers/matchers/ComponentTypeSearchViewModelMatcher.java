package org.jembi.bsis.helpers.matchers;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.ComponentTypeSearchViewModel;

public class ComponentTypeSearchViewModelMatcher extends TypeSafeMatcher<ComponentTypeSearchViewModel> {
  
  private ComponentTypeSearchViewModel expected;
  
  public ComponentTypeSearchViewModelMatcher(ComponentTypeSearchViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A ComponentTypeSearchViewModel with the following state:")
    .appendText("\nId: ").appendValue(expected.getId())
    .appendText("\nExpires after: ").appendValue(expected.getExpiresAfter())
    .appendText("\nIs deleted: ").appendValue(expected.getIsDeleted())
    .appendText("\nExpires after units: ").appendValue(expected.getExpiresAfterUnits())
    .appendText("\nContains plasma: ").appendValue(expected.getContainsPlasma())
    .appendText("\nComponent type name: ").appendValue(expected.getExpiresAfterUnits())
    .appendText("\nComponent type code: ").appendValue(expected.getComponentTypeCode())
    .appendText("\nDescription: ").appendValue(expected.getDescription());  
  }

  @Override
  protected boolean matchesSafely(ComponentTypeSearchViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getExpiresAfter(), expected.getExpiresAfter()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getExpiresAfterUnits(), expected.getExpiresAfterUnits()) &&
        Objects.equals(actual.getContainsPlasma(), expected.getContainsPlasma()) &&
        Objects.equals(actual.getComponentTypeName(), expected.getComponentTypeName()) &&
        Objects.equals(actual.getComponentTypeCode(), expected.getComponentTypeCode()) &&
        Objects.equals(actual.getDescription(), expected.getDescription());
  }
  
  public static ComponentTypeSearchViewModelMatcher hasSameStateAsComponentTypeSearchViewModel(
      ComponentTypeSearchViewModel expected) {
    return new ComponentTypeSearchViewModelMatcher(expected);
  }

}
