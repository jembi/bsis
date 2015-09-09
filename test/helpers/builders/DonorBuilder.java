package helpers.builders;

import java.util.Date;

import model.donor.Donor;

public class DonorBuilder extends AbstractEntityBuilder<Donor> {
    
    private Long id;
    private String notes;
    private Boolean deleted;
    private Date dateOfFirstDonation;
    private Date dateOfLastDonation;

    public DonorBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    
    public DonorBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }
    
    public DonorBuilder thatIsDeleted() {
        deleted = true;
        return this;
    }
    
    public DonorBuilder withDateOfFirstDonation(Date dateOfFirstDonation) {
        this.dateOfFirstDonation = dateOfFirstDonation;
        return this;
    }
    
    public DonorBuilder withDateOfLastDonation(Date dateOfLastDonation) {
        this.dateOfLastDonation = dateOfLastDonation;
        return this;
    }

    @Override
    public Donor build() {
        Donor donor = new Donor();
        donor.setId(id);
        donor.setNotes(notes);
        donor.setIsDeleted(deleted);
        donor.setDateOfFirstDonation(dateOfFirstDonation);
        donor.setDateOfLastDonation(dateOfLastDonation);
        return donor;
    }
    
    public static DonorBuilder aDonor() {
        return new DonorBuilder();
    }

}
