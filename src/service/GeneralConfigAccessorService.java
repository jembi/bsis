package service;

import model.admin.DataType;
import model.admin.EnumDataType;
import model.admin.GeneralConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.GeneralConfigRepository;

@Service
public class GeneralConfigAccessorService {

  @Autowired
  private GeneralConfigRepository generalConfigRepository;

  public void setGeneralConfigRepository(GeneralConfigRepository generalConfigRepository) {
    this.generalConfigRepository = generalConfigRepository;
  }

  public boolean getBooleanValue(String name) {

    GeneralConfig generalConfig = getGeneralConfigOfType(name, EnumDataType.BOOLEAN);
    return Boolean.toString(true).equalsIgnoreCase(generalConfig.getValue());
  }

  public int getIntValue(String name) {

    GeneralConfig generalConfig = getGeneralConfigOfType(name, EnumDataType.INTEGER);
    return Integer.parseInt(generalConfig.getValue());
  }

  private GeneralConfig getGeneralConfigOfType(String name, EnumDataType enumDataType) {

    GeneralConfig generalConfig = generalConfigRepository.getGeneralConfigByName(name);

    if (generalConfig == null) {
      throw new IllegalArgumentException("General config \"" + name + "\" not found.");
    }

    if (getEnumDataType(generalConfig.getDataType()) != enumDataType) {
      throw new IllegalArgumentException("General config \"" + name + "\" is not \"" + enumDataType + "\" type.");
    }

    return generalConfig;
  }

  private EnumDataType getEnumDataType(DataType dataType) {
    return EnumDataType.valueOf(dataType.getDatatype().toUpperCase());
  }

  /**
   * Retrieves a setting from General Config
   *
   * @param generalConfigName String name of the property
   * @return String value of the general config property, or empty string if property cannot be
   * found
   */
  public String getGeneralConfigValueByName(String generalConfigName) {
    GeneralConfig generalConfig = generalConfigRepository.getGeneralConfigByName(generalConfigName);
    if (generalConfig != null) {
      return generalConfig.getValue();
    }
    return "";
  }
}
