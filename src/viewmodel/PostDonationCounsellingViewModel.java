package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import model.counselling.PostDonationCounselling;
import utils.DateTimeSerialiser;

import java.util.Date;
import java.util.Map;

public class PostDonationCounsellingViewModel {

  private PostDonationCounselling postDonationCounselling;

  private Map<String, Boolean> permissions;

  public PostDonationCounsellingViewModel(PostDonationCounselling postDonationCounselling) {
    this.postDonationCounselling = postDonationCounselling;
  }

  public long getId() {
    return postDonationCounselling.getId();
  }

  public boolean isFlaggedForCounselling() {
    return postDonationCounselling.isFlaggedForCounselling();
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCounsellingDate() {
    return postDonationCounselling.getCounsellingDate();
  }

  public CounsellingStatusViewModel getCounsellingStatus() {
    if (postDonationCounselling.getCounsellingStatus() == null) {
      return null;
    }
    return new CounsellingStatusViewModel(postDonationCounselling.getCounsellingStatus());
  }

  public String getNotes() {
    return postDonationCounselling.getDonation().getNotes();
  }

  public DonationViewModel getDonation() {
    return new DonationViewModel(postDonationCounselling.getDonation());
  }

  public DonorViewModel getDonor() {
    return new DonorViewModel(postDonationCounselling.getDonation().getDonor());
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  @JsonIgnore
  public PostDonationCounselling getPostDonationCounselling() {
    return postDonationCounselling;
  }
}
