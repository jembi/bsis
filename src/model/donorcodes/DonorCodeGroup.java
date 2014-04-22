package model.donorcodes;


import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;

import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

import org.hibernate.envers.Audited;



/**
 * Entity implementation class for Entity: DonorCodeGroup
 *
 */
@Entity
@Audited
public class DonorCodeGroup implements ModificationTracker {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false, updatable=false, insertable=false)
	private Long id;
    
    private String donorCodeGroup ;
    
    @OneToMany(mappedBy = "donorCodeGroup")
    private List<DonorCode> donorCodes;
    
    @Valid
    private RowModificationTracker modificationTracker;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDonorCodeGroup() {
		return donorCodeGroup;
	}

	public void setDonorCodeGroup(String donorCodeGroup) {
		this.donorCodeGroup = donorCodeGroup;
	}

	@Override
	public Date getCreatedDate() {
		return modificationTracker.getCreatedDate();
	}

	@Override
	public User getCreatedBy() {
		return modificationTracker.getCreatedBy();
	}
	
	@Override
	public Date getLastUpdated() {
		return modificationTracker.getLastUpdated();
	}

	@Override
	public User getLastUpdatedBy() {
		return modificationTracker.getLastUpdatedBy();
	}

	@Override
	public void setLastUpdated(Date lastUpdated) {
		 modificationTracker.setLastUpdated(lastUpdated);
		
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		modificationTracker.setCreatedDate(createdDate);
		
	}

	@Override
	public void setCreatedBy(User createdBy) {
		modificationTracker.setCreatedBy(createdBy);
		
	}

	@Override
	public void setLastUpdatedBy(User lastUpdatedBy) {
		modificationTracker.setLastUpdatedBy(lastUpdatedBy);
		
	}

	public List<DonorCode> getDonorCodes() {
		return donorCodes;
	}

	public void setDonorCodes(List<DonorCode> donorCodes) {
		this.donorCodes = donorCodes;
	}
	
    
   
}
