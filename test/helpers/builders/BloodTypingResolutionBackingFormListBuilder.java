package helpers.builders;

import java.util.ArrayList;
import java.util.List;

import backingform.BloodTypingResolutionBackingForm;
import backingform.BloodTypingResolutionBackingFormList;

public class BloodTypingResolutionBackingFormListBuilder extends AbstractBuilder<BloodTypingResolutionBackingFormList> {
  
  private List<BloodTypingResolutionBackingForm> bloodTypingResolutions;

  public BloodTypingResolutionBackingFormListBuilder withBloodTypingResolutions(List<BloodTypingResolutionBackingForm> bloodTypingResolutions) {
    this.bloodTypingResolutions = bloodTypingResolutions;
    return this;
  }

  public BloodTypingResolutionBackingFormListBuilder withBloodTypingResolution(BloodTypingResolutionBackingForm bloodTypingResolution) {
    if (bloodTypingResolutions == null) {
      bloodTypingResolutions = new ArrayList<>();
    }
    bloodTypingResolutions.add(bloodTypingResolution);
    return this;
  }

  @Override
  public BloodTypingResolutionBackingFormList build() {
    BloodTypingResolutionBackingFormList forms = new BloodTypingResolutionBackingFormList();
    forms.setBloodTypingResolutions(bloodTypingResolutions);
    return forms;
  }
  
  public static BloodTypingResolutionBackingFormListBuilder aBloodTypingResolutionBackingFormList() {
    return new BloodTypingResolutionBackingFormListBuilder();
  }

}
