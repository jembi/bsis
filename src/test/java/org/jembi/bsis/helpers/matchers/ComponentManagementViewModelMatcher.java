package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;

public class ComponentManagementViewModelMatcher extends AbstractTypeSafeMatcher<ComponentManagementViewModel> {

  public ComponentManagementViewModelMatcher(ComponentManagementViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, ComponentManagementViewModel model) {
    description.appendText("A ComponentManagementViewModel with the following state:")
        .appendText("\nId: ").appendValue(model.getId())
        .appendText("\nComponentCode: ").appendValue(model.getComponentCode())
        .appendText("\nComponentType: ").appendValue(model.getComponentType())
        .appendText("\nStatus: ").appendValue(model.getStatus())
        .appendText("\nCreated on: ").appendValue(model.getCreatedOn())
        .appendText("\nExpires on: ").appendValue(model.getExpiresOn())
        .appendText("\nDays to expire: ").appendValue(model.getDaysToExpire())
        .appendText("\nWeight: ").appendValue(model.getWeight())
        .appendText("\nPermissions: ").appendValue(model.getPermissions())
        .appendText("\nPack type: ").appendValue(model.getPackType())
        .appendText("\nHas component batch: ").appendValue(model.isBatched())
        .appendText("\nInventory Status: ").appendValue(model.getInventoryStatus())
        .appendText("\nBleed Start Time: ").appendValue(model.getBleedStartTime())
        .appendText("\nBleed End Time: ").appendValue(model.getBleedEndTime())
        .appendText("\nDonation datetime: ").appendValue(model.getDonationDateTime())
        .appendText("\nParent component id: ").appendValue(model.getParentComponentId());
  }

  @Override
  public boolean matchesSafely(ComponentManagementViewModel actual) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getComponentCode(), expected.getComponentCode()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getDaysToExpire(), expected.getDaysToExpire()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions()) &&
        (Objects.equals(actual.getCreatedOn(), expected.getCreatedOn())
            || Objects.equals(sdf.format(actual.getCreatedOn()), sdf.format(expected.getCreatedOn()))) &&
        (Objects.equals(actual.getExpiresOn(), expected.getExpiresOn())
            || Objects.equals(sdf.format(actual.getExpiresOn()), sdf.format(expected.getExpiresOn()))) &&
        Objects.equals(actual.getPackType(), expected.getPackType()) &&
        Objects.equals(actual.isBatched(), expected.isBatched()) &&
        Objects.equals(actual.getInventoryStatus(), expected.getInventoryStatus()) &&
        Objects.equals(actual.getBleedStartTime(), expected.getBleedStartTime()) &&
        Objects.equals(actual.getBleedEndTime(), expected.getBleedEndTime()) &&
        Objects.equals(actual.getDonationDateTime(), expected.getDonationDateTime()) &&
        Objects.equals(actual.getParentComponentId(), expected.getParentComponentId());
  }

  public static ComponentManagementViewModelMatcher hasSameStateAsComponentManagementViewModel(ComponentManagementViewModel expected) {
    return new ComponentManagementViewModelMatcher(expected);
  }
}
