package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.InventoryFullViewModel;

public class InventoryFullViewModelMatcher extends AbstractTypeSafeMatcher<InventoryFullViewModel> {

  public InventoryFullViewModelMatcher(InventoryFullViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, InventoryFullViewModel inventoryFullViewModel) {
    description.appendText("An InventoryFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(inventoryFullViewModel.getId())
        .appendText("\nComponentCode: ").appendValue(inventoryFullViewModel.getComponentCode())
        .appendText("\nComponentType: ").appendValue(inventoryFullViewModel.getComponentType())
        .appendText("\nInventoryStatus: ").appendValue(inventoryFullViewModel.getInventoryStatus())
        .appendText("\nLocation: ").appendValue(inventoryFullViewModel.getLocation())
        .appendText("\ncreatedOn: ").appendValue(inventoryFullViewModel.getCreatedOn())
        .appendText("\ndonationIdentificationNumber: ").appendValue(inventoryFullViewModel.getDonationIdentificationNumber())
        .appendText("\ndaysToExpire: ").appendValue(inventoryFullViewModel.getDaysToExpire())
        .appendText("\nBloodGroup: ").appendValue(inventoryFullViewModel.getBloodGroup())
        .appendText("\nexpiresOn: ").appendValue(inventoryFullViewModel.getExpiresOn())
        .appendText("\nOrderForms: ").appendValue(inventoryFullViewModel.getOrderForms())
        .appendText("\ncomponentStatus: ").appendValue(inventoryFullViewModel.getComponentStatus());
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
        Objects.equals(actual.getDaysToExpire(), expected.getDaysToExpire()) &&
        (Objects.equals(actual.getExpiresOn(), expected.getExpiresOn())
            || Objects.equals(sdf.format(actual.getExpiresOn()), sdf.format(expected.getExpiresOn()))) &&
        Objects.equals(actual.getLocation(), expected.getLocation()) &&
        Objects.equals(actual.getBloodGroup(), expected.getBloodGroup()) &&
        Objects.equals(actual.getOrderForms(), expected.getOrderForms()) &&
        Objects.equals(actual.getComponentStatus(), expected.getComponentStatus());
  }

  public static InventoryFullViewModelMatcher hasSameStateAsInventoryFullViewModel(InventoryFullViewModel expected) {
    return new InventoryFullViewModelMatcher(expected);
  }
}
