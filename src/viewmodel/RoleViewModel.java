package viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.user.Permission;
import model.user.Role;

public class RoleViewModel {

	private Role role;

	public RoleViewModel() {
	}

	public RoleViewModel(Role role) {
		this.role = role;
	}

	public Long getId() {
		return role.getId();
	}

	public String getName() {
		return role.getName();
	}

	public Set<Permission> getPermissions() {
		return role.getPermissions();
	}
	
	public String getDescription() {
		return role.getDescription();
	}
       
}
