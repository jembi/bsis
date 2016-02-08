package helpers.matchers;

import java.util.Objects;

import model.donor.Donor;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DonorMatcher extends TypeSafeMatcher<Donor> {

  private Donor expected;

  public DonorMatcher(Donor expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donor with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nDate of First Donation: ").appendValue(expected.getDateOfFirstDonation())
        .appendText("\nDate of Last Donation: ").appendValue(expected.getDateOfLastDonation());
  }

  @Override
  public boolean matchesSafely(Donor actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getDateOfFirstDonation(), expected.getDateOfFirstDonation()) &&
        Objects.equals(actual.getDateOfLastDonation(), expected.getDateOfLastDonation());
  }

  public static DonorMatcher hasSameStateAsDonor(Donor expected) {
    return new DonorMatcher(expected);
  }

}
