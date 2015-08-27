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
                .appendText("\nDonation Date: ").appendValue(expected.getDonationDate());
    }

    @Override
    public boolean matchesSafely(Donation actual) {
        return Objects.equals(actual.getId(), expected.getId()) &&
                Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
                Objects.equals(actual.getDonationDate(), expected.getDonationDate());
    }
    
    public static DonationMatcher hasSameStateAsDonation(Donation expected) {
        return new DonationMatcher(expected);
    }

}
