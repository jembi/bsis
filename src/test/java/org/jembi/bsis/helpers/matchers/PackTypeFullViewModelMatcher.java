package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;

public class PackTypeFullViewModelMatcher extends TypeSafeMatcher<PackTypeFullViewModel> {

  private PackTypeFullViewModel expected;

  public PackTypeFullViewModelMatcher(PackTypeFullViewModel expected) {
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
        .appendText("\nPack type: ").appendValue(expected.getPackType())
        .appendText("\nMin weight: ").appendValue(expected.getMinWeight())
        .appendText("\nMax weight: ").appendValue(expected.getMaxWeight())
        .appendText("\nLow volume weight: ").appendValue(expected.getLowVolumeWeight());

  }

  @Override
  public boolean matchesSafely(PackTypeFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getTestSampleProduced(), expected.getTestSampleProduced()) &&
        Objects.equals(actual.getPeriodBetweenDonations(), expected.getPeriodBetweenDonations()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getCountAsDonation(), expected.getCountAsDonation()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getPackType(), expected.getPackType()) &&
        Objects.equals(actual.getMinWeight(), expected.getMinWeight()) &&
        Objects.equals(actual.getMaxWeight(), expected.getMaxWeight()) &&
        Objects.equals(actual.getLowVolumeWeight(), expected.getLowVolumeWeight());
  }

  public static PackTypeFullViewModelMatcher hasSameStateAsPackTypeViewFullModel(PackTypeFullViewModel expected) {
    return new PackTypeFullViewModelMatcher(expected);
  }

}
