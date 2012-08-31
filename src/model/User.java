package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	private String username;
	private String password;
	private String type;
	private String name;
	private String contactNumber;
	private String emailId;
	private String comments;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isDeleted;

	public User(String username, String password, String type, String name,
			String contactNumber, String emailId, Boolean isDeleted,
			String comments) {
		this.username = username;
		this.password = password;
		this.type = type;
		this.name = name;
		this.contactNumber = contactNumber;
		this.emailId = emailId;
		this.isDeleted = isDeleted;
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public User() {
	}

	public void copy(User otherUser) {
		this.username = otherUser.username;
		this.password = otherUser.password;
		this.type = otherUser.type;
		this.name = otherUser.name;
		this.contactNumber = otherUser.contactNumber;
		this.emailId = otherUser.emailId;
		this.isDeleted = otherUser.isDeleted;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return type;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public Boolean getDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
