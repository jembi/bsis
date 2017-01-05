package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;

public class ComponentTypeFullViewModelMatcher extends TypeSafeMatcher<ComponentTypeFullViewModel> {

  private ComponentTypeFullViewModel expected;
  
  public ComponentTypeFullViewModelMatcher(ComponentTypeFullViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A componentTypeFullViewModel with the following state:")
    .appendText("\nId: ").appendValue(expected.getId())
    .appendText("\nHas blood group: ").appendValue(expected.isHasBloodGroup())
    .appendText("\nLow storage temperature: ").appendValue(expected.getLowStorageTemperature())
    .appendText("\nHigh storage temperature: ").appendValue(expected.getHighStorageTemperature())
    .appendText("\nPreparation Info: ").appendValue(expected.getPreparationInfo())
    .appendText("\nStorage Info: ").appendValue(expected.getStorageInfo())
    .appendText("\nExpires after: ").appendValue(expected.getExpiresAfter())
    .appendText("\nCan be issued: ").appendValue(expected.getCanBeIssued())
    .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
    .appendText("\nExpires after units: ").appendValue(expected.getExpiresAfterUnits())
    .appendText("\nComponent name: ").appendValue(expected.getComponentTypeName())
    .appendText("\nComponent code: ").appendValue(expected.getComponentTypeCode())
    .appendText("\nDescription: ").appendValue(expected.getDescription())
    .appendText("\nContains Plasma: ").appendValue(expected.getContainsPlasma())
    .appendText("\nGravity: ").appendValue(expected.getGravity());
  }

  @Override
  protected boolean matchesSafely(ComponentTypeFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.isHasBloodGroup(), expected.isHasBloodGroup()) &&
        Objects.equals(actual.getLowStorageTemperature(), expected.getLowStorageTemperature()) &&
        Objects.equals(actual.getHighStorageTemperature(), expected.getHighStorageTemperature()) &&
        Objects.equals(actual.getPreparationInfo(), expected.getPreparationInfo()) &&
        Objects.equals(actual.getStorageInfo(), expected.getStorageInfo()) &&
        Objects.equals(actual.getExpiresAfter(), expected.getExpiresAfter()) &&
        Objects.equals(actual.getCanBeIssued(), expected.getCanBeIssued()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getStorageInfo(), expected.getStorageInfo()) &&
        Objects.equals(actual.getExpiresAfterUnits(), expected.getExpiresAfterUnits()) &&
        Objects.equals(actual.getComponentTypeName(), expected.getComponentTypeName()) &&
        Objects.equals(actual.getComponentTypeCode(), expected.getComponentTypeCode()) &&
        Objects.equals(actual.getDescription(), expected.getDescription()) &&
        Objects.equals(actual.getContainsPlasma(), expected.getContainsPlasma()) &&
        Objects.equals(actual.getGravity(), expected.getGravity());
  }
  
  public static ComponentTypeFullViewModelMatcher hasSameStateAsComponentTypeFullViewModel(ComponentTypeFullViewModel expected) {
    return new ComponentTypeFullViewModelMatcher(expected);
  }

}
