package viewmodel;

import model.counselling.CounsellingStatus;

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
    return other == this || other instanceof CounsellingStatusViewModel && ((CounsellingStatusViewModel) other).counsellingStatus == counsellingStatus;
  }

}
