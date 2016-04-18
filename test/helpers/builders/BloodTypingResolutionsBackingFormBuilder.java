package helpers.builders;

import java.util.ArrayList;
import java.util.List;

import backingform.BloodTypingResolutionBackingForm;
import backingform.BloodTypingResolutionsBackingForm;

public class BloodTypingResolutionsBackingFormBuilder extends AbstractBuilder<BloodTypingResolutionsBackingForm> {
  
  private List<BloodTypingResolutionBackingForm> bloodTypingResolutions;

  public BloodTypingResolutionsBackingFormBuilder withBloodTypingResolutions(List<BloodTypingResolutionBackingForm> bloodTypingResolutions) {
    this.bloodTypingResolutions = bloodTypingResolutions;
    return this;
  }

  public BloodTypingResolutionsBackingFormBuilder withBloodTypingResolution(BloodTypingResolutionBackingForm bloodTypingResolution) {
    if (bloodTypingResolutions == null) {
      bloodTypingResolutions = new ArrayList<>();
    }
    bloodTypingResolutions.add(bloodTypingResolution);
    return this;
  }

  @Override
  public BloodTypingResolutionsBackingForm build() {
    BloodTypingResolutionsBackingForm forms = new BloodTypingResolutionsBackingForm();
    forms.setBloodTypingResolutions(bloodTypingResolutions);
    return forms;
  }
  
  public static BloodTypingResolutionsBackingFormBuilder aBloodTypingResolutionsBackingForm() {
    return new BloodTypingResolutionsBackingFormBuilder();
  }

}
