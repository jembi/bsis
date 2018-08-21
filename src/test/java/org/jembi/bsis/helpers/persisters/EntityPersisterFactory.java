package org.jembi.bsis.helpers.persisters;

import org.jembi.bsis.helpers.builders.AdverseEventBuilder;
import org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder;
import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.ComponentBatchBuilder;
import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.DataTypeBuilder;
import org.jembi.bsis.helpers.builders.DeferralReasonBuilder;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.DonationTypeBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.OrderFormBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.helpers.builders.TestBatchBuilder;
import org.jembi.bsis.helpers.builders.UserBuilder;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.user.User;

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

  public static AbstractEntityPersister<Component> aComponentPersister() {
    return new ComponentBuilder().getPersister();
  }
  
  public static AbstractEntityPersister<User> aUserPersister() {
    return new UserBuilder().getPersister();
  }
  
  public static AbstractEntityPersister<BloodTest> aBloodTestPersister() {
    return new BloodTestBuilder().getPersister();
  }
  
  public static AbstractEntityPersister<ComponentStatusChangeReason> aComponentStatusChangeReasonPersister() {
    return new ComponentStatusChangeReasonBuilder().getPersister();
  }

  public static AbstractEntityPersister<TestBatch> aTestBatchPersister() {
    return new TestBatchBuilder().getPersister();
  }

}
