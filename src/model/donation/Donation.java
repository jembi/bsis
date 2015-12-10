package model.donation;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import constraintvalidator.*;
import model.adverseevent.AdverseEvent;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.component.Component;
import model.donationbatch.DonationBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.packtype.PackType;
import model.user.User;
import model.worksheet.Worksheet;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Range;
import repository.DonationNamedQueryConstants;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A donation of blood
 *
 * @author iamrohitbanga
 */
@NamedQueries({
        @NamedQuery(name = DonationNamedQueryConstants.NAME_COUNT_DONATIONS_FOR_DONOR,
                query = DonationNamedQueryConstants.QUERY_COUNT_DONATION_FOR_DONOR),
        @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_ASCENDING_DONATION_DATES_FOR_DONOR,
                query = DonationNamedQueryConstants.QUERY_FIND_ASCENDING_DONATION_DATES_FOR_DONOR),
        @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_DESCENDING_DONATION_DATES_FOR_DONOR,
                query = DonationNamedQueryConstants.QUERY_FIND_DESCENDING_DONATION_DATES_FOR_DONOR),
        @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_COLLECTED_DONATION_VALUE_OBJECTS_FOR_DATE_RANGE,
                query = DonationNamedQueryConstants.QUERY_FIND_COLLECTED_DONATION_VALUE_OBJECTS_FOR_DATE_RANGE),
        @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR,
                query = DonationNamedQueryConstants.QUERY_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Donation implements ModificationTracker, Comparable<Donation> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  /**
   * Very common usecase to search for donation by donation identification number.
   * In most cases the donation numbers will be preprinted labels.
   */
  @Column(length = 20, unique = true)
  @Index(name = "donation_donationIdentificationNumber_index")
  private String donationIdentificationNumber;

  @DonorExists
  @ManyToOne
  private Donor donor;

  @Column(length = 50)
  private String bloodAbo;

  @Column(length = 50)
  private String bloodRh;

  @Column(length = 150)
  private String extraBloodTypeInformation;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "donation")
  private List<BloodTestResult> bloodTestResults;


  /**
   * Index to find donations done between date ranges.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Index(name = "donation_donationDate_index")
  private Date donationDate;

  @DonationTypeExists
  @ManyToOne
  private DonationType donationType;

  @PackTypeExists
  @ManyToOne
  private PackType packType;

  /**
   * List of components created from this donation.
   */
  @OneToMany(mappedBy = "donation")
  private List<Component> components;

  @NotAudited
  @ManyToMany(mappedBy = "donations")
  private Set<Worksheet> worksheets;

  @Range(min = 0, max = 30)
  private BigDecimal haemoglobinCount;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private HaemoglobinLevel haemoglobinLevel;

  @Column(name = "bloodPressureSystolic")
  @Range(min = 0, max = 250)
  private Integer bloodPressureSystolic;

  @Column(name = "bloodPressureDiastolic")
  @Range(min = 0, max = 150)
  private Integer bloodPressureDiastolic;

  /**
   * Limit the number of bytes required to store.
   */

  @Range(min = 0, max = 300)
  private BigDecimal donorWeight;

  @ManyToOne(optional = true)
  private User donationCreatedBy;

  @DonationBatchExists
  @ManyToOne(optional = true)
  private DonationBatch donationBatch;

  @Lob
  private String notes;

  @Valid
  private RowModificationTracker modificationTracker;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private BloodTypingStatus bloodTypingStatus;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private BloodTypingMatchStatus bloodTypingMatchStatus;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private TTIStatus ttiStatus;

  private Boolean isDeleted;

  @Range(min = 0, max = 290)
  private Integer donorPulse;

  @Temporal(TemporalType.TIMESTAMP)
  private Date bleedStartTime;

  @Temporal(TemporalType.TIMESTAMP)
  private Date bleedEndTime;

  @OneToOne
  @LocationExists
  @NotNull
  private Location venue;

  @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
  private AdverseEvent adverseEvent;

  // If the donor was ineligible when the donation was captured
  @Column(nullable = false)
  private boolean ineligibleDonor = false;

  // If this donation has been released in a test batch
  @Column(nullable = false)
  private boolean released = false;

  public Donation() {
    modificationTracker = new RowModificationTracker();
    worksheets = new HashSet<>();
  }

  public Donation(Donation donation) {
    this();
    this.id = donation.getId();
    this.donationIdentificationNumber = donation.getDonationIdentificationNumber();
    this.donor = donation.getDonor();
    this.bloodAbo = donation.getBloodAbo();
    this.bloodRh = donation.getBloodRh();
    this.extraBloodTypeInformation = donation.getExtraBloodTypeInformation();
    this.bloodTestResults = donation.getBloodTestResults();
    this.donationDate = donation.getDonationDate();
    this.donationType = donation.getDonationType();
    this.packType = donation.getPackType();
    this.components = donation.getComponents();
    this.worksheets = donation.getWorksheets();
    this.haemoglobinCount = donation.getHaemoglobinCount();
    this.haemoglobinLevel = donation.getHaemoglobinLevel();
    this.bloodPressureSystolic = donation.getBloodPressureSystolic();
    this.bloodPressureDiastolic = donation.getBloodPressureDiastolic();
    this.donorWeight = donation.getDonorWeight();
    this.donationCreatedBy = donation.getCreatedBy();
    this.donationBatch = donation.getDonationBatch();
    this.notes = donation.getNotes();
    this.bloodTypingStatus = donation.getBloodTypingStatus();
    this.bloodTypingMatchStatus = donation.getBloodTypingMatchStatus();
    this.ttiStatus = donation.getTTIStatus();
    this.isDeleted = donation.getIsDeleted();
    this.donorPulse = donation.getDonorPulse();
    this.bleedStartTime = donation.getBleedStartTime();
    this.bleedEndTime = donation.getBleedEndTime();
    this.venue = donation.getVenue();
    this.adverseEvent = donation.getAdverseEvent();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public Donor getDonor() {
    return donor;
  }

  public void setDonor(Donor donor) {
    this.donor = donor;
  }

  public Date getDonationDate() {
    return donationDate;
  }

  public void setDonationDate(Date donationDate) {
    this.donationDate = donationDate;
  }

  public PackType getPackType() {
    return packType;
  }

  public void setPackType(PackType packType) {
    this.packType = packType;
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

  public void copy(Donation donation) {
    assert (this.getId().equals(donation.getId()));
    this.donationIdentificationNumber = donation.donationIdentificationNumber;
    this.donor = donation.donor;
    this.setDonationType(donation.getDonationType());
    this.packType = donation.packType;
    this.donationDate = donation.donationDate;
    this.donationBatch = donation.donationBatch;
    this.notes = donation.notes;
    this.haemoglobinCount = donation.haemoglobinCount;
    this.haemoglobinLevel = donation.haemoglobinLevel;
    this.donorPulse = donation.donorPulse;
    this.donorWeight = donation.donorWeight;
    this.bloodPressureDiastolic = donation.bloodPressureDiastolic;
    this.bloodPressureSystolic = donation.bloodPressureSystolic;
    this.venue = donation.getVenue();
    this.bloodAbo = donation.bloodAbo;
    this.bloodRh = donation.bloodRh;
    this.setBloodTypingMatchStatus(donation.getBloodTypingMatchStatus());
  }

  public List<Component> getComponents() {
    return components;
  }

  public void setComponents(List<Component> components) {
    this.components = components;
  }

  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
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
   * Compares two donations using the object's identifier (id field)
   */
  @Override
  public int compareTo(Donation c) {
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

  public HaemoglobinLevel getHaemoglobinLevel() {
    return haemoglobinLevel;
  }

  public void setHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
    this.haemoglobinLevel = haemoglobinLevel;
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

  public DonationBatch getDonationBatch() {
    return donationBatch;
  }

  public void setDonationBatch(DonationBatch donationBatch) {
    this.donationBatch = donationBatch;
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

  public String getDonationBatchNumber() {
    if (donationBatch != null)
      return donationBatch.getBatchNumber();
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

  public BloodTypingMatchStatus getBloodTypingMatchStatus() {
    return bloodTypingMatchStatus;
  }

  public void setBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
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

  public Date getBleedStartTime() {
    return bleedStartTime;
  }

  public void setBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
  }

  public Date getBleedEndTime() {
    return bleedEndTime;
  }

  public void setBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
  }

  public Location getVenue() {
    return venue;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
  }

  public AdverseEvent getAdverseEvent() {
    return adverseEvent;
  }

  public void setAdverseEvent(AdverseEvent adverseEvent) {
    this.adverseEvent = adverseEvent;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    return other instanceof Donation &&
            ((Donation) other).id == id;
  }

  public boolean isIneligibleDonor() {
    return ineligibleDonor;
  }

  public void setIneligibleDonor(boolean ineligibleDonor) {
    this.ineligibleDonor = ineligibleDonor;
  }

  public boolean isReleased() {
    return released;
  }

  public void setReleased(boolean released) {
    this.released = released;
  }

}
