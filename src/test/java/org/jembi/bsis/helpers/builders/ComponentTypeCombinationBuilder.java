package org.jembi.bsis.helpers.builders;

import java.util.List;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;

public class ComponentTypeCombinationBuilder  extends AbstractEntityBuilder<ComponentTypeCombination> {
  
  private Long id;
  private boolean isDeleted = false;
  private String combinationName;
  private List<ComponentType> componentTypes;

  public ComponentTypeCombinationBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentTypeCombinationBuilder withCombinationName(String combinationName) {
    this.combinationName = combinationName;
    return this;
  }

  public ComponentTypeCombinationBuilder withComponentTypes(List<ComponentType> componentTypes) {
    this.componentTypes = componentTypes;
    return this;
  }
  
  public ComponentTypeCombinationBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  @Override
  public ComponentTypeCombination build() {
    ComponentTypeCombination componentTypeCombination = new ComponentTypeCombination();
    componentTypeCombination.setId(id);
    componentTypeCombination.setCombinationName(combinationName);
    componentTypeCombination.setComponentTypes(componentTypes);
    componentTypeCombination.setIsDeleted(isDeleted);
    return componentTypeCombination;
  }
  
  public static ComponentTypeCombinationBuilder aComponentTypeCombination() {
    return new ComponentTypeCombinationBuilder();
  }
}
