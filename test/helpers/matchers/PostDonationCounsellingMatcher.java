package helpers.matchers;

import java.util.Objects;

import model.counselling.PostDonationCounselling;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class PostDonationCounsellingMatcher extends TypeSafeMatcher<PostDonationCounselling> {
    
    private PostDonationCounselling expected;
    
    public PostDonationCounsellingMatcher(PostDonationCounselling expected) {
        this.expected = expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("A post donation counselling with the following state:")
                .appendText("\nId: ").appendValue(expected.getId())
                .appendText("\nFlagged for counselling: ").appendValue(expected.isFlaggedForCounselling())
                .appendText("\nDonation: ").appendValue(expected.getDonation())
                .appendText("\nCounselling date: ").appendValue(expected.getCounsellingDate())
                .appendText("\nCounselling status: ").appendValue(expected.getCounsellingStatus());
    }

    @Override
    public boolean matchesSafely(PostDonationCounselling actual) {
        return Objects.equals(actual.getId(), expected.getId()) &&
                actual.isFlaggedForCounselling() == expected.isFlaggedForCounselling() &&
                Objects.equals(actual.getDonation(), expected.getDonation()) &&
                Objects.equals(actual.getCounsellingDate(), expected.getCounsellingDate()) &&
                Objects.equals(actual.getCounsellingStatus(), expected.getCounsellingStatus());
    }
    
    public static PostDonationCounsellingMatcher hasSameStateAsPostDonationCounselling(PostDonationCounselling expected) {
        return new PostDonationCounsellingMatcher(expected);
    }

}
