package backingform.validator;

import backingform.GeneralConfigBackingForm;
import controller.UtilController;
import model.admin.DataType;
import model.admin.EnumDataType;
import model.admin.GeneralConfig;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.GeneralConfigRepository;

import java.util.Arrays;


public class GeneralConfigBackingFormValidator implements Validator {

    private Validator validator;

    private GeneralConfigRepository configRepository;

    private UtilController utilController;

    public GeneralConfigBackingFormValidator(Validator validator, GeneralConfigRepository configRepository, UtilController utilController) {
        super();
        this.validator = validator;
        this.configRepository = configRepository;
        this.utilController = utilController;
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

        boolean error = false;
        GeneralConfigBackingForm formItem = (GeneralConfigBackingForm) target;
        DataType dataType = formItem.getDataType();

        EnumDataType enumDataType = EnumDataType.valueOf(formItem.getDataType().getDatatype().toUpperCase());
        switch(enumDataType){
            case TEXT:
                // Allow all
                break;
            case INTEGER:
                if (!formItem.getValue().matches("[0-9]+"))
                    error=true;
                break;
            case DECIMAL:
                if (!formItem.getValue().matches("[0-9]*\\.?[0-9]+"))
                    error=true;
                break;
            case BOOLEAN:
                if(!(formItem.getValue().equalsIgnoreCase("true") || formItem.getValue().equalsIgnoreCase("false")))
                    error = true;
                break;

        }
        if(error)
            errors.rejectValue("value","400", formItem.getName() + " is incorrect");

        GeneralConfig generalConfig = new GeneralConfig();
        generalConfig.setName(formItem.getName());
        generalConfig.setValue(formItem.getValue());
        generalConfig.setDescription(formItem.getDescription());
        generalConfig.setDataType(dataType);
        formItem.setGeneralConfig(generalConfig);

        if (utilController.isDuplicateGeneralConfigName(generalConfig) && generalConfig.getId() != formItem.getId())
            errors.rejectValue("generalConfig.name", "generalConfig.nonunique",
                    "There exists a generalConfig with the same name.");
    }
}
