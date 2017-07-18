package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.DonationPersister;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.util.RandomTestDate;

public class DonationBuilder extends AbstractEntityBuilder<Donation> {

  private UUID id;
  private Donor donor = aDonor().build();
  private Date donationDate;
  private Location venue = LocationBuilder.aVenue().build();
  private String donationIdentificationNumber;
  private TTIStatus ttiStatus;
  private Boolean deleted = Boolean.FALSE;
  private Integer donorPulse;
  private BigDecimal haemoglobinCount;
  private HaemoglobinLevel haemoglobinLevel;
  private Integer bloodPressureSystolic;
  private Integer bloodPressureDiastolic;
  private BigDecimal donorWeight;
  private String notes;
  private PackType packType = aPackType().build();
  private Date bleedStartTime;
  private Date bleedEndTime;
  private AdverseEvent adverseEvent;
  private DonationType donationType;
  private String bloodAbo;
  private String bloodRh;
  private List<BloodTestResult> bloodTestResults;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private BloodTypingStatus bloodTypingStatus;
  private DonationBatch donationBatch = aDonationBatch().build();
  private TestBatch testBatch;
  private Date createdDate = new RandomTestDate();
  private boolean released;
  private boolean ineligibleDonor;
  private List<Component> components = new ArrayList<>();
  private User createdBy;
  private Titre titre;
  private String flagCharacters;

  public DonationBuilder withTitre(Titre titre) {
    this.titre = titre;
    return this;
  }
  public DonationBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DonationBuilder withDonor(Donor donor) {
    this.donor = donor;
    return this;
  }

  public DonationBuilder withDonationBatch(DonationBatch donationBatch) {
    this.donationBatch = donationBatch;
    return this;
  }

  public DonationBuilder withTestBatch(TestBatch testBatch) {
    this.testBatch = testBatch;
    return this;
  }

  public DonationBuilder withDonationDate(Date donationDate) {
    this.donationDate = donationDate;
    return this;
  }

  public DonationBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  public DonationBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public DonationBuilder withTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
    return this;
  }

  public DonationBuilder withDonorPulse(Integer donorPulse) {
    this.donorPulse = donorPulse;
    return this;
  }

  public DonationBuilder withHaemoglobinCount(BigDecimal haemoglobinCount) {
    this.haemoglobinCount = haemoglobinCount;
    return this;
  }

  public DonationBuilder withHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
    this.haemoglobinLevel = haemoglobinLevel;
    return this;
  }

  public DonationBuilder withBloodPressureSystolic(Integer bloodPressureSystolic) {
    this.bloodPressureSystolic = bloodPressureSystolic;
    return this;
  }

  public DonationBuilder withBloodPressureDiastolic(Integer bloodPressureDiastolic) {
    this.bloodPressureDiastolic = bloodPressureDiastolic;
    return this;
  }

  public DonationBuilder withDonorWeight(BigDecimal donorWeight) {
    this.donorWeight = donorWeight;
    return this;
  }

  public DonationBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public DonationBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public DonationBuilder thatIsNotDeleted() {
    deleted = false;
    return this;
  }

  public DonationBuilder withPackType(PackType packType) {
    this.packType = packType;
    return this;
  }

  public DonationBuilder withBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
    return this;
  }

  public DonationBuilder withBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
    return this;
  }

  public DonationBuilder withAdverseEvent(AdverseEvent adverseEvent) {
    this.adverseEvent = adverseEvent;
    return this;
  }

  public DonationBuilder withDonationType(DonationType donationType) {
    this.donationType = donationType;
    return this;
  }

  public DonationBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public DonationBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public DonationBuilder withBloodTestResults(List<BloodTestResult> bloodTestResults) {
    this.bloodTestResults = bloodTestResults;
    return this;
  }

  public DonationBuilder withBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
    return this;
  }

  public DonationBuilder withBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
    return this;
  }

  public DonationBuilder thatIsReleased() {
    this.released = true;
    return this;
  }

  public DonationBuilder thatIsNotReleased() {
    this.released = false;
    return this;
  }
  
  public DonationBuilder withIneligibleDonor(boolean ineligibleDonor) {
    this.ineligibleDonor = ineligibleDonor;
    return this;
  }
  
  public DonationBuilder withComponents(List<Component> components) {
    this.components = components;
    return this;
  }
  
  public DonationBuilder withComponent(Component component) {
    if (components == null) {
      components = new ArrayList<>();
    }
    components.add(component);
    return this;
  }
  
  public DonationBuilder withCreatedBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }
  
  public DonationBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }
  
  public DonationBuilder withFlagCharacters(String flagCharacters) {
    this.flagCharacters = flagCharacters;
    return this;
  }

  @Override
  public Donation build() {
    Donation donation = new Donation();
    donation.setId(id);
    donation.setDonor(donor);
    donation.setDonationDate(donationDate);
    donation.setVenue(venue);
    donation.setDonationIdentificationNumber(donationIdentificationNumber);
    donation.setTTIStatus(ttiStatus);
    donation.setDonorPulse(donorPulse);
    donation.setHaemoglobinCount(haemoglobinCount);
    donation.setHaemoglobinLevel(haemoglobinLevel);
    donation.setBloodPressureSystolic(bloodPressureSystolic);
    donation.setBloodPressureDiastolic(bloodPressureDiastolic);
    donation.setDonorWeight(donorWeight);
    donation.setNotes(notes);
    donation.setIsDeleted(deleted);
    donation.setPackType(packType);
    donation.setBleedStartTime(bleedStartTime);
    donation.setBleedEndTime(bleedEndTime);
    donation.setAdverseEvent(adverseEvent);
    donation.setDonationType(donationType);
    donation.setBloodAbo(bloodAbo);
    donation.setBloodRh(bloodRh);
    donation.setBloodTestResults(bloodTestResults);
    donation.setBloodTypingMatchStatus(bloodTypingMatchStatus);
    donation.setBloodTypingStatus(bloodTypingStatus);
    donation.setDonationBatch(donationBatch);
    donation.setTestBatch(testBatch);
    donation.setCreatedBy(createdBy);
    donation.setCreatedDate(createdDate);
    donation.setReleased(released);
    donation.setIneligibleDonor(ineligibleDonor);
    donation.setComponents(components);
    donation.setTitre(titre);
    donation.setFlagCharacters(flagCharacters);
    return donation;
  }

  @Override
  public AbstractEntityPersister<Donation> getPersister() {
    return new DonationPersister();
  }

  public static DonationBuilder aDonation() {
    return new DonationBuilder();
  }

}
