package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.PackTypePersister;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.packtype.PackType;

public class PackTypeBuilder extends AbstractEntityBuilder<PackType> {

  private Long id;
  private String type;
  private Boolean countAsDonation;
  private int periodBetweenDonations;
  private Boolean testSampleProduced;
  private Boolean isDeleted;
  private ComponentType componentType = aComponentType().build();

  public PackTypeBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public PackTypeBuilder withPackType(String packType) {
    this.type = packType;
    return this;
  }

  public PackTypeBuilder withTestSampleProduced(boolean testSampleProduced) {
    this.testSampleProduced = testSampleProduced;
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
  
  public PackTypeBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }
  
  public PackTypeBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  public PackTypeBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }

  @Override
  public AbstractEntityPersister<PackType> getPersister() {
    return new PackTypePersister();
  }

  @Override
  public PackType build() {
    PackType packType = new PackType();
    packType.setId(id);
    packType.setPackType(type);
    if (testSampleProduced != null) {
      packType.setTestSampleProduced(testSampleProduced);
    }
    packType.setCountAsDonation(countAsDonation);
    packType.setPeriodBetweenDonations(periodBetweenDonations);
    if (testSampleProduced != null) {
      packType.setTestSampleProduced(testSampleProduced);
    }
    packType.setComponentType(componentType);
    packType.setIsDeleted(isDeleted);
    return packType;
  }

  public static PackTypeBuilder aPackType() {
    return new PackTypeBuilder();
  }

}