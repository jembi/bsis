package org.jembi.bsis.helpers.matchers;

import java.util.Date;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.model.user.User;

public class TransfusionMatcher extends TypeSafeMatcher<Transfusion> {

  private Transfusion expected;

  public TransfusionMatcher(Transfusion expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A transfusion with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nCreated By: ").appendValue(expected.getCreatedBy())
        .appendText("\nCreated Date: ").appendValue(expected.getCreatedDate())
        .appendText("\nLast Updated By: ").appendValue(expected.getLastUpdatedBy())
        .appendText("\nLast Updated Date: ").appendValue(expected.getLastUpdated())
        .appendText("\nDIN: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nPatient: ").appendValue(expected.getPatient())
        .appendText("\nComponent Code: ").appendValue(expected.getComponentCode())
        .appendText("\nUsage Site: ").appendValue(expected.getUsageSite())
        .appendText("\ntTransfusion Reaction Type: ").appendValue(expected.getTransfusionReactionType())
        .appendText("\nTransfusion Outcome: ").appendValue(expected.getTransfusionOutcome())
        .appendText("\nDate Transfused: ").appendValue(expected.getDateTransfused());
  }
  
  @Override
  public boolean matchesSafely(Transfusion actual) {

    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getCreatedBy(), expected.getCreatedBy()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getLastUpdatedBy(), expected.getLastUpdatedBy()) &&
        Objects.equals(actual.getLastUpdated(), expected.getLastUpdated()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getPatient(), expected.getPatient()) &&
        Objects.equals(actual.getComponentCode(), expected.getComponentCode()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getUsageSite(), expected.getUsageSite()) &&
        Objects.equals(actual.getTransfusionReactionType(), expected.getTransfusionReactionType()) &&
        Objects.equals(actual.getTransfusionOutcome(), expected.getTransfusionOutcome()) &&
        Objects.equals(actual.getDateTransfused(), expected.getDateTransfused());
  }

  public static TransfusionMatcher hasSameStateAsTransfusion(Transfusion expected) {
    return new TransfusionMatcher(expected);
  }

}
