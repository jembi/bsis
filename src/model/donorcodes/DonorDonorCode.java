package model.donorcodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import model.donor.Donor;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

@Entity
@Audited
public class DonorDonorCode implements ModificationTracker{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, insertable = false)
    private Long id;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "donorId", nullable = false, updatable = false)
    private Donor donorId;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "donorCodeId", nullable = false, updatable = false)
    private DonorCode donorCodeId;

    private RowModificationTracker modificationTracker;

    public DonorDonorCode() {
        modificationTracker = new RowModificationTracker();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Donor getDonorId() {
		return donorId;
	}

	public void setDonorId(Donor donorId) {
		this.donorId = donorId;
	}

	public DonorCode getDonorCodeId() {
		return donorCodeId;
	}

	public void setDonorCodeId(DonorCode donorCodeId) {
		this.donorCodeId = donorCodeId;
	}


	public Date getLastUpdated() {
	    return modificationTracker.getLastUpdated();
	  }

	
	  public Date getCreatedDate() {
	    return modificationTracker.getCreatedDate();
	  }

	@JsonIgnore
	public User getCreatedBy() {
	    return modificationTracker.getCreatedBy();
	  }

	@JsonIgnore
	public User getLastUpdatedBy() {
	    return modificationTracker.getLastUpdatedBy();
	  }


	  public void setLastUpdated(Date lastUpdated) {
	    modificationTracker.setLastUpdated(lastUpdated);
	  }

	  public void setCreatedDate(Date createdDate) {
	    modificationTracker.setCreatedDate(createdDate);
	  }

	  public void setCreatedBy(User createdBy) {
	    modificationTracker.setCreatedBy(createdBy);
	  }


	  public void setLastUpdatedBy(User lastUpdatedBy) {
	    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
	  }

	
	
}
