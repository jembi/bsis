package model.collectedsample;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import constraintvalidator.BloodBagTypeExists;
import constraintvalidator.CollectionBatchExists;
import constraintvalidator.DonationTypeExists;
import constraintvalidator.DonorExists;
import constraintvalidator.LocationExists;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import model.bloodbagtype.BloodBagType;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.collectionbatch.CollectionBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.user.User;
import model.worksheet.Worksheet;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Range;
import repository.bloodtesting.BloodTypingStatus;

/**
 * A donation or a collection as it is in the UI.
 * Not naming the class as Collection to avoid confusion with java.util.Collection.
 * @author iamrohitbanga
 */
@Entity
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class CollectedSample implements ModificationTracker, Comparable<CollectedSample> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
  private Long id;

  /**
   * Very common usecase to search for collection by collection number.
   * In most cases the collection numbers will be preprinted labels.
   */
  @Column(length=20, unique=true)
  @Index(name="collectedSample_collectionNumber_index")
  private String collectionNumber;

  @DonorExists
  @ManyToOne
  private Donor donor;

  @Column(length=50)
  private String bloodAbo;

  @Column(length=50)
  private String bloodRh;

  @Column(length=150)
  private String extraBloodTypeInformation;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy="collectedSample")
  private List<BloodTestResult> bloodTestResults;



  /**
   * Index to find collections done between date ranges.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="collectedSample_collectedOn_index")
  private Date collectedOn;

  @DonationTypeExists
  @ManyToOne
  private DonationType donationType;

  @BloodBagTypeExists
  @ManyToOne
  private BloodBagType bloodBagType;

  /**
   * List of products created from this collection.
   */
  @OneToMany(mappedBy="collectedSample")
  private List<Product> products;

  @NotAudited
  @ManyToMany(mappedBy="collectedSamples")
  private Set<Worksheet> worksheets;

  @Range(min = 0, max = 30)
  private BigDecimal haemoglobinCount;

  @Column(name="bloodPressureSystolic")
  @Range(min = 0, max = 250)
  private Integer bloodPressureSystolic;
  
  @Column(name="bloodPressureDiastolic")
  @Range(min = 0, max = 150)
  private Integer bloodPressureDiastolic;

  /**
   * Limit the number of bytes required to store.
   */
  
  @Range(min = 0, max = 300)
  private BigDecimal donorWeight;

  @ManyToOne(optional=true)
  private User donationCreatedBy;

  @CollectionBatchExists
  @ManyToOne(optional=true)
  private CollectionBatch collectionBatch;

  @Lob
  private String notes;

  @Valid
  private RowModificationTracker modificationTracker;

  @Enumerated(EnumType.STRING)
  @Column(length=20)
  private BloodTypingStatus bloodTypingStatus;

  @Enumerated(EnumType.STRING)
  @Column(length=20)
  private TTIStatus ttiStatus;

  private Boolean isDeleted;
  
  @Range(min =0 ,max = 290)
  private Integer donorPulse;
  
  public CollectedSample() {
    modificationTracker = new RowModificationTracker();
    worksheets = new HashSet<Worksheet>();
  }

  public Long getId() {
    return id;
  }


  
  public String getCollectionNumber() {
    return collectionNumber;
  }

  public Donor getDonor() {
    return donor;
  }


  public Date getCollectedOn() {
    return collectedOn;
  }

  public BloodBagType getBloodBagType() {
    return bloodBagType;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }

  public void setDonor(Donor donor) {
    this.donor = donor;
  }


  public void setCollectedOn(Date collectedOn) {
    this.collectedOn = collectedOn;
  }

  public void setBloodBagType(BloodBagType bloodBagType) {
    this.bloodBagType = bloodBagType;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void copy(CollectedSample collectedSample) {
    assert (this.getId().equals(collectedSample.getId()));
    this.collectionNumber = collectedSample.collectionNumber;
    this.donor = collectedSample.donor;
    this.setDonationType(collectedSample.getDonationType());
    this.bloodBagType = collectedSample.bloodBagType;
    this.collectedOn = collectedSample.collectedOn;
    this.collectionBatch = collectedSample.collectionBatch;
    this.notes = collectedSample.notes;
    this.haemoglobinCount=collectedSample.haemoglobinCount;
    this.donorPulse = collectedSample.donorPulse;
    this.donorWeight=collectedSample.donorWeight;
    this.bloodPressureDiastolic=collectedSample.bloodPressureDiastolic;
    this.bloodPressureSystolic=collectedSample.bloodPressureSystolic;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
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

  public Set<Worksheet> getWorksheets() {
    return worksheets;
  }

  public void setWorksheets(Set<Worksheet> worksheets) {
    this.worksheets = worksheets;
  }

  /**
   * Must implement this method because the collections
   * in worksheet must be sorted in the same order every time.
   * This method allows invoking Collections.sort on the collections.
   */
  @Override
  public int compareTo(CollectedSample c) {
    Long diff = (this.id - c.id);
    if (diff < 0)
      return -1;
    if (diff > 0)
      return 1;
    return 0;
  }

  public TTIStatus getTTIStatus() {
    return ttiStatus;
  }

  public void setTTIStatus(TTIStatus testedStatus) {
    this.ttiStatus = testedStatus;
  }

  public BigDecimal getHaemoglobinCount() {
    return haemoglobinCount;
  }

  public void setHaemoglobinCount(BigDecimal haemoglobinCount) {
    this.haemoglobinCount = haemoglobinCount;
  }
  
  public Integer getBloodPressureSystolic() {
		return bloodPressureSystolic;
	}

  public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
		this.bloodPressureSystolic = bloodPressureSystolic;
	}

  public BigDecimal getDonorWeight() {
    return donorWeight;
  }
  
  public void setDonorWeight(BigDecimal donorWeight) {
    this.donorWeight = donorWeight;
  }

  public User getDonationCreatedBy() {
    return donationCreatedBy;
  }

  public void setDonationCreatedBy(User donationCreatedBy) {
    this.donationCreatedBy = donationCreatedBy;
  }

  public CollectionBatch getCollectionBatch() {
    return collectionBatch;
  }

  public void setCollectionBatch(CollectionBatch collectionBatch) {
    this.collectionBatch = collectionBatch;
  }

  public DonationType getDonationType() {
    return donationType;
  }

  public void setDonationType(DonationType donationType) {
    this.donationType = donationType;
  }

  public String getDonorNumber() {
    if (donor != null)
      return donor.getDonorNumber();
    return "";
  }

  public String getCollectionBatchNumber() {
    if (collectionBatch != null)
      return collectionBatch.getBatchNumber();
    return "";
  }

  public List<BloodTestResult> getBloodTestResults() {
    return bloodTestResults;
  }

  public void setBloodTestResults(List<BloodTestResult> bloodTestResults) {
    this.bloodTestResults = bloodTestResults;
  }

  public BloodTypingStatus getBloodTypingStatus() {
    return bloodTypingStatus;
  }

  public void setBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public Integer getBloodPressureDiastolic() {
		return bloodPressureDiastolic;
	}

	public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
		this.bloodPressureDiastolic = bloodPressureDiastolic;
	}

	public String getExtraBloodTypeInformation() {
    return extraBloodTypeInformation;
  }

    public void setExtraBloodTypeInformation(String extraBloodTypeInformation) {
        this.extraBloodTypeInformation = extraBloodTypeInformation;
    }

    public Integer getDonorPulse() {
        return donorPulse;
    }

    public void setDonorPulse(Integer donorPulse) {
        this.donorPulse = donorPulse;
    }

}
