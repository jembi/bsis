package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.componentbatch.ComponentBatch;

public class ComponentBatchMatcher extends TypeSafeMatcher<ComponentBatch> {

  private ComponentBatch expected;

  public ComponentBatchMatcher(ComponentBatch expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A Component Batch with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nLocation: ").appendValue(expected.getLocation())
        .appendText("\nCollection date: ").appendValue(expected.getCollectionDate())
        .appendText("\nDelivery date: ").appendValue(expected.getDeliveryDate())
        .appendText("\nBlood Transport Boxes: ").appendValue(expected.getBloodTransportBoxes())
        .appendText("\nComponents: ").appendValue(expected.getComponents())
        .appendText("\nDonationBatch: ").appendValue(expected.getDonationBatch())
        .appendText("\nIsDeleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  public boolean matchesSafely(ComponentBatch actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getLocation(), expected.getLocation()) &&
        Objects.equals(actual.getCollectionDate(), expected.getCollectionDate()) &&
        Objects.equals(actual.getDeliveryDate(), expected.getDeliveryDate()) &&
        Objects.equals(actual.getBloodTransportBoxes(), expected.getBloodTransportBoxes()) &&
        Objects.equals(actual.getComponents(), expected.getComponents()) &&
        Objects.equals(actual.getDonationBatch(), expected.getDonationBatch()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }

  public static ComponentBatchMatcher hasSameStateAsComponentBatch(ComponentBatch expected) {
    return new ComponentBatchMatcher(expected);
  }
}