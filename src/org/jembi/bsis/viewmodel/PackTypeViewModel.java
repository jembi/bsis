package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.packtype.PackType;

public class PackTypeViewModel {

  private Long id;
  private String packType;

  public PackTypeViewModel(PackType packType) {
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
