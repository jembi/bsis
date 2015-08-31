package helpers.matchers;

import java.util.Objects;

import model.donation.Donation;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DonationMatcher extends TypeSafeMatcher<Donation> {
    
    private Donation expected;
    
    public DonationMatcher(Donation expected) {
        this.expected = expected;
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
                .appendText("\nNotes: ").appendValue(expected.getNotes());
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
                Objects.equals(actual.getDonationDate(), expected.getDonationDate());
    }
    
    public static DonationMatcher hasSameStateAsDonation(Donation expected) {
        return new DonationMatcher(expected);
    }

}
