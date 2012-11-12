package model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class User implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
  private Long id;

  @Length(min=1, max=30)
  @NotBlank
  @Column(length=30, unique=true, nullable=false)
  private String username;

  @Length(min=1, max=255)
  @NotBlank
  @Column(length=255, nullable=false)
  private String password;

  @NotBlank
  @Length(min=1, max=30)
  @Column(length=30, nullable=false)
  private String firstName;

  @Length(max=30)
  @Column(length=30)
  private String middleName;

  @Length(max=30)
  @Column(length=30)
  private String lastName;

  @Length(max=255)
  @Column(length=255)
  private String emailId;

  private Boolean isStaff;
  private Boolean isActive;
  private Boolean isAdmin;

  private RowModificationTracker modificationTracker;

  private Boolean isDeleted;

  @Lob
  private String notes;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastLogin;

  public User() {
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmailId() {
    return emailId;
  }

  public Boolean getIsStaff() {
    return isStaff;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public Boolean getIsAdmin() {
    return isAdmin;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public void setIsStaff(Boolean isStaff) {
    this.isStaff = isStaff;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public void setIsSuperuser(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @Override
  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  @Override
  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  @Override
  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  @Override
  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  @Override
  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  @Override
  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  @Override
  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  @Override
  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }
}