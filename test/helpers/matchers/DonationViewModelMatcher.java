package helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import viewmodel.DonationViewModel;

public class DonationViewModelMatcher extends TypeSafeMatcher<DonationViewModel> {
    
    private DonationViewModel expected;
    
    public DonationViewModelMatcher(DonationViewModel expected) {
        this.expected = expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("A donation view model with the following state:")
                .appendText("\nDonation: ").appendValue(expected.getDonation())
                .appendText("\nPermissions: ").appendValue(expected.getPermissions())
                .appendText("\nAdverse Event: ").appendValue(expected.getAdverseEvent());
    }

    @Override
    public boolean matchesSafely(DonationViewModel actual) {
        return Objects.equals(actual.getDonation(), expected.getDonation()) &&
                Objects.equals(actual.getPermissions(), expected.getPermissions()) &&
                Objects.equals(actual.getAdverseEvent(), expected.getAdverseEvent());
    }
    
    public static DonationViewModelMatcher hasSameStateAsDonationViewModel(DonationViewModel expected) {
        return new DonationViewModelMatcher(expected);
    }

}
