package helpers.builders;

import model.donationtype.DonationType;
import model.location.Location;
import model.util.Gender;
import valueobject.CollectedDonationValueObject;

public class CollectedDonationValueObjectBuilder extends AbstractBuilder<CollectedDonationValueObject> {

    private DonationType donationType;
    private Gender gender;
    private String bloodAbo;
    private String bloodRh;
    private long count;
    private Location venue;
    
    public CollectedDonationValueObjectBuilder withDonationType(DonationType donationType) {
        this.donationType = donationType;
        return this;
    }

    public CollectedDonationValueObjectBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }
    
    public CollectedDonationValueObjectBuilder withBloodAbo(String bloodAbo) {
        this.bloodAbo = bloodAbo;
        return this;
    }
    
    public CollectedDonationValueObjectBuilder withBloodRh(String bloodRh) {
        this.bloodRh = bloodRh;
        return this;
    }
    
    public CollectedDonationValueObjectBuilder withCount(long count) {
        this.count = count;
        return this;
    }
    
    public CollectedDonationValueObjectBuilder withVenue(Location venue) {
        this.venue = venue;
        return this;
    }

    @Override
    public CollectedDonationValueObject build() {
        CollectedDonationValueObject collectedDonationValueObject = new CollectedDonationValueObject();
        collectedDonationValueObject.setDonationType(donationType);
        collectedDonationValueObject.setGender(gender);
        collectedDonationValueObject.setBloodAbo(bloodAbo);
        collectedDonationValueObject.setBloodRh(bloodRh);
        collectedDonationValueObject.setCount(count);
        collectedDonationValueObject.setVenue(venue);
        return collectedDonationValueObject;
    }
    
    public static CollectedDonationValueObjectBuilder aCollectedDonationValueObject() {
        return new CollectedDonationValueObjectBuilder();
    }

}
