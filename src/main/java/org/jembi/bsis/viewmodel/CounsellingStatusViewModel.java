package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.counselling.CounsellingStatus;

public class CounsellingStatusViewModel {

  private CounsellingStatus counsellingStatus;

  public CounsellingStatusViewModel(CounsellingStatus counsellingStatus) {
    this.counsellingStatus = counsellingStatus;
  }

  public int getId() {
    return counsellingStatus.getId();
  }

  public String getLabel() {
    return counsellingStatus.getLabel();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    return other instanceof CounsellingStatusViewModel &&
        ((CounsellingStatusViewModel) other).counsellingStatus == counsellingStatus;
  }

}
