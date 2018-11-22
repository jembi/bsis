package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;

public class TestBatchFullViewModelMatcher extends AbstractTypeSafeMatcher<TestBatchFullViewModel> {

  public TestBatchFullViewModelMatcher(TestBatchFullViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, TestBatchFullViewModel model) {
    description.appendText("A test batch view model with the following state:")
      .appendText("\nId: ").appendValue(model.getId())
      .appendText("\nStatus: ").appendValue(model.getStatus())
      .appendText("\nBatch number: ").appendValue(model.getBatchNumber())
      .appendText("\nTest batch date: ").appendValue(model.getTestBatchDate())
      .appendText("\nLast updated date: ").appendValue(model.getLastUpdated())
      .appendText("\nNotes: ").appendValue(model.getNotes())
      .appendText("\nDonations: ").appendValue(model.getDonations())
      .appendText("\nPermissions: ").appendValue(model.getPermissions())
      .appendText("\nReady for release count: ").appendValue(model.getReadyForReleaseCount())
      .appendText("\nDins Without Test Samples: ").appendValue(model.getDinsWithoutTestSamples())
      .appendText("\nDins In Other Test Batches: ").appendValue(model.getDinsInOtherTestBatches())
      .appendText("\nDins In Open Donationan Batch: ").appendValue(model.getDinsInOpenDonationanBatch());
  }

  @Override
  public boolean matchesSafely(TestBatchFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getBatchNumber(), expected.getBatchNumber()) &&
        Objects.equals(actual.getTestBatchDate(), expected.getTestBatchDate()) &&
        Objects.equals(actual.getLastUpdated(), expected.getLastUpdated()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getDonations(), expected.getDonations()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions()) &&
        Objects.equals(actual.getReadyForReleaseCount(), expected.getReadyForReleaseCount()) && 
        Objects.equals(actual.getDinsWithoutTestSamples(), expected.getDinsWithoutTestSamples()) &&
        Objects.equals(actual.getDinsInOtherTestBatches(), expected.getDinsInOtherTestBatches()) &&
        Objects.equals(actual.getDinsInOpenDonationanBatch(), expected.getDinsInOpenDonationanBatch());
  }

  public static TestBatchFullViewModelMatcher hasSameStateAsTestBatchFullViewModel(TestBatchFullViewModel expected) {
    return new TestBatchFullViewModelMatcher(expected);
  }
}