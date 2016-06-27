package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.ComponentViewModel;

public class ComponentViewModelMatcher extends TypeSafeMatcher<ComponentViewModel> {

  private ComponentViewModel expected;

  public ComponentViewModelMatcher(ComponentViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A ComponentViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nComponentCode: ").appendValue(expected.getComponentCode())
        .appendText("\nComponentType: ").appendValue(expected.getComponentType())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nInventoryStatus: ").appendValue(expected.getInventoryStatus())
        .appendText("\nLocation: ").appendValue(expected.getLocation())
        .appendText("\nCreatedOn: ").appendValue(expected.getCreatedOn())
        .appendText("\nExpiresOn: ").appendValue(expected.getExpiresOn())
        .appendText("\nBlood ABO: ").appendValue(expected.getBloodAbo())
        .appendText("\nBlood Rh: ").appendValue(expected.getBloodRh())
        ;
  }

  @Override
  public boolean matchesSafely(ComponentViewModel actual) {
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
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo());
  }

  public static ComponentViewModelMatcher hasSameStateAsComponentViewModel(ComponentViewModel expected) {
    return new ComponentViewModelMatcher(expected);
  }

}
