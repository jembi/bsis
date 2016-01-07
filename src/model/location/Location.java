package model.location;

import javax.persistence.Entity;
import javax.persistence.Lob;

import model.BaseEntity;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Entity representing a place where Donations can be made and/or used.
 */
@Entity
@Audited
public class Location extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @NotBlank
  private String name;

  private Boolean isUsageSite;

  private Boolean isMobileSite;

  private Boolean isVenue;

  private Boolean isDeleted;

  @Lob
  private String notes;

  public Location() {
  }

  public void copy(Location location) {
    this.name = location.name;
    this.isVenue = location.isVenue;
    this.isMobileSite = location.isMobileSite;
    this.isUsageSite = location.isUsageSite;
    this.isDeleted = location.isDeleted;
    this.notes = location.notes;
  }

  public String getName() {
    return name;
  }

  public Boolean getIsMobileSite() {
    return isMobileSite;
  }

  public Boolean getIsUsageSite() {
    return isUsageSite;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setIsMobileSite(Boolean mobileSite) {
    isMobileSite = mobileSite;
  }

  public void setIsUsageSite(Boolean usageSite) {
    isUsageSite = usageSite;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Boolean getIsVenue() {
    return isVenue;
  }

  public void setIsVenue(Boolean isVenue) {
    this.isVenue = isVenue;
  }
}
