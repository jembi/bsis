package model.util;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long locationId;
	private String name;
	private Long type;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isCenter;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isCollectionSite;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isUsageSite;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isMobileSite;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isDeleted;

	private String comments;

	public Location() {
	}

	public Location(String name, Long type, Boolean center,
			Boolean collectionSite, Boolean usageSite, Boolean mobileSite,
			Boolean isDeleted, String comments) {
		this.name = name;
		this.type = type;
		this.isCenter = center;
		this.isCollectionSite = collectionSite;
		this.isUsageSite = usageSite;
		this.isMobileSite = mobileSite;
		this.isDeleted = isDeleted;
		this.comments = comments;
	}

	public void copy(Location location) {
		this.name = location.name;
		this.type = location.type;
		isCenter = location.isCenter;
		isCollectionSite = location.isCollectionSite;
		isUsageSite = location.isUsageSite;
		isMobileSite = location.isMobileSite;
		isDeleted = location.isDeleted;
	}

	public Boolean getCenter() {
		return isCenter;
	}

	public Boolean getCollectionSite() {
		return isCollectionSite;
	}

	public String getComments() {
		return comments;
	}

	public Boolean getDeleted() {
		return isDeleted;
	}

	public Long getLocationId() {
		return locationId;
	}

	public Boolean getMobileSite() {
		return isMobileSite;
	}

	public String getName() {
		return name;
	}

	public Long getType() {
		return type;
	}

	public Boolean getUsageSite() {
		return isUsageSite;
	}

	public void setCenter(Boolean center) {
		isCenter = center;
	}

	public void setCollectionSite(Boolean collectionSite) {
		isCollectionSite = collectionSite;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setMobileSite(Boolean mobileSite) {
		isMobileSite = mobileSite;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public void setUsageSite(Boolean usageSite) {
		isUsageSite = usageSite;
	}
}
