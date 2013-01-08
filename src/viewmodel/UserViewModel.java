package viewmodel;

import model.user.User;

public class UserViewModel {

  private User user;

  public UserViewModel(User user) {
    this.user = user;
  }

  public Long getId() {
    return user.getId();
  }

  public String getUsername() {
    return user.getUsername();
  }

  public String getFirstName() {
    return user.getFirstName();
  }

  public String getLastName() {
    return user.getLastName();
  }

  public Boolean getIsAdmin() {
    return user.getIsAdmin();
  }
}
