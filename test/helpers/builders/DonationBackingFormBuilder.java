package helpers.builders;

import java.math.BigDecimal;

import model.donation.HaemoglobinLevel;
import backingform.DonationBackingForm;

public class DonationBackingFormBuilder extends AbstractBuilder<DonationBackingForm> {

    private Integer donorPulse;
    private BigDecimal haemoglobinCount;
    private HaemoglobinLevel haemoglobinLevel;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private BigDecimal donorWeight;
    private String notes;
    
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
        return donationBackingForm;
    }
    
    public static DonationBackingFormBuilder aDonationBackingForm() {
        return new DonationBackingFormBuilder();
    }

}
