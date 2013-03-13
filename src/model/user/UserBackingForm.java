package model.user;

import java.util.Date;

import javax.validation.Valid;

public class UserBackingForm {

  @Valid
  private User user;

  private boolean modifyPassword;

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

  public void setModifyPassword(boolean modifyPassword) {
    this.modifyPassword = modifyPassword;
  }

  public boolean getModifyPassword() {
    return modifyPassword;
  }
}
