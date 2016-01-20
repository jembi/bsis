package helpers.builders;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.ComponentPersister;
import model.component.Component;
import model.component.ComponentStatus;
import model.donation.Donation;

public class ComponentBuilder extends AbstractEntityBuilder<Component> {

  private ComponentStatus status;
  private Donation donation;

  public static ComponentBuilder aComponent() {
    return new ComponentBuilder();
  }

  public ComponentBuilder withStatus(ComponentStatus status) {
    this.status = status;
    return this;
  }

  public ComponentBuilder withDonation(Donation donation) {
    this.donation = donation;
    return this;
  }

  @Override
  public Component build() {
    Component component = new Component();
    component.setStatus(status);
    component.setDonation(donation);
    return component;
  }

  @Override
  public AbstractEntityPersister<Component> getPersister() {
    return new ComponentPersister();
  }

}
