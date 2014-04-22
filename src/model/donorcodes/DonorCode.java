package model.donorcodes;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class DonorCode implements ModificationTracker{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false, updatable=false, insertable=false)
	private Long id;
	
	private String donorcode;
	
	@ManyToOne
	@JoinColumn(name= "donorCodeGroupId")
	private DonorCodeGroup donorCodeGroup;
	
	public DonorCodeGroup getDonorCodeGroup() {
		return donorCodeGroup;
	}

	public void setDonorCodeGroup(DonorCodeGroup donorCodeGroup) {
		this.donorCodeGroup = donorCodeGroup;
	}

	@Valid
    private RowModificationTracker modificationTracker;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDonorcode() {
		return donorcode;
	}

	public void setDonorcode(String donorcode) {
		this.donorcode = donorcode;
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


}
