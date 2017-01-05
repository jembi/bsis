package org.jembi.bsis.helpers.matchers;

import java.util.Objects;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.backingform.ComponentTypeBackingForm;

public class ComponentTypeBackingFormMatcher extends TypeSafeMatcher<ComponentTypeBackingForm> {
  
  private ComponentTypeBackingForm expected;
  
  public ComponentTypeBackingFormMatcher(ComponentTypeBackingForm expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A ComponentTypeBackingForm with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nComponentTypeName: ").appendValue(expected.getComponentTypeName())
        .appendText("\nComponentTypeCode: ").appendValue(expected.getComponentTypeCode())
        .appendText("\nExpiresAfter: ").appendValue(expected.getExpiresAfter())
        .appendText("\nMaxBleedTime: ").appendValue(expected.getMaxBleedTime())
        .appendText("\nMaxTimeSinceDonation: ").appendValue(expected.getMaxTimeSinceDonation())
        .appendText("\nExpiresAfterUnits: ").appendValue(expected.getExpiresAfterUnits())
        .appendText("\nHasBloodGroup: ").appendValue(expected.getHasBloodGroup())
        .appendText("\nDescription: ").appendValue(expected.getDescription())
        .appendText("\nIsDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nLowStorageTemperature: ").appendValue(expected.getLowStorageTemperature())
        .appendText("\nHighStorageTemperature: ").appendValue(expected.getHighStorageTemperature())
        .appendText("\nLowTransportTemperature: ").appendValue(expected.getLowTransportTemperature())
        .appendText("\nHighTransportTemperature: ").appendValue(expected.getHighTransportTemperature())
        .appendText("\nPreparationInfo: ").appendValue(expected.getPreparationInfo())
        .appendText("\nTransportInfo: ").appendValue(expected.getTransportInfo())
        .appendText("\nStorageInfo: ").appendValue(expected.getStorageInfo())
        .appendText("\nCanBeIssued: ").appendValue(expected.getCanBeIssued())
        .appendText("\nContainsPlasma: ").appendValue(expected.getContainsPlasma())
        .appendText("\nProducedComponentTypeCombinations: ").appendValue(expected.getProducedComponentTypeCombinations())
        .appendText("\nGravity: ").appendValue(expected.getGravity());
  }

  @Override
  public boolean matchesSafely(ComponentTypeBackingForm actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getComponentTypeName(), expected.getComponentTypeName()) &&
        Objects.equals(actual.getComponentTypeCode(), expected.getComponentTypeCode()) &&
        Objects.equals(actual.getExpiresAfter(), expected.getExpiresAfter()) &&
        Objects.equals(actual.getMaxBleedTime(), expected.getMaxBleedTime()) &&
        Objects.equals(actual.getMaxTimeSinceDonation(), expected.getMaxTimeSinceDonation()) &&
        Objects.equals(actual.getExpiresAfterUnits(), expected.getExpiresAfterUnits()) &&
        Objects.equals(actual.getHasBloodGroup(), expected.getHasBloodGroup()) &&
        Objects.equals(actual.getDescription(), expected.getDescription()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getLowStorageTemperature(), expected.getLowStorageTemperature()) &&
        Objects.equals(actual.getHighStorageTemperature(), expected.getHighStorageTemperature()) &&
        Objects.equals(actual.getLowTransportTemperature(), expected.getLowTransportTemperature()) &&
        Objects.equals(actual.getHighTransportTemperature(), expected.getHighTransportTemperature()) &&
        Objects.equals(actual.getPreparationInfo(), expected.getPreparationInfo()) &&
        Objects.equals(actual.getTransportInfo(), expected.getTransportInfo()) &&
        Objects.equals(actual.getStorageInfo(), expected.getStorageInfo()) &&
        Objects.equals(actual.getCanBeIssued(), expected.getCanBeIssued()) &&
        Objects.equals(actual.getProducedComponentTypeCombinations(), expected.getProducedComponentTypeCombinations()) &&
        Objects.equals(actual.getContainsPlasma(), expected.getContainsPlasma() &&
        Objects.equals(actual.getGravity(), expected.getGravity()));
  }

  public static ComponentTypeBackingFormMatcher hasSameStateAsComponentTypeBackingForm(ComponentTypeBackingForm expected) {
    return new ComponentTypeBackingFormMatcher(expected);
  }
}
