package model.donordeferral;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.donor.Donor;
import model.user.User;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class DonorDeferral {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  @ManyToOne
  private Donor deferredDonor;

  @Temporal(TemporalType.DATE)
  private Date deferredOn;

  @Temporal(TemporalType.DATE)
  private Date deferredUntil;

  @ManyToOne
  private User deferredBy;

  @ManyToOne
  private DeferralReason deferralReason;
  
  @ManyToOne
  private User voidedBy;

  @Lob
  private String deferralReasonText;
  
  private Boolean isVoided;
  
  @Temporal(TemporalType.DATE)
  private Date voidedDate;

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

  public Date getDeferredOn() {
    return deferredOn;
  }

  public void setDeferredOn(Date deferredOn) {
    this.deferredOn = deferredOn;
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

  public User getDeferredBy() {
    return deferredBy;
  }

  public void setDeferredBy(User deferredBy) {
    this.deferredBy = deferredBy;
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
	    setDeferredOn(deferral.getDeferredOn());
	    setDeferredUntil(deferral.getDeferredUntil());
	    setDeferralReason(deferral.getDeferralReason());
	    setDeferralReasonText(deferral.getDeferralReasonText());
	    setDeferredBy(deferral.getDeferredBy());
	    setIsVoided(deferral.getIsVoided());
	    setVoidedBy(deferral.getVoidedBy());
	    setVoidedDate(deferral.getVoidedDate());	   
	 }
  
}
