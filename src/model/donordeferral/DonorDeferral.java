package model.donordeferral;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import model.donor.Donor;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;
import org.hibernate.envers.Audited;
import repository.DonorDeferralNamedQueryConstants;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_COUNT_DONOR_DEFERRALS_FOR_DONOR,
                query = DonorDeferralNamedQueryConstants.QUERY_COUNT_DONOR_DEFERRALS_FOR_DONOR),
        @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_FIND_DONOR_DEFERRAL_BY_ID,
                query = DonorDeferralNamedQueryConstants.QUERY_FIND_DONOR_DEFERRAL_BY_ID),
        @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR,
                query = DonorDeferralNamedQueryConstants.QUERY_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR),
        @NamedQuery(name = DonorDeferralNamedQueryConstants.NAME_FIND_DONOR_DEFERRALS_FOR_DONOR_BY_DEFERRAL_REASON,
                query = DonorDeferralNamedQueryConstants.QUERY_FIND_DONOR_DEFERRALS_FOR_DONOR_BY_DEFERRAL_REASON)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class DonorDeferral implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false, insertable = false)
  private Long id;

  @ManyToOne
  private Donor deferredDonor;

  @Temporal(TemporalType.DATE)
  private Date deferredUntil;

  /*
  @ManyToOne
  private User createdBy;
  */

  @ManyToOne
  private DeferralReason deferralReason;

  @ManyToOne
  private User voidedBy;

  @Lob
  private String deferralReasonText;

  private Boolean isVoided = Boolean.FALSE;

  @Temporal(TemporalType.DATE)
  private Date voidedDate;

  @Valid
  private RowModificationTracker modificationTracker;

  public DonorDeferral() {
    modificationTracker = new RowModificationTracker();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
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

  public void copy(DonorDeferral deferral) {
    assert (deferral.getId().equals(this.getId()));
    setDeferredDonor(deferral.getDeferredDonor());
    setDeferredUntil(deferral.getDeferredUntil());
    setDeferralReason(deferral.getDeferralReason());
    setDeferralReasonText(deferral.getDeferralReasonText());
    setIsVoided(deferral.getIsVoided());
    setVoidedBy(deferral.getVoidedBy());
    setVoidedDate(deferral.getVoidedDate());
  }

}
