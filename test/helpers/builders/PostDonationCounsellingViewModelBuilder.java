package helpers.builders;

import model.counselling.PostDonationCounselling;
import viewmodel.PostDonationCounsellingViewModel;

import java.util.HashMap;
import java.util.Map;

public class PostDonationCounsellingViewModelBuilder extends AbstractBuilder<PostDonationCounsellingViewModel> {

  private PostDonationCounselling postDonationCounselling;
  private Map<String, Boolean> permissions;

  public static PostDonationCounsellingViewModelBuilder aPostDonationCounsellingViewModel() {
    return new PostDonationCounsellingViewModelBuilder();
  }

  public PostDonationCounsellingViewModelBuilder withPostDonationCounselling(PostDonationCounselling postDonationCounselling) {
    this.postDonationCounselling = postDonationCounselling;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withPermission(String key, boolean value) {
    if (permissions == null) {
      permissions = new HashMap<>();
    }
    permissions.put(key, value);
    return this;
  }

  @Override
  public PostDonationCounsellingViewModel build() {
    PostDonationCounsellingViewModel postDonationCounsellingViewModel = new PostDonationCounsellingViewModel(postDonationCounselling);
    postDonationCounsellingViewModel.setPermissions(permissions);
    return postDonationCounsellingViewModel;
  }

}
