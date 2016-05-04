package model.location;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;

import model.BaseEntity;
import repository.LocationNamedQueryConstants;

/**
 * Entity representing a place where Donations can be made and/or used.
 */
@NamedQueries({
  @NamedQuery(name = LocationNamedQueryConstants.NAME_GET_ALL_LOCATIONS,
      query = LocationNamedQueryConstants.QUERY_GET_ALL_LOCATIONS),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_COUNT_LOCATION_WITH_ID,
      query = LocationNamedQueryConstants.QUERY_COUNT_LOCATION_WITH_ID),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_VENUES,
      query = LocationNamedQueryConstants.QUERY_FIND_VENUES),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_PROCESSING_SITES,
      query = LocationNamedQueryConstants.QUERY_FIND_PROCESSING_SITES),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_USAGE_SITES,
      query = LocationNamedQueryConstants.QUERY_FIND_USAGE_SITES)
})
@Entity
@Audited
public class Location extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @NotBlank
  private String name;

  private Boolean isUsageSite;

  private Boolean isMobileSite;

  private Boolean isVenue;
  
  private boolean isProcessingSite;

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

  public boolean isProcessingSite() {
    return isProcessingSite;
  }

  public void setProcessingSite(boolean isProcessingSite) {
    this.isProcessingSite = isProcessingSite;
  }
}
