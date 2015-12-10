package model.donor;

import javax.persistence.Column;
import javax.persistence.Entity;

import model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity that stores the backup when duplicate Donors are merged. The backup links the Donations
 * that were made by which original Donor before the merge so it is possible to rollback a merge.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class DuplicateDonorBackup extends BaseEntity {

  private static final long serialVersionUID = 1L;
	
	@Column
	private String newDonorNumber;
	
	@Column
	private String mergedDonorNumber;
	
	@Column(name = "donation_id")
	private Long donationId;
	
	@Column(name = "donorDeferral_id")
	private Long donorDeferralId;

	public DuplicateDonorBackup() {
	    super();
    }
	
	public DuplicateDonorBackup(String newDonorId, String mergedDonorId, Long donationId, Long deferralId) {
		super();
		this.newDonorNumber = newDonorId;
		this.mergedDonorNumber = mergedDonorId;
		this.donationId = donationId;
		this.donorDeferralId = deferralId;
	}
	
	public String getNewDonorNumber() {
		return newDonorNumber;
	}
	
	public void setNewDonorNumber(String newDonorId) {
		this.newDonorNumber = newDonorId;
	}
	
	public String getMergedDonorNumber() {
		return mergedDonorNumber;
	}
	
	public void setMergedDonorNumber(String mergedDonorId) {
		this.mergedDonorNumber = mergedDonorId;
	}
	
	public Long getDonationId() {
		return donationId;
	}
	
	public void setDonationId(Long donationId) {
		this.donationId = donationId;
	}
	
	public Long getDonorDeferralId() {
		return donorDeferralId;
	}
	
	public void setDonorDeferralId(Long deferralId) {
		this.donorDeferralId = deferralId;
	}
}
