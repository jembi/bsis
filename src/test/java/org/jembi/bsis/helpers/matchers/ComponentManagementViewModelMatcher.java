package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;

public class ComponentManagementViewModelMatcher extends TypeSafeMatcher<ComponentManagementViewModel> {

  private ComponentManagementViewModel expected;

  public ComponentManagementViewModelMatcher(ComponentManagementViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A ComponentViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nComponentCode: ").appendValue(expected.getComponentCode())
        .appendText("\nComponentType: ").appendValue(expected.getComponentType())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nCreated on: ").appendValue(expected.getCreatedOn())
        .appendText("\nExpires on: ").appendValue(expected.getExpiresOn())
        .appendText("\nExpiry status: ").appendValue(expected.getExpiryStatus())
        .appendText("\nCreatedOn: ").appendValue(expected.getCreatedOn())
        .appendText("\nWeight: ").appendValue(expected.getWeight())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions())
        .appendText("\nPack type: ").appendValue(expected.getPackType())
        .appendText("\nHas component batch: ").appendValue(expected.getHasComponentBatch())
        .appendText("\nInventory Status: ").appendValue(expected.getInventoryStatus())
        .appendText("\nBleed Start Time: ").appendValue(expected.getBleedStartTime())
        .appendText("\nBleed End Time: ").appendValue(expected.getBleedEndTime())
        .appendText("\nDonation datetime: ").appendValue(expected.getDonationDateTime());
  }

  @Override
  public boolean matchesSafely(ComponentManagementViewModel actual) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getComponentCode(), expected.getComponentCode()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getExpiryStatus(), expected.getExpiryStatus()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions()) &&
        (Objects.equals(actual.getCreatedOn(), expected.getCreatedOn()) || Objects.equals(sdf.format(actual.getCreatedOn()), sdf.format(expected.getCreatedOn()))) &&
        (Objects.equals(actual.getExpiresOn(), expected.getExpiresOn()) || Objects.equals(sdf.format(actual.getExpiresOn()), sdf.format(expected.getExpiresOn()))) &&
        Objects.equals(actual.getPackType(), expected.getPackType()) &&
        Objects.equals(actual.getHasComponentBatch(), expected.getHasComponentBatch()) &&
        Objects.equals(actual.getInventoryStatus(), expected.getInventoryStatus()) &&
        Objects.equals(actual.getBleedStartTime(), expected.getBleedStartTime()) &&
        Objects.equals(actual.getBleedEndTime(), expected.getBleedEndTime()) &&
        Objects.equals(actual.getDonationDateTime(), expected.getDonationDateTime());
  }

  public static ComponentManagementViewModelMatcher hasSameStateAsComponentManagementViewModel(ComponentManagementViewModel expected) {
    return new ComponentManagementViewModelMatcher(expected);
  }

}
