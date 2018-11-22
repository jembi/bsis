package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.InventoryViewModel;

public class InventoryViewModelMatcher extends AbstractTypeSafeMatcher<InventoryViewModel> {

  public InventoryViewModelMatcher(InventoryViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, InventoryViewModel inventoryViewModel) {
    description.appendText("An InventoryViewModel with the following state:")
        .appendText("\nId: ").appendValue(inventoryViewModel.getId())
        .appendText("\nComponentCode: ").appendValue(inventoryViewModel.getComponentCode())
        .appendText("\nComponentType: ").appendValue(inventoryViewModel.getComponentType())
        .appendText("\nInventoryStatus: ").appendValue(inventoryViewModel.getInventoryStatus())
        .appendText("\nLocation: ").appendValue(inventoryViewModel.getLocation())
        .appendText("\ncreatedOn: ").appendValue(inventoryViewModel.getCreatedOn())
        .appendText("\ndonationIdentificationNumber: ").appendValue(inventoryViewModel.getDonationIdentificationNumber())
        .appendText("\ndaysToExpire: ").appendValue(inventoryViewModel.getDaysToExpire())
        .appendText("\nBloodGroup: ").appendValue(inventoryViewModel.getBloodGroup())
        .appendText("\nexpiresOn: ").appendValue(inventoryViewModel.getExpiresOn())
        .appendText("\ncomponentStatus: ").appendValue(inventoryViewModel.getComponentStatus());
  }

  @Override
  public boolean matchesSafely(InventoryViewModel actual) {
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
        Objects.equals(actual.getBloodGroup(), expected.getBloodGroup())  &&
        Objects.equals(actual.getComponentStatus(), expected.getComponentStatus());
  }

  public static InventoryViewModelMatcher hasSameStateAsInventoryViewModel(InventoryViewModel expected) {
    return new InventoryViewModelMatcher(expected);
  }

}
