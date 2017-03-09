package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.InventoryFullViewModel;

public class InventoryFullViewModelMatcher extends TypeSafeMatcher<InventoryFullViewModel> {
  private InventoryFullViewModel expected;

  public InventoryFullViewModelMatcher(InventoryFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An InventoryFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nComponentCode: ").appendValue(expected.getComponentCode())
        .appendText("\nComponentType: ").appendValue(expected.getComponentType())
        .appendText("\nInventoryStatus: ").appendValue(expected.getInventoryStatus())
        .appendText("\nLocation: ").appendValue(expected.getLocation())
        .appendText("\ncreatedOn: ").appendValue(expected.getCreatedOn())
        .appendText("\ndonationIdentificationNumber: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nexpiryStatus: ").appendValue(expected.getExpiryStatus())
        .appendText("\nBloodGroup: ").appendValue(expected.getBloodGroup())
        .appendText("\nexpiresOn: ").appendValue(expected.getExpiresOn())
        .appendText("\nOrderForm: ").appendValue(expected.getOrderForm())
        .appendText("\ncomponentStatus: ").appendValue(expected.getComponentStatus());
  }

  @Override
  public boolean matchesSafely(InventoryFullViewModel actual) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getComponentCode(), expected.getComponentCode()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getInventoryStatus(), expected.getInventoryStatus()) &&
        (Objects.equals(actual.getCreatedOn(), expected.getCreatedOn())
            || Objects.equals(sdf.format(actual.getCreatedOn()), sdf.format(expected.getCreatedOn()))) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getExpiryStatus(), expected.getExpiryStatus()) &&
        (Objects.equals(actual.getExpiresOn(), expected.getExpiresOn())
            || Objects.equals(sdf.format(actual.getExpiresOn()), sdf.format(expected.getExpiresOn()))) &&
        Objects.equals(actual.getLocation(), expected.getLocation()) &&
        Objects.equals(actual.getBloodGroup(), expected.getBloodGroup()) &&
        Objects.equals(actual.getOrderForm(), expected.getOrderForm()) &&
        Objects.equals(actual.getComponentStatus(), expected.getComponentStatus());
  }

  public static InventoryFullViewModelMatcher hasSameStateAsInventoryFullViewModel(InventoryFullViewModel expected) {
    return new InventoryFullViewModelMatcher(expected);
  }
}
