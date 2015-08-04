package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import model.user.Role;
import model.user.User;

public class UserBackingForm {

    @Valid
    @JsonIgnore
    private User user;

    private boolean modifyPassword;
    private String userConfirmPassword;
    private String currentPassword;
    private String roleAdmin;
    private String roleDonorLab;
    private String roleTestLab;
    private String roleUser;
    @JsonIgnore
    private String passwordReset;

    public UserBackingForm() {
        setUser(new User());
    }

    public UserBackingForm(User user) {
        this.setUser(user);
    }

    public boolean equals(Object obj) {
        return getUser().equals(obj);
    }

    public Integer getId() {
        return getUser().getId();
    }

    public String getUsername() {
        return getUser().getUsername();
    }

    public String getPassword() {
        return getUser().getPassword();
    }
    
    public Boolean isPasswordReset() {
        return getUser().isPasswordReset();
    }

    public String getFirstName() {
        return getUser().getFirstName();
    }

    public String getLastName() {
        return getUser().getLastName();
    }

    public String getEmailId() {
        return getUser().getEmailId();
    }

    public Boolean getIsStaff() {
        return getUser().getIsStaff();
    }

    public Boolean getIsActive() {
        return getUser().getIsActive();
    }

    public Boolean getIsAdmin() {
        return getUser().getIsAdmin();
    }

    public Date getLastLogin() {
        return getUser().getLastLogin();
    }

    public String getNotes() {
        return getUser().getNotes();
    }

    public Boolean getIsDeleted() {
        return getUser().getIsDeleted();
    }

    public int hashCode() {
        return getUser().hashCode();
    }

    public void setId(Integer id) {
        getUser().setId(id);
    }

    public void setUsername(String username) {
        getUser().setUsername(username);
    }

    public void setPassword(String password) {
        getUser().setPassword(password);
    }

    public void setFirstName(String firstName) {
        getUser().setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        getUser().setLastName(lastName);
    }

    public void setEmailId(String emailId) {
        getUser().setEmailId(emailId);
    }

    public void setIsStaff(Boolean isStaff) {
        getUser().setIsStaff(isStaff);
    }

    public void setIsActive(Boolean isActive) {
        getUser().setIsActive(isActive);
    }

    public void setIsSuperuser(Boolean isAdmin) {
        getUser().setIsSuperuser(isAdmin);
    }

    public void setLastLogin(Date lastLogin) {
        getUser().setLastLogin(lastLogin);
    }

    public void setNotes(String notes) {
        getUser().setNotes(notes);
    }

    public void setIsDeleted(Boolean isDeleted) {
        getUser().setIsDeleted(isDeleted);
    }

    public void setIsAdmin(Boolean isAdmin) {
        getUser().setIsAdmin(isAdmin);
    }

    public String toString() {
        return getUser().toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isModifyPassword() {
        return modifyPassword;
    }

    public void setModifyPassword(boolean modifyPassword) {
        this.modifyPassword = modifyPassword;
    }

    public List<Role> getRoles() {
        return user.getRoles();
    }
    
    public void setRoles(List<Role> roles){
        user.setRoles(roles);
    }

    /**
     * @return the userConfirmPassword
     */
    public String getConfirmPassword() {
        return userConfirmPassword;
    }

    /**
     * @param userConfirmPassword the userConfirPassword to set
     */
    public void setConfirmPassword(String userConfirmPassword) {
        this.userConfirmPassword = userConfirmPassword;
    }

    /**
     * @return the currentPassword
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * @param currentPassword the currentPassword to set
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * @return the roleAdmin
     */
    public String getRoleAdmin() {
        return roleAdmin;
    }

    /**
     * @param roleAdmin the roleAdmin to set
     */
    public void setRoleAdmin(String roleAdmin) {
        this.roleAdmin = roleAdmin;
    }

    /**
     * @return the roleDonorLab
     */
    public String getRoleDonorLab() {
        return roleDonorLab;
    }

    /**
     * @param roleDonorLab the roleDonorLab to set
     */
    public void setRoleDonorLab(String roleDonorLab) {
        this.roleDonorLab = roleDonorLab;
    }

    /**
     * @return the roleTestLab
     */
    public String getRoleTestLab() {
        return roleTestLab;
    }

    /**
     * @param roleTestLab the roleTestLab to set
     */
    public void setRoleTestLab(String roleTestLab) {
        this.roleTestLab = roleTestLab;
    }

    /**
     * @return the roleUser
     */
    public String getRoleUser() {
        return roleUser;
    }

    /**
     * @param roleUser the roleUser to set
     */
    public void setRoleUser(String roleUser) {
        this.roleUser = roleUser;
    }
    

}
