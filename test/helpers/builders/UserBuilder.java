package helpers.builders;

import model.user.User;

public class UserBuilder {
    
    private String emailId;
    private String username;
    private boolean passwordReset;
    private int id;

    public UserBuilder withId(int id) {
        this.id = id;
        return this;
    }
    
    public UserBuilder withEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }
    
    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
    
    public UserBuilder withPasswordReset() {
        passwordReset = true;
        return this;
    }
    
    public User build() {
        User user = new User();
        user.setId(id);
        user.setEmailId(emailId);
        user.setUsername(username);
        user.setPasswordReset(passwordReset);
        return user;
    }
    
    public static UserBuilder aUser() {
        return new UserBuilder();
    }
}
