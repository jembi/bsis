
package backingform;

import javax.validation.Valid;

import model.location.Location;

public class LocationBackingForm {

  @Valid
  private Location location;

  public LocationBackingForm() {
    location = new Location();
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Long getId() {
    return location.getId();
  }

  public void setId(Long id) {
    location.setId(id);
  }

  public String getName() {
    return location.getName();
  }

  public void setName(String name) {
    location.setName(name);
  }

  public Boolean getIsUsageSite() {
    return location.getIsUsageSite();
  }

  public void setIsUsageSite(Boolean isUsageSite) {
    location.setIsUsageSite(isUsageSite);
  }

  public Boolean getIsMobilesite() {
    return location.getIsMobileSite();
  }

  public void setIsMobileSite(Boolean isMobileSite) {
    location.setIsMobileSite(isMobileSite);
  }

  public Boolean getIsVenue() {
    return location.getIsVenue();
  }

  public void setIsVenue(Boolean isVenue) {
    location.setIsVenue(isVenue);
  }

  public Boolean getIsDeleted() {
    return location.getIsDeleted();
  }

  public void setIsDeleted(Boolean isDeleted) {
    location.setIsDeleted(isDeleted);
  }

  public String getNotes() {
    return location.getNotes();
  }

  public void setNotes(String notes) {
    location.setNotes(notes);
  }
  
  public boolean getIsProcessingSite() {
    return location.isProcessingSite();
  }
  
  public void setIsProcessingSite(boolean isProcessingSite) {
    location.setProcessingSite(isProcessingSite);
  }
}
