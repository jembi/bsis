package helpers.builders;

import java.math.BigDecimal;
import java.util.Date;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.DonationPersister;
import model.adverseevent.AdverseEvent;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donation.HaemoglobinLevel;
import model.donor.Donor;
import model.location.Location;
import model.packtype.PackType;

public class DonationBuilder extends AbstractEntityBuilder<Donation> {
    
    private Long id;
    private Donor donor;
    private Date donationDate;
    private Location donorPanel;
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
    
    public DonationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DonationBuilder withDonor(Donor donor) {
        this.donor = donor;
        return this;
    }
    
    public DonationBuilder withDonationDate(Date donationDate) {
        this.donationDate = donationDate;
        return this;
    }
    
    public DonationBuilder withDonorPanel(Location donorPanel) {
        this.donorPanel = donorPanel;
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

    @Override
    public Donation build() {
        Donation donation = new Donation();
        donation.setId(id);
        donation.setDonor(donor);
        donation.setDonationDate(donationDate);
        donation.setDonorPanel(donorPanel);
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
