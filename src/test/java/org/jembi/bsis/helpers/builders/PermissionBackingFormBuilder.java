package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.PermissionBackingForm;

public class PermissionBackingFormBuilder extends AbstractBuilder<PermissionBackingForm> {

  private Long id;
  private String name;

  public PermissionBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public PermissionBackingFormBuilder withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public PermissionBackingForm build() {
    PermissionBackingForm permissionBackingForm = new PermissionBackingForm();
    permissionBackingForm.setId(id);
    permissionBackingForm.setName(name);
    return permissionBackingForm;
  }

  public static PermissionBackingFormBuilder aPermissionBackingForm() {
    return new PermissionBackingFormBuilder();
  }
}
