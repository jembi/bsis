package viewmodel;

import model.packtype.PackType;

public class PackTypeBasicViewModel {

  private Long id;
  private String packType;

  public PackTypeBasicViewModel(PackType packType) {
    this.id = packType.getId();
    this.packType = packType.getPackType();
  }

  public Long getId() {
    return id;
  }

  public String getPackType() {
    return packType;
  }
}
