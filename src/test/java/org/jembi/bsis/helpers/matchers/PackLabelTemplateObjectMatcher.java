package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.template.labelling.PackLabelTemplateObject;

public class PackLabelTemplateObjectMatcher extends AbstractTypeSafeMatcher<PackLabelTemplateObject> {

  public PackLabelTemplateObjectMatcher(PackLabelTemplateObject expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, PackLabelTemplateObject template) {
    description.appendText("A PackLabelTemplateObject with the following state:")
    .appendText("\nDINPositioning.flagCharPos:").appendValue(template.DINPositioning.flagCharPos)
    .appendText("\nDINPositioning.boxPos:").appendValue(template.DINPositioning.boxPos)
    .appendText("\nDINPositioning.checkCharPos:").appendValue(template.DINPositioning.checkCharPos)
    .appendText("\nconfig.serviceInfoLine1:").appendValue(template.config.serviceInfoLine1)
    .appendText("\nconfig.serviceInfoLine2:").appendValue(template.config.serviceInfoLine2)
    .appendText("\ndonation.DIN:").appendValue(template.donation.DIN)
    .appendText("\ndonation.flagCharacters:").appendValue(template.donation.flagCharacters)
    .appendText("\ndonation.checkCharacter:").appendValue(template.donation.checkCharacter)
    .appendText("\ndonation.bloodABO:").appendValue(template.donation.bloodABO)
    .appendText("\ndonation.bloodRh:").appendValue(template.donation.bloodRh)
    .appendText("\ndonation.isBloodRhPositive:").appendValue(template.donation.isBloodRhPositive)
    .appendText("\ndonation.isBloodRhNegative:").appendValue(template.donation.isBloodRhNegative)
    .appendText("\ndonation.isBloodHighTitre:").appendValue(template.donation.isBloodHighTitre)
    .appendText("\ndonation.donationDate:").appendValue(template.donation.donationDate)
    .appendText("\ndonation.donationDateISO:").appendValue(template.donation.donationDateISO)
    .appendText("\ncomponent.componentCode:").appendValue(template.component.componentCode)
    .appendText("\ncomponent.expiresOn:").appendValue(template.component.expiresOn)
    .appendText("\ncomponent.expiresOnISO:").appendValue(template.component.expiresOnISO)
    .appendText("\ncomponent.volume:").appendValue(template.component.volume)
    .appendText("\ncomponentType.componentTypeName:").appendValue(template.componentType.componentTypeName)
    .appendText("\ncomponentType.preparationInfo:").appendValue(template.componentType.preparationInfo)
    .appendText("\ncomponentType.storageInfo:").appendValue(template.componentType.storageInfo)
    .appendText("\ncomponentType.transportInfo:").appendValue(template.componentType.transportInfo);
  }

  @Override
  protected boolean matchesSafely(PackLabelTemplateObject actual) {
    return Objects.equals(actual.DINPositioning.flagCharPos, expected.DINPositioning.flagCharPos)
        && Objects.equals(actual.DINPositioning.boxPos, expected.DINPositioning.boxPos)
        && Objects.equals(actual.DINPositioning.checkCharPos, expected.DINPositioning.checkCharPos)
        && Objects.equals(actual.config.serviceInfoLine1, expected.config.serviceInfoLine1)
        && Objects.equals(actual.config.serviceInfoLine2, expected.config.serviceInfoLine2)
        && Objects.equals(actual.donation.DIN, expected.donation.DIN)
        && Objects.equals(actual.donation.flagCharacters, expected.donation.flagCharacters)
        && Objects.equals(actual.donation.checkCharacter, expected.donation.checkCharacter)
        && Objects.equals(actual.donation.bloodABO, expected.donation.bloodABO)
        && Objects.equals(actual.donation.bloodRh, expected.donation.bloodRh)
        && Objects.equals(actual.donation.isBloodRhPositive, expected.donation.isBloodRhPositive)
        && Objects.equals(actual.donation.isBloodRhNegative, expected.donation.isBloodRhNegative)
        && Objects.equals(actual.donation.isBloodHighTitre, expected.donation.isBloodHighTitre)
        && Objects.equals(actual.donation.donationDate, expected.donation.donationDate)
        && Objects.equals(actual.donation.donationDateISO, expected.donation.donationDateISO)
        && Objects.equals(actual.component.componentCode, expected.component.componentCode)
        && Objects.equals(actual.component.expiresOn, expected.component.expiresOn)
        && Objects.equals(actual.component.expiresOnISO, expected.component.expiresOnISO)
        && Objects.equals(actual.component.volume, expected.component.volume)
        && Objects.equals(actual.componentType.componentTypeName, expected.componentType.componentTypeName)
        && Objects.equals(actual.componentType.preparationInfo, expected.componentType.preparationInfo)
        && Objects.equals(actual.componentType.storageInfo, expected.componentType.storageInfo)
        && Objects.equals(actual.componentType.transportInfo, expected.componentType.transportInfo);
  }

  public static PackLabelTemplateObjectMatcher hasSameStateAsPackLabelTemplateObject(
      PackLabelTemplateObject expected) {
    return new PackLabelTemplateObjectMatcher(expected);
  }
}
