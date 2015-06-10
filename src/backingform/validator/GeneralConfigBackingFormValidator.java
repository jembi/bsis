package backingform.validator;

import backingform.GeneralConfigBackingForm;
import controller.UtilController;
import model.admin.DataType;
import model.admin.EnumDataType;
import model.admin.GeneralConfig;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.DataTypeRepository;
import repository.GeneralConfigRepository;

import java.util.Arrays;


public class GeneralConfigBackingFormValidator implements Validator {

    private Validator validator;

    private GeneralConfigRepository configRepository;

    private UtilController utilController;

    private DataTypeRepository dataTypeRepository;

    public GeneralConfigBackingFormValidator(Validator validator, GeneralConfigRepository configRepository, UtilController utilController, DataTypeRepository dataTypeRepository) {
        super();
        this.validator = validator;
        this.configRepository = configRepository;
        this.utilController = utilController;
        this.dataTypeRepository = dataTypeRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(GeneralConfigBackingForm.class,
                GeneralConfig.class, String.class).contains(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target == null || validator == null) {
            return;
        }
        GeneralConfigBackingForm formItem = (GeneralConfigBackingForm) target;
        DataType dataType = dataTypeRepository.getDataTypeByid(formItem.getDataType().getId());

        EnumDataType enumDataType = EnumDataType.valueOf(dataType.getDatatype().toUpperCase());
        switch(enumDataType){
            case TEXT:
                // Allow all
                break;
            case INTEGER:
                if (!formItem.getValue().matches("[0-9]+"))
                    errors.rejectValue("value","400", formItem.getName() + " is not an integer");
                break;
            case DECIMAL:
                if (!formItem.getValue().matches("[0-9]*\\.?[0-9]+"))
                    errors.rejectValue("value","400", formItem.getName() + " is not a decimal");
                break;
            case BOOLEAN:
                if(!(formItem.getValue().equalsIgnoreCase("true") || formItem.getValue().equalsIgnoreCase("false")))
                    errors.rejectValue("value","400", formItem.getName() + " is not a boolean");
                break;

        }


        GeneralConfig generalConfig = new GeneralConfig();
        generalConfig.setId(formItem.getId()); // I would like to get the Id from the url for put requests
        generalConfig.setName(formItem.getName());
        generalConfig.setValue(formItem.getValue());
        generalConfig.setDescription(formItem.getDescription());
        generalConfig.setDataType(dataType);

        if (utilController.isDuplicateGeneralConfigName(generalConfig))
            errors.rejectValue("name", "400",
                    "There exists a generalConfig with the same name.");

        formItem.setGeneralConfig(generalConfig);
    }
}
