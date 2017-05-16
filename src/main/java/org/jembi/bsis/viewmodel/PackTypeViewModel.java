package org.jembi.bsis.viewmodel;

import java.util.UUID;

public class PackTypeViewModel extends BaseViewModel<UUID> {

  private String packType;

  public String getPackType() {
    return packType;
  }

  public void setPackType(String packType) {
    this.packType = packType;
  }

}
