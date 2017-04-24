package org.jembi.bsis.helpers.builders;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;

public class ComponentTypeCombinationBackingFormBuilder {

  private UUID id;
  private String combinationName;
  private List<ComponentTypeBackingForm> componentTypes;
  private Set<ComponentTypeBackingForm> sourceComponentTypes;
  private boolean isDeleted = false;

  public ComponentTypeCombinationBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ComponentTypeCombinationBackingFormBuilder withCombinationName(String combinationName) {
    this.combinationName = combinationName;
    return this;
  }

  public ComponentTypeCombinationBackingFormBuilder withComponentTypes(List<ComponentTypeBackingForm> componentTypes) {
    this.componentTypes = componentTypes;
    return this;
  }

  public ComponentTypeCombinationBackingFormBuilder withSourceComponentTypes(Set<ComponentTypeBackingForm> sourceComponentTypes) {
    this.sourceComponentTypes = sourceComponentTypes;
    return this;
  }

  public ComponentTypeCombinationBackingFormBuilder thatIsDeleted() {
    this.isDeleted =true;
    return this;
  }

  public ComponentTypeCombinationBackingForm build() {
    ComponentTypeCombinationBackingForm backingForm = new ComponentTypeCombinationBackingForm();
    backingForm.setId(id);
    backingForm.setCombinationName(combinationName);
    backingForm.setComponentTypes(componentTypes);
    backingForm.setSourceComponentTypes(sourceComponentTypes);
    backingForm.setIsDeleted(isDeleted);
    return backingForm;
  }

  public static ComponentTypeCombinationBackingFormBuilder aComponentTypeCombinationBackingForm() {
    return new ComponentTypeCombinationBackingFormBuilder();
  }
}
