package model.location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class LocationType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String notes;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isDeleted;

	public LocationType(String name, Boolean isDeleted, String notes) {
		this.name = name;
		this.isDeleted = isDeleted;
		this.notes = notes;
	}

	public LocationType() {
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
