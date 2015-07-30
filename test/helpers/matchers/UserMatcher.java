package helpers.matchers;

import java.util.Objects;

import model.user.User;

import org.mockito.ArgumentMatcher;

public class UserMatcher extends ArgumentMatcher<User> {
    
    private User expected;
    
    public UserMatcher(User expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object object) {
        if (!(object instanceof User)) {
            return false;
        }

        User actual = (User) object;

        return Objects.equals(actual.getEmailId(), expected.getEmailId())
                && Objects.equals(actual.isPasswordReset(), expected.isPasswordReset());
    }
    
    public static UserMatcher hasSameStateAsUser(User expected) {
        return new UserMatcher(expected);
    }

}
