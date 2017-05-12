package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.GeneralConfigBackingForm;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.GeneralConfig;

public class GeneralConfigBackingFormBuilder extends AbstractBuilder<GeneralConfigBackingForm>{
  
  private UUID id;
  private DataType dataType;
  private String value;
  private String name;
  private String description;

  public GeneralConfigBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public GeneralConfigBackingFormBuilder withDataType(DataType dataType) {
    this.dataType= dataType;
    return this;
  }

  public GeneralConfigBackingFormBuilder withValue(String value) {
    this.value = value;
    return this;
  }

  public GeneralConfigBackingFormBuilder withName(String name) {  
    this.name = name;
    return this;
  }

  public GeneralConfigBackingFormBuilder withDescription(String description) {
    this.description = description;
    return this;
  }
  
  @Override
  public GeneralConfigBackingForm build() {
    GeneralConfigBackingForm config = new GeneralConfigBackingForm();
    config.setId(id);
    config.setName(name);
    config.setDescription(description);
    config.setValue(value);
    config.setDataType(dataType);
    return config;
  }

  public static GeneralConfigBackingFormBuilder aGeneralConfigBackingFormBuilder() {
    return new GeneralConfigBackingFormBuilder();
  }
}
