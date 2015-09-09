package helpers.persisters;

import helpers.builders.DonationBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.LocationBuilder;
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

}
