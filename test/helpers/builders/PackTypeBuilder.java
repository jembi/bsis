package helpers.builders;

import model.packtype.PackType;

public class PackTypeBuilder extends AbstractEntityBuilder<PackType> {
    
    private Integer id;
    private Integer periodBetweenDonations;

    public PackTypeBuilder withId(Integer id) {
        this.id = id;
        return this;
    }
    
    public PackTypeBuilder withPeriodBetweenDonations(Integer periodBetweenDonations) {
    	this.periodBetweenDonations = periodBetweenDonations;
    	return this;
    }

    @Override
    public PackType build() {
        PackType packType = new PackType();
        packType.setId(id);
        packType.setPeriodBetweenDonations(periodBetweenDonations);
        return packType;
    }
    
    public static PackTypeBuilder aPackType() {
        return new PackTypeBuilder();
    }

}
