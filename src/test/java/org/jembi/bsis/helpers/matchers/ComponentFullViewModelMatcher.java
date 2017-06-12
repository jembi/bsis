package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;

public class ComponentFullViewModelMatcher extends AbstractTypeSafeMatcher<ComponentFullViewModel> {

  public ComponentFullViewModelMatcher(ComponentFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void appendDescription(Description description, ComponentFullViewModel model) {
    description.appendText("A ComponentViewModel with the following state:")
        .appendText("\nId: ").appendValue(model.getId())
        .appendText("\nComponentCode: ").appendValue(model.getComponentCode())
        .appendText("\nComponentType: ").appendValue(model.getComponentType())
        .appendText("\nStatus: ").appendValue(model.getStatus())
        .appendText("\nInventoryStatus: ").appendValue(model.getInventoryStatus())
        .appendText("\nLocation: ").appendValue(model.getLocation())
        .appendText("\nCreatedOn: ").appendValue(model.getCreatedOn())
        .appendText("\nExpiresOn: ").appendValue(model.getExpiresOn())
        .appendText("\nBlood ABO: ").appendValue(model.getBloodAbo())
        .appendText("\nBlood Rh: ").appendValue(model.getBloodRh())
        .appendText("\nIs InitialComponent: ").appendValue(model.getIsInitialComponent())
        ;
  }

  @Override
  public boolean matchesSafely(ComponentFullViewModel actual) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getComponentCode(), expected.getComponentCode()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getInventoryStatus(), expected.getInventoryStatus()) &&
        Objects.equals(actual.getLocation(), expected.getLocation()) &&
        (Objects.equals(actual.getCreatedOn(), expected.getCreatedOn()) || Objects.equals(sdf.format(actual.getCreatedOn()), sdf.format(expected.getCreatedOn()))) &&
        (Objects.equals(actual.getExpiresOn(), expected.getExpiresOn()) || Objects.equals(sdf.format(actual.getExpiresOn()), sdf.format(expected.getExpiresOn()))) &&
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo()) &&
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo()) &&
        Objects.equals(actual.getIsInitialComponent(), expected.getIsInitialComponent());
  }

  public static ComponentFullViewModelMatcher hasSameStateAsComponentFullViewModel(ComponentFullViewModel expected) {
    return new ComponentFullViewModelMatcher(expected);
  }
}
