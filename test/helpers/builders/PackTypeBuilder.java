package helpers.builders;

import model.packtype.PackType;

public class PackTypeBuilder extends AbstractEntityBuilder<PackType> {
    
    private Integer id;
    private Boolean countAsDonation;
    private Integer periodBetweenDonations;

    public PackTypeBuilder withId(Integer id) {
        this.id = id;
        return this;
    }
    
    public PackTypeBuilder withCountAsDonation(boolean countAsDonation) {
        this.countAsDonation = countAsDonation;
        return this;
    }
    
    public PackTypeBuilder withPeriodBetweenDonations(int periodBetweenDonations) {
        this.periodBetweenDonations = periodBetweenDonations;
        return this;
    }

    @Override
    public PackType build() {
        PackType packType = new PackType();
        packType.setId(id);
        packType.setCountAsDonation(countAsDonation);
        packType.setPeriodBetweenDonations(periodBetweenDonations);
        return packType;
    }
    
    public static PackTypeBuilder aPackType() {
        return new PackTypeBuilder();
    }

}
