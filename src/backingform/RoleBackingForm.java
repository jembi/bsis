package backingform;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import model.user.Permission;
import model.user.Role;
import model.user.User;

public class RoleBackingForm {

    @Valid
    @JsonIgnore
    private Role role;

    public RoleBackingForm() {
        setRole(new Role());
    }

    public RoleBackingForm(Role role) {
        this.setRole(role);
    }

    public boolean equals(Object obj) {
        return role.equals(obj);
    }

    public Long getId() {
        return role.getId();
    }

    public String getName() {
        return role.getName();
    }

    @JsonIgnore
    public List<User> getUsers() {
        return role.getUsers();
    }

    public Set<Permission> getPermissions() {
        return role.getPermissions();
    }

    public String getDescription() {
        return role.getDescription();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(Long id) {
        role.setId(id);
    }

    public void setName(String name) {
        role.setName(name);
    }

    public void setUsers(List<User> users) {
        role.setUsers(users);
    }

    public void setPermissions(Set<Permission> permissions) {
        role.setPermissions(permissions);
    }

    public void setDescription(String description) {
        role.setDescription(description);
    }

}
