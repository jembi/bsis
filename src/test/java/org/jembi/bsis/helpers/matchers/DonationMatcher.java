package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.donation.Donation;

public class DonationMatcher extends AbstractTypeSafeMatcher<Donation> {

  public DonationMatcher(Donation expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, Donation model) {
    description.appendText("A Donation with the following state:")
        .appendText("\nId: ").appendValue(model.getId())
        .appendText("\nDIN: ").appendValue(model.getDonationIdentificationNumber())
        .appendText("\nDeleted: ").appendValue(model.getIsDeleted())
        .appendText("\nDonation Date: ").appendValue(model.getDonationDate())
        .appendText("\nDonor Pulse: ").appendValue(model.getDonorPulse())
        .appendText("\nHaemoglobin Count: ").appendValue(model.getHaemoglobinCount())
        .appendText("\nHaemoglobin Level: ").appendValue(model.getHaemoglobinLevel())
        .appendText("\nBlood Pressure Systolic: ").appendValue(model.getBloodPressureSystolic())
        .appendText("\nBlood Pressure Diastolic: ").appendValue(model.getBloodPressureDiastolic())
        .appendText("\nDonor Weight: ").appendValue(model.getDonorWeight())
        .appendText("\nNotes: ").appendValue(model.getNotes())
        .appendText("\nPack Type: ").appendValue(model.getPackType())
        .appendText("\nBleed Start Time: ").appendValue(model.getBleedStartTime())
        .appendText("\nBleed End Time: ").appendValue(model.getBleedEndTime())
        .appendText("\nAdverse Event: ").appendValue(model.getAdverseEvent())
        .appendText("\nTTI status: ").appendValue(model.getTTIStatus())
        .appendText("\nBlood ABO: ").appendValue(model.getBloodAbo())
        .appendText("\nBlood rh: ").appendValue(model.getBloodRh())
        .appendText("\nReleased: ").appendValue(model.isReleased())
        .appendText("\nComponents: ").appendValue(model.getComponents())
        .appendText("\nTitre: ").appendValue(model.getTitre())
        .appendText("\nFlag Characters: ").appendValue(model.getFlagCharacters());
  }

  @Override
  public boolean matchesSafely(Donation actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getDonorPulse(), expected.getDonorPulse()) &&
        Objects.equals(actual.getHaemoglobinCount(), expected.getHaemoglobinCount()) &&
        Objects.equals(actual.getHaemoglobinLevel(), expected.getHaemoglobinLevel()) &&
        Objects.equals(actual.getBloodPressureSystolic(), expected.getBloodPressureSystolic()) &&
        Objects.equals(actual.getBloodPressureDiastolic(), expected.getBloodPressureDiastolic()) &&
        Objects.equals(actual.getDonorWeight(), expected.getDonorWeight()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getDonationDate(), expected.getDonationDate()) &&
        Objects.equals(actual.getPackType(), expected.getPackType()) &&
        Objects.equals(actual.getBleedStartTime(), expected.getBleedStartTime()) &&
        Objects.equals(actual.getBleedEndTime(), expected.getBleedEndTime()) &&
        Objects.equals(actual.getAdverseEvent(), expected.getAdverseEvent()) &&
        Objects.equals(actual.getTTIStatus(), expected.getTTIStatus()) &&
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo()) &&
        Objects.equals(actual.getBloodRh(), expected.getBloodRh()) &&
        Objects.equals(actual.isReleased(), expected.isReleased()) &&
        Objects.equals(actual.getComponents(), expected.getComponents()) &&
        Objects.equals(actual.getTitre(), expected.getTitre()) &&
        Objects.equals(actual.getFlagCharacters(), expected.getFlagCharacters());
  }

  public static DonationMatcher hasSameStateAsDonation(Donation expected) {
    return new DonationMatcher(expected);
  }
}