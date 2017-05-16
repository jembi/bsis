package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;

public class DeferralReasonViewModelBuilder extends AbstractBuilder<DeferralReasonViewModel> {

  private UUID id;
  private String reason;
  private Integer defaultDuration;
  private DurationType durationType = DurationType.TEMPORARY;
  private boolean isDeleted = false;
  
  public DeferralReasonViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public DeferralReasonViewModelBuilder withReason(String reason) {
    this.reason = reason;
    return this;
  }

  public DeferralReasonViewModelBuilder withDefaultDuration(Integer defaultDuration) {
    this.defaultDuration = defaultDuration;
    return this;
  }

  public DeferralReasonViewModelBuilder withDurationType(DurationType durationType) {
    this.durationType = durationType;
    return this;
  }
  
  public DeferralReasonViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public DeferralReasonViewModel build() {
    DeferralReasonViewModel viewModel = new DeferralReasonViewModel();
    viewModel.setReason(reason);
    viewModel.setDefaultDuration(defaultDuration);
    viewModel.setDurationType(durationType);
    viewModel.setId(id);
    viewModel.setIsDeleted(isDeleted);
    return viewModel;
  }

  public static DeferralReasonViewModelBuilder aDeferralReasonViewModel() {
    return new DeferralReasonViewModelBuilder();
  }

}
