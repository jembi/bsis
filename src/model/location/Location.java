package model.location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Location {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;

  private Boolean isCollectionCenter;

  private Boolean isCollectionSite;

  private Boolean isUsageSite;

  private Boolean isMobileSite;

  private Boolean isDonorPanel;

  private Boolean isDeleted;

  @Lob
  private String notes;

  public Location() {
  }

  public void copy(Location location) {
    this.name = location.name;
    this.isCollectionCenter = location.isCollectionCenter;
    this.isCollectionSite = location.isCollectionSite;
    this.isUsageSite = location.isUsageSite;
    this.notes = location.notes;
  }

  public String getName() {
    return name;
  }

  public Boolean getIsCollectionCenter() {
    return isCollectionCenter;
  }

  public Boolean getIsCollectionSite() {
    return isCollectionSite;
  }

  public Long getId() {
    return id;
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setIsCollectionCenter(Boolean isCollectionCenter) {
    this.isCollectionCenter = isCollectionCenter;
  }

  public void setIsCollectionSite(Boolean collectionSite) {
    isCollectionSite = collectionSite;
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

  public String toString() {
    return name;
  }

  public Boolean getIsDonorPanel() {
    return isDonorPanel;
  }

  public void setIsDonorPanel(Boolean isDonorPanel) {
    this.isDonorPanel = isDonorPanel;
  }
}
