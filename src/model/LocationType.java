package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class LocationType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long locationTypeId;
	private String name;
	private String comments;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isDeleted;

	public LocationType(String name, Boolean isDeleted, String comments) {
		this.name = name;
		this.isDeleted = isDeleted;
		this.comments = comments;
	}

	public LocationType() {
	}

	public String getName() {
		return name;
	}

	public Long getLocationTypeId() {
		return locationTypeId;
	}

	public void setLocationTypeId(Long locationTypeId) {
		this.locationTypeId = locationTypeId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void copy(LocationType locationType) {
		this.name = locationType.name;
		this.isDeleted = locationType.isDeleted;
	}

	public Boolean getDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
