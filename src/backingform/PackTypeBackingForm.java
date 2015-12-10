package backingform;

import model.componenttype.ComponentType;
import model.packtype.PackType;

import javax.validation.Valid;

public class PackTypeBackingForm {

  @Valid
  private PackType type;

  public PackTypeBackingForm() {
    type = new PackType();
  }

  public PackType getType() {
    return type;
  }

  public void setType(PackType packType) {
    this.type = packType;
  }

  public void setId(Integer id) {
    type.setId(id);
  }

  public String getPackType() {
    return type.getPackType();
  }

  public void setPackType(String packTypeStr) {
    type.setPackType(packTypeStr);
  }

  public void setComponentType(ComponentType componentType) {
    type.setComponentType(componentType);
  }

  public void setCanPool(Boolean canPool) {
    type.setCanPool(canPool);
  }

  public void setCanSplit(Boolean canSplit) {
    type.setCanSplit(canSplit);
  }

  public void setIsDeleted(Boolean isDeleted) {
    type.setIsDeleted(isDeleted);
  }

  public void setCountAsDonation(Boolean countAsDonation) {
    type.setCountAsDonation(countAsDonation);
  }

  public void setTestSampleProduced(Boolean testSampleProduced) {
    type.setTestSampleProduced(testSampleProduced);
  }

  public void setPeriodBetweenDonations(Integer periodBetweenDonations) {
    type.setPeriodBetweenDonations(periodBetweenDonations);
  }

}
