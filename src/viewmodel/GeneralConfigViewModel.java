package viewmodel;

import model.admin.DataType;
import model.admin.GeneralConfig;

public class GeneralConfigViewModel {

  private GeneralConfig generalConfig;

  public GeneralConfigViewModel(GeneralConfig generalConfig) {
    this.generalConfig = generalConfig;
  }

  public Long getId() {
    return generalConfig.getId();
  }

  public String getName() {
    return generalConfig.getName();
  }

  public String getValue() {
    return generalConfig.getValue();
  }

  public String getDescription() {
    return generalConfig.getDescription();
  }

  public DataType getDataType() {
    return generalConfig.getDataType();
  }

}
