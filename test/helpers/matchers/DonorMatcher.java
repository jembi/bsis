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
                .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
                .appendText("\nNotes: ").appendValue(expected.getNotes());
    }

    @Override
    public boolean matchesSafely(Donor actual) {
        return actual.getIsDeleted() == expected.getIsDeleted() &&
                Objects.equals(actual.getNotes(), expected.getNotes());
    }
    
    public static DonorMatcher hasSameStateAsDonor(Donor expected) {
        return new DonorMatcher(expected);
    }

}
