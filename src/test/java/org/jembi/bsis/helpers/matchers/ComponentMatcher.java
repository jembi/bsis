package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.component.Component;

public class ComponentMatcher extends TypeSafeMatcher<Component> {

  private Component expected;

  public ComponentMatcher(Component expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A Component with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nComponentCode: ").appendValue(expected.getComponentCode())
        .appendText("\nComponentType: ").appendValue(expected.getComponentType())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nInventoryStatus: ").appendValue(expected.getInventoryStatus())
        .appendText("\nLocation: ").appendValue(expected.getLocation())
        .appendText("\nCreatedOn: ").appendValue(expected.getCreatedOn())
        .appendText("\nExpiresOn: ").appendValue(expected.getExpiresOn())
        .appendText("\nParentComponent: ").appendValue(expected.getParentComponent())
        .appendText("\nDonation: ").appendValue(expected.getDonation())
        .appendText("\nWeight: ").appendValue(expected.getWeight())
        .appendText("\nIsDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nStatusChanges: ").appendValue(expected.getStatusChanges())
        .appendText("\nComponentBatch: ").appendValue(expected.getComponentBatch())
        .appendText("\nProcessedOn: ").appendValue(expected.getProcessedOn());
  }

  @Override
  public boolean matchesSafely(Component actual) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getComponentCode(), expected.getComponentCode()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getInventoryStatus(), expected.getInventoryStatus()) &&
        Objects.equals(actual.getLocation(), expected.getLocation()) &&
        (Objects.equals(actual.getCreatedOn(), expected.getCreatedOn()) || Objects.equals(sdf.format(actual.getCreatedOn()), sdf.format(expected.getCreatedOn()))) &&
        (Objects.equals(actual.getExpiresOn(), expected.getExpiresOn()) || Objects.equals(sdf.format(actual.getExpiresOn()), sdf.format(expected.getExpiresOn()))) &&
        (Objects.equals(actual.getIssuedOn(), expected.getIssuedOn()) || Objects.equals(sdf.format(actual.getIssuedOn()), sdf.format(expected.getIssuedOn()))) &&
        (Objects.equals(actual.getDiscardedOn(), expected.getDiscardedOn()) || Objects.equals(sdf.format(actual.getDiscardedOn()), sdf.format(expected.getDiscardedOn()))) &&
        Objects.equals(actual.getParentComponent(), expected.getParentComponent()) &&
        Objects.equals(actual.getDonation(), expected.getDonation()) &&
        Objects.equals(actual.getWeight(), expected.getWeight()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getStatusChanges(), expected.getStatusChanges()) &&
        Objects.equals(actual.getComponentBatch(), expected.getComponentBatch()) &&
        Objects.equals(actual.getProcessedOn(), expected.getProcessedOn());
  }

  public static ComponentMatcher hasSameStateAsComponent(Component expected) {
    return new ComponentMatcher(expected);
  }

}
