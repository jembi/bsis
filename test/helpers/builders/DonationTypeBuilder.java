package helpers.builders;

import model.donationtype.DonationType;

public class DonationTypeBuilder extends AbstractEntityBuilder<DonationType> {
    
    private Integer id;
    private String name;

    public DonationTypeBuilder withId(Integer id) {
        this.id = id;
        return this;
    }
    
    public DonationTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DonationType build() {
        DonationType donationType = new DonationType();
        donationType.setId(id);
        donationType.setDonationType(name);
        return donationType;
    }
    
    public static DonationTypeBuilder aDonationType() {
        return new DonationTypeBuilder();
    }

}
