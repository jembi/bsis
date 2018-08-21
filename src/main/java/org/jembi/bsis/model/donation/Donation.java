package org.jembi.bsis.model.donation;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Range;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.DonationNamedQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * A donation of blood
 *
 * @author iamrohitbanga
 */
@SuppressWarnings("deprecation")
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
        query = DonationNamedQueryConstants.QUERY_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_LAST_DONATIONS_BY_DONOR_VENUE_AND_DONATION_DATE,
        query = DonationNamedQueryConstants.QUERY_FIND_LAST_DONATIONS_BY_DONOR_VENUE_AND_DONATION_DATE),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_DONATIONS_FOR_EXPORT,
        query = DonationNamedQueryConstants.QUERY_FIND_DONATIONS_FOR_EXPORT),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_DONATION_BY_DONATION_IDENTIFICATION_NUMBER_INCLUDE_DELETED,
        query = DonationNamedQueryConstants.QUERY_FIND_DONATION_BY_DONATION_IDENTIFICATION_NUMBER_INCLUDE_DELETED),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_DONATION_BY_DONATION_IDENTIFICATION_NUMBER,
        query = DonationNamedQueryConstants.QUERY_FIND_DONATION_BY_DONATION_IDENTIFICATION_NUMBER),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_DONATIONS_BETWEEN_TWO_DINS,
        query = DonationNamedQueryConstants.QUERY_FIND_DONATIONS_BETWEEN_TWO_DINS),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_IN_RANGE,
        query = DonationNamedQueryConstants.QUERY_FIND_IN_RANGE),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_BY_PACK_TYPE_ID_IN_RANGE,
        query = DonationNamedQueryConstants.QUERY_FIND_BY_PACK_TYPE_ID_IN_RANGE),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_BY_VENUE_ID_IN_RANGE,
        query = DonationNamedQueryConstants.QUERY_FIND_BY_VENUE_ID_IN_RANGE),
    @NamedQuery(name = DonationNamedQueryConstants.NAME_FIND_BY_VENUE_ID_AND_PACK_TYPE_ID_IN_RANGE,
        query = DonationNamedQueryConstants.QUERY_FIND_BY_VENUE_ID_AND_PACK_TYPE_ID_IN_RANGE)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Donation extends BaseModificationTrackerUUIDEntity implements Comparable<Donation> {

  private static final long serialVersionUID = 1L;

  /**
   * Very common usecase to search for donation by donation identification number. In most cases the
   * donation numbers will be preprinted labels.
   */
  @Column(length = 20, unique = true)
  @Index(name = "donation_donationIdentificationNumber_index")
  private String donationIdentificationNumber;

  @ManyToOne
  private Donor donor;

  @Column(length = 50)
  private String bloodAbo;

  @Column(length = 50)
  private String bloodRh;

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

  @ManyToOne
  private DonationType donationType;

  @ManyToOne(optional = false)
  @JoinColumn(name = "packType_id", nullable = false)
  private PackType packType;

  /**
   * List of components created from this donation.
   */
  @OneToMany(mappedBy = "donation")
  @Where(clause = "isDeleted = 0")
  private List<Component> components = new ArrayList<>();

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

  @ManyToOne(optional = true)
  private DonationBatch donationBatch;

  @ManyToOne(optional = true)
  private TestBatch testBatch;

  @Lob
  private String notes;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private BloodTypingStatus bloodTypingStatus;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private BloodTypingMatchStatus bloodTypingMatchStatus;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private TTIStatus ttiStatus;

  private Boolean isDeleted = Boolean.FALSE;

  @Range(min = 0, max = 290)
  private Integer donorPulse;

  @Temporal(TemporalType.TIMESTAMP)
  private Date bleedStartTime;

  @Temporal(TemporalType.TIMESTAMP)
  private Date bleedEndTime;

  @ManyToOne(optional = false)
  private Location venue;

  @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
  private AdverseEvent adverseEvent;

  // If the donor was ineligible when the donation was captured
  @Column(nullable = false)
  private boolean ineligibleDonor = false;

  // If this donation has been released in a test batch
  @Column(nullable = false)
  private boolean released = false;
  
  @Column(nullable = true)
  private String flagCharacters;

  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  private Titre titre;

  public Donation() {
    super();
  }

  public Donation(Donation donation) {
    this();
    setId(donation.getId());
    this.donationIdentificationNumber = donation.getDonationIdentificationNumber();
    this.donor = donation.getDonor();
    this.bloodAbo = donation.getBloodAbo();
    this.bloodRh = donation.getBloodRh();
    this.bloodTestResults = donation.getBloodTestResults();
    this.donationDate = donation.getDonationDate();
    this.donationType = donation.getDonationType();
    this.packType = donation.getPackType();
    this.components = donation.getComponents();
    this.haemoglobinCount = donation.getHaemoglobinCount();
    this.haemoglobinLevel = donation.getHaemoglobinLevel();
    this.bloodPressureSystolic = donation.getBloodPressureSystolic();
    this.bloodPressureDiastolic = donation.getBloodPressureDiastolic();
    this.donorWeight = donation.getDonorWeight();
    this.donationCreatedBy = donation.getCreatedBy();
    this.donationBatch = donation.getDonationBatch();
    this.testBatch = donation.getTestBatch();
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
    this.titre = donation.getTitre();
    this.flagCharacters = donation.getFlagCharacters();
  }

  public boolean isTestable() {
    return getPackType().getTestSampleProduced();
  }

  public boolean isIncludedIn(TestBatch testBatch) {
    return this.getTestBatch() != null && Objects.equals(this.getTestBatch(), testBatch);
  }

  public void resetTestStatuses() {
    setTTIStatus(TTIStatus.NOT_DONE);
    setBloodAbo(null);
    setBloodRh(null);
    setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public Donor getDonor() {
    return donor;
  }


  public Date getDonationDate() {
    return donationDate;
  }

  public PackType getPackType() {
    return packType;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public void setDonor(Donor donor) {
    this.donor = donor;
  }


  public void setDonationDate(Date donationDate) {
    this.donationDate = donationDate;
  }

  public void setPackType(PackType packType) {
    this.packType = packType;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  /**
   * Finds the initial component (which is the component which is the parent
   * of all other components) for this Donation.
   *
   * @return Component, or null if there are no components
   */
  public Component getInitialComponent() {
    for (Component component : this.components) {
      if (component.isInitialComponent()) {
        return component;
      }
    }
    return null;
  }

  public List<Component> getComponents() {
    return components;
  }

  public void setComponents(List<Component> components) {
    this.components = components;
  }

  public void addComponent(Component component) {
    if (component == null) {
      return;
    }
    if (components == null) {
      components = new ArrayList<>();
    }
    components.add(component);
  }

  /**
   * Compares two donations using the object's identifier (id field)
   */
  @Override
  public int compareTo(Donation c) {
    return this.getId().compareTo(c.getId());
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

  public TestBatch getTestBatch() {
    return testBatch;
  }

  public void setTestBatch(TestBatch testBatch) {
    this.testBatch = testBatch;
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

  public Titre getTitre () {
    return titre;
  }

  public void setTitre (Titre titre) {
    this.titre = titre;
  }

  public String getFlagCharacters() {
    return flagCharacters;
  }

  public void setFlagCharacters(String flagCharacters) {
    this.flagCharacters = flagCharacters;
  }
}
