package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserBackingForm {

  private UUID id;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String emailId;

  private Boolean isStaff;
  private Boolean isActive;
  private Boolean isAdmin;
  private Boolean isDeleted;
  private String notes;
  private Date lastLogin;

  private boolean modifyPassword;
  private String userConfirmPassword;
  private String currentPassword;
  private String roleAdmin;
  private String roleDonorLab;
  private String roleTestLab;
  private String roleUser;
  @JsonIgnore
  private String passwordReset;
  private Boolean isPasswordReset = Boolean.FALSE;
  
  private List<RoleBackingForm> roles;

  /**
   * @return the userConfirmPassword
   */
  public String getConfirmPassword() {
    return userConfirmPassword;
  }

  /**
   * @return the currentPassword
   */
  public String getCurrentPassword() {
    return currentPassword;
  }

  public String getEmailId() {
    return emailId;
  }

  public String getFirstName() {
    return firstName;
  }

  public UUID getId() {
    return id;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public Boolean getIsAdmin() {
    return isAdmin;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public Boolean getIsStaff() {
    return isStaff;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public String getLastName() {
    return lastName;
  }

  public String getNotes() {
    return notes;
  }

  public String getPassword() {
    return password;
  }

  /**
   * @return the roleAdmin
   */
  public String getRoleAdmin() {
    return roleAdmin;
  }

  /**
   * @return the roleDonorLab
   */
  public String getRoleDonorLab() {
    return roleDonorLab;
  }

  /**
   * @return the roleTestLab
   */
  public String getRoleTestLab() {
    return roleTestLab;
  }

  /**
   * @return the roleUser
   */
  public String getRoleUser() {
    return roleUser;
  }

  public String getUserConfirmPassword() {
    return userConfirmPassword;
  }

  public String getUsername() {
    return username;
  }

  public boolean isModifyPassword() {
    return modifyPassword;
  }

  public Boolean isPasswordReset() {
    return isPasswordReset;
  }

  /**
   * @param userConfirmPassword the userConfirPassword to set
   */
  public void setConfirmPassword(String userConfirmPassword) {
    this.userConfirmPassword = userConfirmPassword;
  }

  /**
   * @param currentPassword the currentPassword to set
   */
  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setIsStaff(Boolean isStaff) {
    this.isStaff = isStaff;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setModifyPassword(boolean modifyPassword) {
    this.modifyPassword = modifyPassword;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @param roleAdmin the roleAdmin to set
   */
  public void setRoleAdmin(String roleAdmin) {
    this.roleAdmin = roleAdmin;
  }

  /**
   * @param roleDonorLab the roleDonorLab to set
   */
  public void setRoleDonorLab(String roleDonorLab) {
    this.roleDonorLab = roleDonorLab;
  }

  /**
   * @param roleTestLab the roleTestLab to set
   */
  public void setRoleTestLab(String roleTestLab) {
    this.roleTestLab = roleTestLab;
  }

  /**
   * @param roleUser the roleUser to set
   */
  public void setRoleUser(String roleUser) {
    this.roleUser = roleUser;
  }

  public void setUserConfirmPassword(String userConfirmPassword) {
    this.userConfirmPassword = userConfirmPassword;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<RoleBackingForm> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleBackingForm> roles) {
    this.roles = roles;
  }

  public void setIsPasswordReset(Boolean isPasswordReset) {
    this.isPasswordReset = isPasswordReset;
  }

}
