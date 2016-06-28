package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.packtype.PackType;

public class PackTypeMatcher extends TypeSafeMatcher<PackType> {

  private PackType expected;

  public PackTypeMatcher(PackType expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A pack type with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nComponentType: ").appendValue(expected.getComponentType())
        .appendText("\nCanPool: ").appendValue(expected.getCanPool())
        .appendText("\nCanSplit: ").appendValue(expected.getCanSplit())
        .appendText("\nIsDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nCountAsDonation: ").appendValue(expected.getCountAsDonation())
        .appendText("\nTestSampleProduced: ").appendValue(expected.getTestSampleProduced())
        .appendText("\nPeriodBetweenDonations: ").appendValue(expected.getPeriodBetweenDonations());
  }

  @Override
  protected boolean matchesSafely(PackType actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getComponentType(), expected.getComponentType())
        && Objects.equals(actual.getCanPool(), expected.getCanPool())
        && Objects.equals(actual.getCanSplit(), expected.getCanSplit())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getCountAsDonation(), expected.getCountAsDonation())
        && Objects.equals(actual.getTestSampleProduced(), expected.getTestSampleProduced())
        && Objects.equals(actual.getPeriodBetweenDonations(), expected.getPeriodBetweenDonations());
  }
  
  public static PackTypeMatcher hasSameStateAsPackType(PackType expected) {
    return new PackTypeMatcher(expected);
  }

}
