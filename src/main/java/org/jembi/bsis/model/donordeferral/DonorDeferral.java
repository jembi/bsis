package org.jembi.bsis.model.donordeferral;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.DonorDeferralNamedQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@NamedQueries({
    @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_COUNT_DONOR_DEFERRALS_FOR_DONOR,
        query = DonorDeferralNamedQueryConstants.QUERY_COUNT_DONOR_DEFERRALS_FOR_DONOR),
    @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_FIND_DONOR_DEFERRAL_BY_ID,
        query = DonorDeferralNamedQueryConstants.QUERY_FIND_DONOR_DEFERRAL_BY_ID),
    @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR,
        query = DonorDeferralNamedQueryConstants.QUERY_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR),
    @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_FIND_DONOR_DEFERRALS_FOR_DONOR_BY_DEFERRAL_REASON,
        query = DonorDeferralNamedQueryConstants.QUERY_FIND_DONOR_DEFERRALS_FOR_DONOR_BY_DEFERRAL_REASON),
    @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_COUNT_DEFERRALS_BY_VENUE_DEFERRAL_REASON_AND_GENDER,
        query = DonorDeferralNamedQueryConstants.QUERY_COUNT_DEFERRALS_BY_VENUE_DEFERRAL_REASON_AND_GENDER),
    @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_FIND_DEFERRALS_FOR_EXPORT,
        query = DonorDeferralNamedQueryConstants.QUERY_FIND_DEFERRALS_FOR_EXPORT)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class DonorDeferral extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date deferralDate;

  @ManyToOne(optional = false)
  private Donor deferredDonor;

  @Temporal(TemporalType.DATE)
  private Date deferredUntil;

  @ManyToOne
  private DeferralReason deferralReason;
  
  @ManyToOne(optional = false)
  private Location venue;

  @ManyToOne
  private User voidedBy;

  @Lob
  private String deferralReasonText;

  private Boolean isVoided = Boolean.FALSE;

  @Temporal(TemporalType.DATE)
  private Date voidedDate;

  public DonorDeferral() {
    super();
  }

  public Donor getDeferredDonor() {
    return deferredDonor;
  }

  public void setDeferredDonor(Donor deferredDonor) {
    this.deferredDonor = deferredDonor;
  }

  public Date getDeferredUntil() {
    return deferredUntil;
  }

  public void setDeferredUntil(Date deferredUntil) {
    this.deferredUntil = deferredUntil;
  }

  public DeferralReason getDeferralReason() {
    return deferralReason;
  }

  public void setDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
  }

  public String getDeferralReasonText() {
    return deferralReasonText;
  }

  public void setDeferralReasonText(String deferralReasonText) {
    this.deferralReasonText = deferralReasonText;
  }

  public Location getVenue() {
    return venue;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
  }

  /**
   * @return the isVoided
   */
  public Boolean getIsVoided() {
    return isVoided;
  }

  /**
   * @param isVoided the isVoided to set
   */
  public void setIsVoided(Boolean isVoided) {
    this.isVoided = isVoided;
  }

  /**
   * @return the voidedBy
   */
  public User getVoidedBy() {
    return voidedBy;
  }

  /**
   * @param voidedBy the voidedBy to set
   */
  public void setVoidedBy(User voidedBy) {
    this.voidedBy = voidedBy;
  }

  /**
   * @return the voidedDate
   */
  public Date getVoidedDate() {
    return voidedDate;
  }

  /**
   * @param voidedDate the voidedDate to set
   */
  public void setVoidedDate(Date voidedDate) {
    this.voidedDate = voidedDate;
  }

  public Date getDeferralDate() {
    return deferralDate;
  }

  public void setDeferralDate(Date deferralDate) {
    this.deferralDate = deferralDate;
  }

}
