package model.donordeferral;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import model.ModificationTrackerBaseEntity;
import model.donor.Donor;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

import org.hibernate.envers.Audited;

import repository.DonorDeferralNamedQueryConstants;

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
            query = DonorDeferralNamedQueryConstants.QUERY_FIND_DONOR_DEFERRALS_FOR_DONOR_BY_DEFERRAL_REASON)
})
@Entity
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class DonorDeferral extends ModificationTrackerBaseEntity {

  private static final long serialVersionUID = 1L;

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
  
  public DonorDeferral(){
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
