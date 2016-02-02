package helpers.builders;

import model.packtype.PackType;

public class PackTypeBuilder extends AbstractEntityBuilder<PackType> {

  private Long id;
  private Boolean countAsDonation;
  private Integer periodBetweenDonations;
  private Boolean testSampleProduced;

  public PackTypeBuilder withId(Long id) {
    this.id = id;
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

  @Override
  public PackType build() {
    PackType packType = new PackType();
    packType.setId(id);
    if (testSampleProduced != null) {
      packType.setTestSampleProduced(testSampleProduced);
    }
    packType.setCountAsDonation(countAsDonation);
    packType.setPeriodBetweenDonations(periodBetweenDonations);
    if (testSampleProduced != null) {
      packType.setTestSampleProduced(testSampleProduced);
    }
    return packType;
  }

  public static PackTypeBuilder aPackType() {
    return new PackTypeBuilder();
  }

}
