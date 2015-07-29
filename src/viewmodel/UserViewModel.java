package viewmodel;

import java.util.ArrayList;
import java.util.List;

import model.user.Role;
import model.user.User;

public class UserViewModel {

  private User user;

  public UserViewModel() {
  }

  public UserViewModel(User user) {
    this.user = user;
  }

  public Integer getId() {
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
  
  public String getEmailId() {
      return user.getEmailId();
  }
  
  public Boolean isPasswordReset() {
      return user.isPasswordReset();
  }

  @Override
  public String toString() {
    return user.getUsername();
  }
  
  public List<RoleViewModel> getRoles() {
	List<RoleViewModel> roleViewModels = new ArrayList<RoleViewModel>();
	for (Role role : user.getRoles()) {
		roleViewModels.add(new RoleViewModel(role));
	}
	return roleViewModels;
  }
}
