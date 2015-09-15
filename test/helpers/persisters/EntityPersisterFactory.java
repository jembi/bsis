package helpers.persisters;

import helpers.builders.AdverseEventBuilder;
import helpers.builders.AdverseEventTypeBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.LocationBuilder;
import model.adverseevent.AdverseEvent;
import model.adverseevent.AdverseEventType;
import model.donation.Donation;
import model.donor.Donor;
import model.location.Location;

public class EntityPersisterFactory {
    
    public static AbstractEntityPersister<Donor> aDonorPersister() {
        return new DonorBuilder().getPersister();
    }
    
    public static AbstractEntityPersister<Donation> aDonationPersister() {
        return new DonationBuilder().getPersister();
    }
    
    public static AbstractEntityPersister<Location> aLocationPersister() {
        return new LocationBuilder().getPersister();
    }
    
    public static AbstractEntityPersister<AdverseEvent> anAdverseEventPersister() {
        return new AdverseEventBuilder().getPersister();
    }
    
    public static AbstractEntityPersister<AdverseEventType> anAdverseEventTypePersister() {
        return new AdverseEventTypeBuilder().getPersister();
    }

}
