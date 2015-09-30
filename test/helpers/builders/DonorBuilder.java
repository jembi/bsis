package helpers.builders;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.DonorPersister;
import java.util.Date;
import model.donor.Donor;
import model.location.Location;
import model.util.Gender;

public class DonorBuilder extends AbstractEntityBuilder<Donor> {
    
    private Long id;
    private String notes;
    private Boolean deleted;
    private Date dateOfFirstDonation;
    private Date dateOfLastDonation;
    private Location venue;
    private Gender gender;

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
    
    public DonorBuilder withVenue(Location venue) {
        this.venue = venue;
        return this;
    }
    
    public DonorBuilder withGender(Gender gender) {
        this.gender = gender;
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
        donor.setVenue(venue);
        donor.setGender(gender);
        return donor;
    }

    @Override
    public AbstractEntityPersister<Donor> getPersister() {
        return new DonorPersister();
    }
    
    public static DonorBuilder aDonor() {
        return new DonorBuilder();
    }

}
