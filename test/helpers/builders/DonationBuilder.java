package helpers.builders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import model.donationbatch.DonationBatch;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.DonationPersister;
import model.adverseevent.AdverseEvent;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donation.HaemoglobinLevel;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.packtype.PackType;

public class DonationBuilder extends AbstractEntityBuilder<Donation> {
    
    private Long id;
    private Donor donor;
    private Date donationDate;
    private Location venue;
    private String donationIdentificationNumber;
    private TTIStatus ttiStatus;
    private Boolean deleted;
    private Integer donorPulse;
    private BigDecimal haemoglobinCount;
    private HaemoglobinLevel haemoglobinLevel;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private BigDecimal donorWeight;
    private String notes;
    private PackType packType;
    private Date bleedStartTime;
    private Date bleedEndTime;
    private AdverseEvent adverseEvent;
    private DonationType donationType;
    private String bloodAbo;
    private String bloodRh;
    private List<BloodTestResult> bloodTestResults;
    private BloodTypingMatchStatus bloodTypingMatchStatus;
    private BloodTypingStatus bloodTypingStatus;
    private DonationBatch donationBatch;
    private String extraBloodTypeInformation;
    
    public DonationBuilder withId(Long id) {
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
    
  public DonationBuilder withExtraBloodTypeInformation(String extraBloodTypeInformation) {
    this.extraBloodTypeInformation = extraBloodTypeInformation;
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
        donation.setExtraBloodTypeInformation(extraBloodTypeInformation);
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
