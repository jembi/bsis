package viewmodel;

import java.util.ArrayList;
import java.util.List;

import model.user.Role;
import model.user.User;

public class UserViewModel {

  private User user;
  private String userRole="";

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

  @Override
  public String toString() {
    return user.getUsername();
  }
  
  public List<Role> getRoles() {
    return user.getRoles();
  }

	/**
	 * @return the userRole
	 */
	public List<String> getUserRoles() {
		List<Role> roles=user.getRoles();
                List<String> userRoles = new ArrayList<String>();
		if(roles.size() > 0){
			for(Role r:roles){
                                userRoles.add(r.getId()+"");
			}
			
		}
		return userRoles;
	}
}
