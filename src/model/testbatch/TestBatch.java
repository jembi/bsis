package model.testbatch;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import model.BaseModificationTrackerEntity;
import model.donationbatch.DonationBatch;

import org.hibernate.envers.Audited;

import service.TestBatchCRUDService;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class TestBatch extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

	@Lob
	private String notes;

	private Boolean isDeleted;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length=20, unique=true)
	@Size(min=6,max=6)
	private String batchNumber;

	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TestBatchStatus status;
        
        @OneToMany(mappedBy = "testBatch", fetch = FetchType.EAGER)
        private List<DonationBatch> donationBatches;


	public TestBatch() {
		super();
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}
	
	public void setIsDeleted(Boolean isDeleted) {
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

	/**
	 * N.B. Updating the status of a test batch should be done via the {@link TestBatchCRUDService}.
	 * 
	 * @param status The new {@link TestBatchStatus}
	 */
	public void setStatus(TestBatchStatus status) {
		this.status = status;
	}

    public List<DonationBatch> getDonationBatches() {
            return donationBatches;
    }

    public void setDonationBatches(List<DonationBatch> donationBatches) {
            this.donationBatches = donationBatches;
    }

}