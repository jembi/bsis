package helpers.builders;

import model.componenttype.ComponentType;

public class ComponentTypeBuilder extends AbstractEntityBuilder<ComponentType> {

  private Long id;
  private String componentTypeName;
  private Boolean isDeleted;
  private int expiresAfter;

  public ComponentTypeBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentTypeBuilder withComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
    return this;
  }

  public ComponentTypeBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }
  
  public ComponentTypeBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public ComponentType build() {
    ComponentType componentType = new ComponentType();
    componentType.setId(id);
    componentType.setComponentTypeName(componentTypeName);
    componentType.setIsDeleted(isDeleted);
    componentType.setExpiresAfter(expiresAfter);
    return componentType;
  }

  public static ComponentTypeBuilder aComponentType() {
    return new ComponentTypeBuilder();
  }

}
