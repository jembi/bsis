package helpers.builders;

import model.donor.Donor;

public class DonorBuilder extends AbstractEntityBuilder<Donor> {
    
    private Long id;

    public DonorBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Donor build() {
        Donor donor = new Donor();
        donor.setId(id);
        return donor;
    }
    
    public static DonorBuilder aDonor() {
        return new DonorBuilder();
    }

}
