package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.template.DiscardLabelTemplateObject;

public class DiscardLabelTemplateObjectMatcher extends AbstractTypeSafeMatcher<DiscardLabelTemplateObject> {

  private DiscardLabelTemplateObject expected;

  public DiscardLabelTemplateObjectMatcher(DiscardLabelTemplateObject expected) {
    this.expected = expected;
  }

  @Override
  public void appendDescription(Description description, DiscardLabelTemplateObject template) {
    description.appendText("A DiscardLabelTemplateObject with the following state:")
        .appendText("\ncomponent.componentCode: ").appendValue(template.component.getComponentCode())
        .appendText("\nconfig.serviceInfoLine1: ").appendValue(template.config.getServiceInfoLine1())
        .appendText("\nconfig.serviceInfoLine2: ").appendValue(template.config.getServiceInfoLine2());
  }

  @Override
  protected boolean matchesSafely(DiscardLabelTemplateObject actual) {
    return Objects.equals(actual.component.getComponentCode(), expected.component.getComponentCode())
        && Objects.equals(actual.config.getServiceInfoLine1(), expected.config.getServiceInfoLine1())
        && Objects.equals(actual.config.getServiceInfoLine2(), expected.config.getServiceInfoLine2());
  }

  public static DiscardLabelTemplateObjectMatcher hasSameStateAsDiscardLabelTemplateObject(DiscardLabelTemplateObject expected) {
    return new DiscardLabelTemplateObjectMatcher(expected);
  }
}
