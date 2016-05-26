package viewmodel;

import model.compatibility.CompatibilityResult;
import model.compatibility.CompatibilityTest;
import model.component.Component;

public class MatchingComponentViewModel {

  private Component component;
  private ComponentViewModel componentViewModel;
  private CompatibilityTest compatibilityTest;

  public MatchingComponentViewModel(Component component) {
    this.component = component;
    this.componentViewModel = new ComponentViewModel(this.component);
  }

  public MatchingComponentViewModel(Component component,
                                    CompatibilityTest crossmatchTest) {
    this.component = component;
    this.componentViewModel = new ComponentViewModel(this.component);
    this.compatibilityTest = crossmatchTest;
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

  public Long getId() {
    return componentViewModel.getId();
  }

  public ComponentTypeViewModel getComponentType() {
    return componentViewModel.getComponentType();
  }

  public String getCreatedDate() {
    return componentViewModel.getCreatedDate();
  }

  public String getNotes() {
    return componentViewModel.getNotes();
  }

  public String getCreatedOn() {
    return componentViewModel.getCreatedOn();
  }

  public String getExpiresOn() {
    return componentViewModel.getExpiresOn();
  }

  public String getDonationIdentificationNumber() {
    return componentViewModel.getDonationIdentificationNumber();
  }

  public String getIsCompatible() {
    if (compatibilityTest == null)
      return CompatibilityResult.NOT_KNOWN.toString();
    return compatibilityTest.getCompatibilityResult().toString();
  }

}
