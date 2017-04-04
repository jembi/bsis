package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;

import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.PackTypePersister;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.packtype.PackType;

public class PackTypeBuilder extends AbstractEntityBuilder<PackType> {

  private UUID id;
  private String type;
  private Boolean countAsDonation = Boolean.TRUE;;
  private int periodBetweenDonations;
  private Boolean isDeleted;
  private ComponentType componentType = aComponentType().build();
  private Integer maxWeight;
  private Integer minWeight;
  private Integer lowVolumeWeight;
  private Boolean testSampleProduced = Boolean.TRUE;
  private Boolean canPool;
  private Boolean canSplit;

  public PackTypeBuilder withId(UUID id) {
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
  
  public PackTypeBuilder withCanPool(Boolean canPool) {
    this.canPool = canPool;
    return this;
  }

  public PackTypeBuilder withCanSplit(Boolean canSplit) {
    this.canSplit = canSplit;
    return this;
  }

  public PackTypeBuilder withIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  public PackTypeBuilder withMaxWeight(Integer maxWeight) {
    this.maxWeight = maxWeight;
    return this;
  }

  public PackTypeBuilder withMinWeight(Integer minWeight) {
    this.minWeight = minWeight;
    return this;
  }

  public PackTypeBuilder withLowVolumeWeight(Integer lowVolumeWeight) {
    this.lowVolumeWeight = lowVolumeWeight;
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
    packType.setTestSampleProduced(testSampleProduced);
    packType.setCountAsDonation(countAsDonation);
    packType.setPeriodBetweenDonations(periodBetweenDonations);
    packType.setComponentType(componentType);
    packType.setIsDeleted(isDeleted);
    packType.setMaxWeight(maxWeight);
    packType.setMinWeight(minWeight);
    packType.setLowVolumeWeight(lowVolumeWeight);
    packType.setCanPool(canPool);
    packType.setCanSplit(canSplit);
    return packType;
  }

  public static PackTypeBuilder aPackType() {
    return new PackTypeBuilder();
  }

}
