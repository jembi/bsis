package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.template.labelling.DiscardLabelTemplateObject;

public class DiscardLabelTemplateObjectMatcher extends AbstractTypeSafeMatcher<DiscardLabelTemplateObject> {

  public DiscardLabelTemplateObjectMatcher(DiscardLabelTemplateObject expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, DiscardLabelTemplateObject template) {
    description.appendText("A DiscardLabelTemplateObject with the following state:")
        .appendText("\ncomponent.componentCode: ").appendValue(template.component.componentCode)
        .appendText("\ncomponentType.componentTypeCode: ").appendValue(template.componentType.componentTypeCode)
        .appendText("\ndonation.DIN: ").appendValue(template.donation.DIN)
        .appendText("\nconfig.serviceInfoLine1: ").appendValue(template.config.serviceInfoLine1)
        .appendText("\nconfig.serviceInfoLine2: ").appendValue(template.config.serviceInfoLine2);
  }

  @Override
  protected boolean matchesSafely(DiscardLabelTemplateObject actual) {
    return Objects.equals(actual.component.componentCode, expected.component.componentCode)
        && Objects.equals(actual.componentType.componentTypeCode, expected.componentType.componentTypeCode)
        && Objects.equals(actual.donation.DIN, expected.donation.DIN)
        && Objects.equals(actual.config.serviceInfoLine1, expected.config.serviceInfoLine1)
        && Objects.equals(actual.config.serviceInfoLine2, expected.config.serviceInfoLine2);
  }

  public static DiscardLabelTemplateObjectMatcher hasSameStateAsDiscardLabelTemplateObject(DiscardLabelTemplateObject expected) {
    return new DiscardLabelTemplateObjectMatcher(expected);
  }
}
