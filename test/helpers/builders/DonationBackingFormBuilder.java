package helpers.builders;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import model.adverseevent.AdverseEvent;
import model.donation.HaemoglobinLevel;
import model.packtype.PackType;
import backingform.DonationBackingForm;

public class DonationBackingFormBuilder extends AbstractBuilder<DonationBackingForm> {
    
    private static final DateTimeFormatter ISO_FORMAT = ISODateTimeFormat.dateTime();

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
    
    public DonationBackingFormBuilder withDonorPulse(Integer donorPulse) {
        this.donorPulse = donorPulse;
        return this;
    }
    
    public DonationBackingFormBuilder withHaemoglobinCount(BigDecimal haemoglobinCount) {
        this.haemoglobinCount = haemoglobinCount;
        return this;
    }
    
    public DonationBackingFormBuilder withHaemoglobinLevel(HaemoglobinLevel haemoglobinLevel) {
        this.haemoglobinLevel = haemoglobinLevel;
        return this;
    }
    
    public DonationBackingFormBuilder withBloodPressureSystolic(Integer bloodPressureSystolic) {
        this.bloodPressureSystolic = bloodPressureSystolic;
        return this;
    }
    
    public DonationBackingFormBuilder withBloodPressureDiastolic(Integer bloodPressureDiastolic) {
        this.bloodPressureDiastolic = bloodPressureDiastolic;
        return this;
    }
    
    public DonationBackingFormBuilder withDonorWeight(BigDecimal donorWeight) {
        this.donorWeight = donorWeight;
        return this;
    }
    
    public DonationBackingFormBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }
    
    public DonationBackingFormBuilder withPackType(PackType packType) {
        this.packType = packType;
        return this;
    }
    
    public DonationBackingFormBuilder withBleedStartTime(Date bleedStartTime) {
        this.bleedStartTime = bleedStartTime;
        return this;
    }
    
    public DonationBackingFormBuilder withBleedEndTime(Date bleedEndTime) {
        this.bleedEndTime = bleedEndTime;
        return this;
    }
    
    public DonationBackingFormBuilder withAdverseEvent(AdverseEvent adverseEvent) {
        this.adverseEvent = adverseEvent;
        return this;
    }

    @Override
    public DonationBackingForm build() {
        DonationBackingForm donationBackingForm = new DonationBackingForm();
        donationBackingForm.setDonorPulse(donorPulse);
        donationBackingForm.setHaemoglobinCount(haemoglobinCount);
        donationBackingForm.setHaemoglobinLevel(haemoglobinLevel);
        donationBackingForm.setBloodPressureSystolic(bloodPressureSystolic);
        donationBackingForm.setBloodPressureDiastolic(bloodPressureDiastolic);
        donationBackingForm.setDonorWeight(donorWeight);
        donationBackingForm.setNotes(notes);
        donationBackingForm.setPackType(packType);
        if (bleedStartTime != null) {
            donationBackingForm.setBleedStartTime(ISO_FORMAT.print(bleedStartTime.getTime()));
        }
        if (bleedEndTime != null) {
            donationBackingForm.setBleedEndTime(ISO_FORMAT.print(bleedEndTime.getTime()));
        }
        donationBackingForm.setAdverseEvent(adverseEvent);
        return donationBackingForm;
    }
    
    public static DonationBackingFormBuilder aDonationBackingForm() {
        return new DonationBackingFormBuilder();
    }

}
