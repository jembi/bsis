package org.jembi.bsis.model.location;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.repository.LocationNamedQueryConstants;

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
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_MOBILE_VENUES,
      query = LocationNamedQueryConstants.QUERY_FIND_MOBILE_VENUES),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_PROCESSING_SITES,
      query = LocationNamedQueryConstants.QUERY_FIND_PROCESSING_SITES),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_USAGE_SITES,
      query = LocationNamedQueryConstants.QUERY_FIND_USAGE_SITES),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_DISTRIBUTION_SITES,
      query = LocationNamedQueryConstants.QUERY_FIND_DISTRIBUTION_SITES),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_TESTING_SITES,
      query = LocationNamedQueryConstants.QUERY_FIND_TESTING_SITES),
  @NamedQuery(name = LocationNamedQueryConstants.NAME_FIND_REFERRAL_SITES,
      query = LocationNamedQueryConstants.QUERY_FIND_REFERRAL_SITES)
})
@Entity
@Audited
public class Location extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @NotBlank
  private String name;

  @Basic(optional = false)
  @Column(nullable = false)
  private boolean isUsageSite;

  @Basic(optional = false)
  @Column(nullable = false)
  private boolean isMobileSite;

  @Basic(optional = false)
  @Column(nullable = false)
  private boolean isVenue;

  @Basic(optional = false)
  @Column(nullable = false)
  private boolean isProcessingSite;

  @Basic(optional = false)
  @Column(nullable = false)
  private boolean isDistributionSite;

  @Basic(optional = false)
  @Column(nullable = false)
  private boolean isTestingSite;

  @Basic(optional = false)
  @Column(nullable = false)
  private boolean isReferralSite;

  @Basic(optional = false)
  @Column(nullable = false)
  private boolean isDeleted;

  @ManyToOne
  private Division divisionLevel1;

  @ManyToOne
  private Division divisionLevel2;

  @ManyToOne
  private Division divisionLevel3;

  @Lob
  private String notes;

  public void copy(Location location) {
    this.name = location.name;
    this.isVenue = location.isVenue;
    this.isMobileSite = location.isMobileSite;
    this.isUsageSite = location.isUsageSite;
    this.isProcessingSite = location.isProcessingSite;
    this.isDistributionSite = location.isDistributionSite;
    this.isTestingSite = location.isTestingSite;
    this.isReferralSite = location.isReferralSite;
    this.isDeleted = location.isDeleted;
    this.notes = location.notes;
    this.divisionLevel1 = location.divisionLevel1;
    this.divisionLevel2 = location.divisionLevel2;
    this.divisionLevel3 = location.divisionLevel3;
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

  public boolean getIsProcessingSite() {
    return isProcessingSite;
  }

  public void setIsProcessingSite(boolean isProcessingSite) {
    this.isProcessingSite = isProcessingSite;
  }

  public boolean getIsDistributionSite() {
    return isDistributionSite;
  }

  public void setIsDistributionSite(boolean isDistributionSite) {
    this.isDistributionSite = isDistributionSite;
  }

  public boolean getIsTestingSite() {
    return isTestingSite;
  }

  public void setIsTestingSite(boolean isTestingSite) {
    this.isTestingSite = isTestingSite;
  }
  

  public boolean getIsReferralSite() {
    return isReferralSite;
  }

  public void setIsReferralSite(boolean isReferralSite) {
    this.isReferralSite = isReferralSite;
  }

  public Division getDivisionLevel1() {
    return divisionLevel1;
  }

  public void setDivisionLevel1(Division divisionLevel1) {
    this.divisionLevel1 = divisionLevel1;
  }

  public Division getDivisionLevel2() {
    return divisionLevel2;
  }

  public void setDivisionLevel2(Division divisionLevel2) {
    this.divisionLevel2 = divisionLevel2;
  }

  public Division getDivisionLevel3() {
    return divisionLevel3;
  }

  public void setDivisionLevel3(Division divisionLevel3) {
    this.divisionLevel3 = divisionLevel3;
  }

}
