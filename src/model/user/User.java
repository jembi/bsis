package model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

@Entity
@Audited
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=30, unique=true, nullable=false)
  private String username;

  @Column(length=255, nullable=false)
  private String password;
  
  @Column
  private Boolean passwordReset = Boolean.FALSE;

  @Column(length=15, nullable=false)
  private String firstName;
  @Column(length=15)
  private String lastName;

  @Length(max=255)
  @Column(length=255)
  private String emailId;

  private Boolean isStaff;
  private Boolean isActive;
  private Boolean isAdmin;

  private Boolean isDeleted;

  @ManyToMany(fetch=FetchType.EAGER)
  private List<Role> roles;
  
  @ManyToMany(mappedBy="users")
  private List<UserRole> userRole;
  
  @Lob
  private String notes;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastLogin;

  public User() {
  }

  public Integer getId() {
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

  public void setId(Integer id) {
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
    this.setIsAdmin(isAdmin);
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

  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public void copy(User user) {
    this.username = user.getUsername();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.emailId = user.getEmailId();
    this.isAdmin = user.getIsAdmin();
    this.isActive = user.getIsActive();
    this.roles = user.getRoles();
    this.passwordReset = user.isPasswordReset();
  }

  @Override
  public String toString() {
    return this.username;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

	/**
	 * @return the userRole
	 */
	public List<UserRole> getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(List<UserRole> userRole) {
		this.userRole = userRole;
	}

    public Boolean isPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(Boolean passwordReset) {
        this.passwordReset = passwordReset;
    }
  
}