package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.GeneralConfigBackingForm;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.EnumDataType;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.repository.DataTypeRepository;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class GeneralConfigBackingFormValidator extends BaseValidator<GeneralConfigBackingForm> {

  @Autowired
  private GeneralConfigRepository generalConfigRepository;

  @Autowired
  private DataTypeRepository dataTypeRepository;

  @Override
  public void validateForm(GeneralConfigBackingForm formItem, Errors errors) {
    DataType dataType = dataTypeRepository.getDataTypeByid(formItem.getDataType().getId());

    EnumDataType enumDataType = EnumDataType.valueOf(dataType.getDatatype().toUpperCase());
    switch (enumDataType) {
      case TEXT:
        // Allow all
        break;
      case INTEGER:
        if (!formItem.getValue().matches("[0-9]+"))
          errors.rejectValue("value", "400", "Invalid integer value");
        break;
      case DECIMAL:  
        if (!formItem.getValue().matches("[0-9]*\\.?[0-9]+"))
          errors.rejectValue("value", "400", "Invalid decimal value");
        break;
      case BOOLEAN:
        if (!(formItem.getValue().equalsIgnoreCase("true") || formItem.getValue().equalsIgnoreCase("false")))
          errors.rejectValue("value", "400", "Invalid boolean value");
        break;
      case PASSWORD:
        // Allow all
        break;
    }

    GeneralConfig generalConfig = new GeneralConfig();
    generalConfig.setId(formItem.getId()); // I would like to get the Id from the url for put requests                                       
    generalConfig.setName(formItem.getName());
    generalConfig.setValue(formItem.getValue());
    generalConfig.setDescription(formItem.getDescription());
    generalConfig.setDataType(dataType);

    if (isDuplicateGeneralConfigName(generalConfig))
      errors.rejectValue("name", "400", "Configuration name already exists.");

    formItem.setGeneralConfig(generalConfig);
  }

  @Override
  public String getFormName() {
    return "generalConfig";
  }

  private boolean isDuplicateGeneralConfigName(GeneralConfig config) {
    String configName = config.getName();
    if (StringUtils.isBlank(configName)) {
      return false;
    }

    GeneralConfig existingConfig = generalConfigRepository.getGeneralConfigByName(configName);
    if (existingConfig != null && !existingConfig.getId().equals(config.getId())) {
      return true;
    }

    return false;
  }
}
