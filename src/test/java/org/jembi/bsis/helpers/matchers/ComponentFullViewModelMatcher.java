package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;

public class ComponentFullViewModelMatcher extends AbstractTypeSafeMatcher<ComponentFullViewModel> {

  public ComponentFullViewModelMatcher(ComponentFullViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, ComponentFullViewModel model) {
    description.appendText("A ComponentFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(model.getId())
        .appendText("\nComponentCode: ").appendValue(model.getComponentCode())
        .appendText("\nComponentType: ").appendValue(model.getComponentType())
        .appendText("\ndonationIdentificationNumber: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\ndonationFlagCharacters: ").appendValue(expected.getDonationFlagCharacters())
        .appendText("\nStatus: ").appendValue(model.getStatus())
        .appendText("\nInventoryStatus: ").appendValue(model.getInventoryStatus())
        .appendText("\nLocation: ").appendValue(model.getLocation())
        .appendText("\nCreatedDate: ").appendValue(model.getCreatedDate())
        .appendText("\nCreatedOn: ").appendValue(model.getCreatedOn())
        .appendText("\nExpiresOn: ").appendValue(model.getExpiresOn())
        .appendText("\nIssuedOn: ").appendValue(model.getIssuedOn())
        .appendText("\nDiscardedOn: ").appendValue(model.getDiscardedOn())
        .appendText("\ndaysToExpire: ").appendValue(expected.getDaysToExpire())
        .appendText("\nBlood ABO: ").appendValue(model.getBloodAbo())
        .appendText("\nBlood Rh: ").appendValue(model.getBloodRh())
        .appendText("\nIs InitialComponent: ").appendValue(model.getIsInitialComponent())
        .appendText("\nNotes: ").appendValue(model.getNotes())
        ;
  }

  @Override
  public boolean matchesSafely(ComponentFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getComponentCode(), expected.getComponentCode()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getDonationFlagCharacters(), expected.getDonationFlagCharacters()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getInventoryStatus(), expected.getInventoryStatus()) &&
        Objects.equals(actual.getLocation(), expected.getLocation()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getCreatedOn(), expected.getCreatedOn()) &&
        Objects.equals(actual.getExpiresOn(), expected.getExpiresOn()) &&
        Objects.equals(actual.getIssuedOn(), expected.getIssuedOn()) &&
        Objects.equals(actual.getDiscardedOn(), expected.getDiscardedOn()) &&
        Objects.equals(actual.getDaysToExpire(), expected.getDaysToExpire()) &&
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo()) &&
        Objects.equals(actual.getBloodRh(), expected.getBloodRh()) &&
        Objects.equals(actual.getIsInitialComponent(), expected.getIsInitialComponent()) &&
        Objects.equals(actual.getNotes(), expected.getNotes());
  }

  public static ComponentFullViewModelMatcher hasSameStateAsComponentFullViewModel(ComponentFullViewModel expected) {
    return new ComponentFullViewModelMatcher(expected);
  }
}
