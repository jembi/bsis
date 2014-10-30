package model.testbatch;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import model.collectionbatch.CollectionBatch;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;
import org.hibernate.envers.Audited;


@Entity
@Audited
public class TestBatch implements ModificationTracker {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false)
	private Long id;

	@Valid
	private RowModificationTracker modificationTracker;

	@Lob
	private String notes;

	private Integer isDeleted;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length=20, unique=true)
	@Size(min=6,max=6)
	private String batchNumber;

	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TestBatchStatus status;
        
        @OneToMany(mappedBy = "testBatch")
        private List<CollectionBatch> collectionBatches;


	public TestBatch() {
		modificationTracker = new RowModificationTracker();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public TestBatchStatus getStatus() {
		return status;
	}

	public void setStatus(TestBatchStatus status) {
		this.status = status;
	}

	public Date getLastUpdated() {
		return modificationTracker.getLastUpdated();
	}

	public Date getCreatedDate() {
		return modificationTracker.getCreatedDate();
	}

	public User getCreatedBy() {
		return modificationTracker.getCreatedBy();
	}

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

        public List<CollectionBatch> getCollectionBatches() {
                return collectionBatches;
        }

        public void setCollectionBatches(List<CollectionBatch> collectionBatches) {
                this.collectionBatches = collectionBatches;
        }


}