package helpers.builders;

import model.user.User;

public class UserBuilder {
    
    private String emailId;
    private String username;
    
    public UserBuilder withEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }
    
    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
    
    public User build() {
        User user = new User();
        user.setEmailId(emailId);
        user.setUsername(username);
        return user;
    }
    
    public static UserBuilder aUser() {
        return new UserBuilder();
    }
}
