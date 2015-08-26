package helpers.builders;

import model.donor.Donor;

public class DonorBuilder extends AbstractEntityBuilder<Donor> {
    
    private Long id;
    private String notes;
    private boolean deleted;

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

    @Override
    public Donor build() {
        Donor donor = new Donor();
        donor.setId(id);
        donor.setNotes(notes);
        donor.setIsDeleted(deleted);
        return donor;
    }
    
    public static DonorBuilder aDonor() {
        return new DonorBuilder();
    }

}
