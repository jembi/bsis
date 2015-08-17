package viewmodel;

import model.compatibility.CompatibilityResult;
import model.compatibility.CompatibilityTest;
import model.component.Component;
import model.donation.Donation;

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

  public Donation getDonation() {
    return componentViewModel.getDonation();
  }

  public ProductTypeViewModel getProductType() {
    return componentViewModel.getProductType();
  }

  public String getLastUpdated() {
    return componentViewModel.getLastUpdated();
  }

  public String getCreatedDate() {
    return componentViewModel.getCreatedDate();
  }

  public String getCreatedBy() {
    return componentViewModel.getCreatedBy();
  }

  public String getLastUpdatedBy() {
    return componentViewModel.getLastUpdatedBy();
  }

  public String getNotes() {
    return componentViewModel.getNotes();
  }

  public Boolean getIsDeleted() {
    return componentViewModel.getIsDeleted();
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

  public String getAge() {
    return componentViewModel.getAge();
  }

  public String getIsCompatible() {
    if (compatibilityTest == null)
      return CompatibilityResult.NOT_KNOWN.toString();
    return compatibilityTest.getCompatibilityResult().toString();
  }

  public String getBloodGroup() {
    return componentViewModel.getBloodGroup();
  }

  public String getSubdivisionCode() {
    return componentViewModel.getSubdivisionCode();
  }
}
