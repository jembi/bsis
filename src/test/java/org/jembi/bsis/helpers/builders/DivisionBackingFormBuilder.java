package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.DivisionBackingForm;

public class DivisionBackingFormBuilder extends AbstractBuilder<DivisionBackingForm> {
  
  private long id;
  private String name;
  private Integer level;
  private DivisionBackingForm parent;
  
  public DivisionBackingFormBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public DivisionBackingFormBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public DivisionBackingFormBuilder withLevel(Integer level) {
    this.level = level;
    return this;
  }
  
  public DivisionBackingFormBuilder withParent(DivisionBackingForm parent) {
    this.parent = parent;
    return this;
  }

  @Override
  public DivisionBackingForm build() {
    DivisionBackingForm form = new DivisionBackingForm();
    form.setId(id);
    form.setName(name);
    form.setLevel(level);
    form.setParent(parent);
    return form;
  }
  
  public static DivisionBackingFormBuilder aDivisionBackingForm() {
    return new DivisionBackingFormBuilder();
  }

}
