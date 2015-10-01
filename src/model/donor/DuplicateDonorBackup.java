package model.donor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity that stores the backup when duplicate Donors are merged. The backup links the Donations
 * that were made by which original Donor before the merge so it is possible to rollback a merge.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class DuplicateDonorBackup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, updatable = false, insertable = false)
	private Long id;
	
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

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((donorDeferralId == null) ? 0 : donorDeferralId.hashCode());
	    result = prime * result + ((donationId == null) ? 0 : donationId.hashCode());
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    result = prime * result + ((mergedDonorNumber == null) ? 0 : mergedDonorNumber.hashCode());
	    result = prime * result + ((newDonorNumber == null) ? 0 : newDonorNumber.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    DuplicateDonorBackup other = (DuplicateDonorBackup) obj;
	    if (donorDeferralId == null) {
		    if (other.donorDeferralId != null)
			    return false;
	    } else if (!donorDeferralId.equals(other.donorDeferralId))
		    return false;
	    if (donationId == null) {
		    if (other.donationId != null)
			    return false;
	    } else if (!donationId.equals(other.donationId))
		    return false;
	    if (id == null) {
		    if (other.id != null)
			    return false;
	    } else if (!id.equals(other.id))
		    return false;
	    if (mergedDonorNumber == null) {
		    if (other.mergedDonorNumber != null)
			    return false;
	    } else if (!mergedDonorNumber.equals(other.mergedDonorNumber))
		    return false;
	    if (newDonorNumber == null) {
		    if (other.newDonorNumber != null)
			    return false;
	    } else if (!newDonorNumber.equals(other.newDonorNumber))
		    return false;
	    return true;
    }
}
