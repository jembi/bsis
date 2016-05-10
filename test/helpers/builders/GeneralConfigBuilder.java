package helpers.builders;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.GeneralConfigPersister;
import model.admin.DataType;
import model.admin.GeneralConfig;

public class GeneralConfigBuilder extends AbstractEntityBuilder<GeneralConfig> {

  private Long id;
  private DataType dataType;
  private String value;
  private String name;

  public GeneralConfigBuilder withId(Long id) {
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
    return generalConfig;
  }

  public static GeneralConfigBuilder aGeneralConfig() {
    return new GeneralConfigBuilder();
  }

}
