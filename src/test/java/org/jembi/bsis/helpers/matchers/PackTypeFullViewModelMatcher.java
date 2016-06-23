package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.PackTypeViewFullModel;

public class PackTypeFullViewModelMatcher extends TypeSafeMatcher<PackTypeViewFullModel> {

  private PackTypeViewFullModel expected;

  public PackTypeFullViewModelMatcher(PackTypeViewFullModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A test batch view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nTest samples produced: ").appendValue(expected.getTestSampleProduced())
        .appendText("\nPeriod between donations: ").appendValue(expected.getPeriodBetweenDonations())
        .appendText("\nIs deleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nCount as donation: ").appendValue(expected.getCountAsDonation())
        .appendText("\nComponent type: ").appendValue(expected.getComponentType())
        .appendText("\nPack type: ").appendValue(expected.getPackType());

  }

  @Override
  public boolean matchesSafely(PackTypeViewFullModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getTestSampleProduced(), expected.getTestSampleProduced()) &&
        Objects.equals(actual.getPeriodBetweenDonations(), expected.getPeriodBetweenDonations()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getCountAsDonation(), expected.getCountAsDonation()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getPackType(), expected.getPackType());
  }

  public static PackTypeFullViewModelMatcher hasSameStateAsPackTypeViewFullModel(PackTypeViewFullModel expected) {
    return new PackTypeFullViewModelMatcher(expected);
  }

}
