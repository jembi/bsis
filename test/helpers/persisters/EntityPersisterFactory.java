package helpers.persisters;

import helpers.builders.*;
import model.adverseevent.AdverseEvent;
import model.adverseevent.AdverseEventType;
import model.donation.Donation;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.location.Location;
import model.packtype.PackType;

class EntityPersisterFactory {

  public static AbstractEntityPersister<Donor> aDonorPersister() {
    return new DonorBuilder().getPersister();
  }

  public static AbstractEntityPersister<Donation> aDonationPersister() {
    return new DonationBuilder().getPersister();
  }

  public static AbstractEntityPersister<DonationType> aDonationTypePersister() {
    return new DonationTypeBuilder().getPersister();
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

  public static AbstractEntityPersister<DeferralReason> aDeferralReasonPersister() {
    return new DeferralReasonBuilder().getPersister();
  }

  public static AbstractEntityPersister<PackType> aPackTypePersister() {
    return new PackTypeBuilder().getPersister();
  }

}
