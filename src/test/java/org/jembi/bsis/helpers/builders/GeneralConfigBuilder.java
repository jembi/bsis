package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.GeneralConfigPersister;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.GeneralConfig;

public class GeneralConfigBuilder extends AbstractEntityBuilder<GeneralConfig> {

  private UUID id;
  private DataType dataType;
  private String value;
  private String name;
  private String description;

  public GeneralConfigBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public GeneralConfigBuilder withDataType(DataType dataType) {
    this.dataType = dataType;
    return this;
  }

  public GeneralConfigBuilder withValue(String value) {
    this.value = value;
    return this;
  }
  
  public GeneralConfigBuilder withName(String name) {
    this.name = name;
    return this;
  }
  
  public GeneralConfigBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public AbstractEntityPersister<GeneralConfig> getPersister() {
    return new GeneralConfigPersister();
  }

  @Override
  public GeneralConfig build() {
    GeneralConfig generalConfig = new GeneralConfig();
    generalConfig.setId(id);
    generalConfig.setDataType(dataType);
    generalConfig.setValue(value);
    generalConfig.setName(name);
    generalConfig.setDescription(description);
    return generalConfig;
  }

  public static GeneralConfigBuilder aGeneralConfig() {
    return new GeneralConfigBuilder();
  }

}
