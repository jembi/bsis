package org.jembi.bsis.service;

import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.EnumDataType;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public boolean getBooleanValue(String name, boolean defaultValue) {

    GeneralConfig generalConfig = getGeneralConfigOfTypeOrNull(name, EnumDataType.BOOLEAN);
    
    if (generalConfig == null) {
      return defaultValue;
    }
    
    return Boolean.toString(true).equalsIgnoreCase(generalConfig.getValue());
  }

  public int getIntValue(String name) {

    GeneralConfig generalConfig = getGeneralConfigOfType(name, EnumDataType.INTEGER);
    return Integer.parseInt(generalConfig.getValue());
  }

  private GeneralConfig getGeneralConfigOfType(String name, EnumDataType enumDataType) {
    GeneralConfig generalConfig = getGeneralConfigOfTypeOrNull(name, enumDataType);
    
    if (generalConfig == null) {
      throw new IllegalArgumentException("General config \"" + name + "\" not found.");
    }
    
    return generalConfig;
  }
  
  private GeneralConfig getGeneralConfigOfTypeOrNull(String name, EnumDataType enumDataType) {

    GeneralConfig generalConfig = generalConfigRepository.getGeneralConfigByName(name);

    if (generalConfig == null) {
      return null;
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
