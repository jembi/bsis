package viewmodel;

import model.packtype.PackType;

public class PackTypeViewModel {

  private PackType packType;

  public PackTypeViewModel(PackType packType) {
    this.packType = packType;
  }

  public Integer getId() {
    return packType.getId();
  }

  public String getPackType() {
    return packType.getPackType();
  }

  public ComponentTypeViewModel getComponentType() {
    if (packType.getComponentType() != null) {
      return new ComponentTypeViewModel(packType.getComponentType());
    } else {
      return null;
    }
  }

  public Boolean getCanPool() {
    return packType.getCanPool();
  }

  public Boolean getCanSplit() {
    return packType.getCanSplit();
  }

  public Boolean getIsDeleted() {
    return packType.getIsDeleted();
  }

  public Boolean getCountAsDonation() {
    return packType.getCountAsDonation();
  }

  public Boolean getTestSampleProduced() {
    return packType.getTestSampleProduced();
  }

  public Integer getPeriodBetweenDonations() {
    return packType.getPeriodBetweenDonations();
  }

  @Override
  public String toString() {
    return packType.toString();
  }

}
