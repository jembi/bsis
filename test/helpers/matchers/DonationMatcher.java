package helpers.matchers;

import model.donation.Donation;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

public class DonationMatcher extends TypeSafeMatcher<Donation> {

  private Donation expected;

  private DonationMatcher(Donation expected) {
    this.expected = expected;
  }

  public static DonationMatcher hasSameStateAsDonation(Donation expected) {
    return new DonationMatcher(expected);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donation with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nDonation Date: ").appendValue(expected.getDonationDate())
        .appendText("\nDonor Pulse: ").appendValue(expected.getDonorPulse())
        .appendText("\nHaemoglobin Count: ").appendValue(expected.getHaemoglobinCount())
        .appendText("\nHaemoglobin Level: ").appendValue(expected.getHaemoglobinLevel())
        .appendText("\nBlood Pressure Systolic: ").appendValue(expected.getBloodPressureSystolic())
        .appendText("\nBlood Pressure Diastolic: ").appendValue(expected.getBloodPressureDiastolic())
        .appendText("\nDonor Weight: ").appendValue(expected.getDonorWeight())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nPack Type: ").appendValue(expected.getPackType())
        .appendText("\nBleed Start Time: ").appendValue(expected.getBleedStartTime())
        .appendText("\nBleed End Time: ").appendValue(expected.getBleedEndTime())
        .appendText("\nAdverse Event: ").appendValue(expected.getAdverseEvent());
  }

  @Override
  public boolean matchesSafely(Donation actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getDonorPulse(), expected.getDonorPulse()) &&
        Objects.equals(actual.getHaemoglobinCount(), expected.getHaemoglobinCount()) &&
        Objects.equals(actual.getHaemoglobinLevel(), expected.getHaemoglobinLevel()) &&
        Objects.equals(actual.getBloodPressureSystolic(), expected.getBloodPressureSystolic()) &&
        Objects.equals(actual.getBloodPressureDiastolic(), expected.getBloodPressureDiastolic()) &&
        Objects.equals(actual.getDonorWeight(), expected.getDonorWeight()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getDonationDate(), expected.getDonationDate()) &&
        Objects.equals(actual.getPackType(), expected.getPackType()) &&
        Objects.equals(actual.getBleedStartTime(), expected.getBleedStartTime()) &&
        Objects.equals(actual.getBleedEndTime(), expected.getBleedEndTime()) &&
        Objects.equals(actual.getAdverseEvent(), expected.getAdverseEvent());
  }

}
