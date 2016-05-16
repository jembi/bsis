package helpers.persisters;

import helpers.builders.AdverseEventBuilder;
import helpers.builders.AdverseEventTypeBuilder;
import helpers.builders.ComponentBatchBuilder;
import helpers.builders.ComponentTypeBuilder;
import helpers.builders.DataTypeBuilder;
import helpers.builders.DeferralReasonBuilder;
import helpers.builders.DonationBatchBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.DonationTypeBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.OrderFormBuilder;
import helpers.builders.PackTypeBuilder;
import model.admin.DataType;
import model.adverseevent.AdverseEvent;
import model.adverseevent.AdverseEventType;
import model.componentbatch.ComponentBatch;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.location.Location;
import model.order.OrderForm;
import model.packtype.PackType;

public class EntityPersisterFactory {

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

  public static AbstractEntityPersister<DataType> aDataTypePersister() {
    return new DataTypeBuilder().getPersister();
  }

  public static AbstractEntityPersister<DonationBatch> aDonationBatchPersister() {
    return new DonationBatchBuilder().getPersister();
  }

  public static AbstractEntityPersister<ComponentType> aComponentTypePersister() {
    return new ComponentTypeBuilder().getPersister();
  }

  public static AbstractEntityPersister<ComponentBatch> aComponentBatchPersister() {
    return new ComponentBatchBuilder().getPersister();
  }

  public static AbstractEntityPersister<OrderForm> anOrderFormPersister() {
    return new OrderFormBuilder().getPersister();
  }

}
