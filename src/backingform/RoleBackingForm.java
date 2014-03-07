package backingform;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import model.user.Permission;
import model.user.Role;
import model.user.User;

public class RoleBackingForm {

	@Valid
	private Role role;
	
	@Valid
	private Set<String> permissionValues;
	
    @Valid
	private String name;
    
      
    public RoleBackingForm() {
		setRole(new Role());
	}

	public RoleBackingForm(Role role) {
		this.setRole(role);
	}

	public boolean equals(Object obj) {
		return getRole().equals(obj);
	}

	public Long getId() {
		return getRole().getId();
	}

	public String getName() {
		return name;
	}

	public List<User> getUsers() {
		return getRole().getUsers();
	}

	public Set<Permission> getPermissions() {
		return getRole().getPermissions();
	}

	public String getDescription() {
		return getRole().getDescription();
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setId(Long id) {
		getRole().setId(id);
	}

	public void setName(String name) {
		this.name=name;
	}

	public void setUsers(List<User> users) {
		getRole().setUsers(users);
	}

	public void setPermissions(Set<Permission> permissions) {
		getRole().setPermissions(permissions);
	}

	public void setDescription(String description) {
		getRole().setDescription(description);
	}
	

	public void setPermissionValues(Set<String> permissionValues) {
		this.permissionValues= permissionValues;
	}

	public Set<String> getPermissionValues() {
		return permissionValues;
	}
	
	
	
	

}
