package backingform;

public class BloodTypingResolutionBackingForm {

  private String bloodAbo;

  private String bloodRh;

  private FinalBloodTypingMatchStatus status;

  public BloodTypingResolutionBackingForm() {
    super();
  }

  public enum FinalBloodTypingMatchStatus {
    RESOLVED, NO_TYPE_DETERMINED;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public FinalBloodTypingMatchStatus getStatus() {
    return status;
  }

  public void setStatus(FinalBloodTypingMatchStatus status) {
    this.status = status;
  }

}
