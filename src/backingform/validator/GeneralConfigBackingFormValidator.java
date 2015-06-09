package backingform.validator;

import backingform.GeneralConfigItemBackingForm;
import model.admin.DataType;
import model.admin.GeneralConfig;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.GeneralConfigRepository;

import java.util.Arrays;

/**
 * Created by duma on 2015/06/08.
 */
public class GeneralConfigBackingFormValidator implements Validator {

    private Validator validator;

    private GeneralConfigRepository configRepository;

    public GeneralConfigBackingFormValidator(Validator validator, GeneralConfigRepository configRepository) {
        super();
        this.validator = validator;
        this.configRepository = configRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(GeneralConfigItemBackingForm.class,
                GeneralConfig.class, String.class).contains(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target == null || validator == null) {
            return;
        }
        GeneralConfigItemBackingForm formItem = (GeneralConfigItemBackingForm) target;
        GeneralConfig generalConfig = new GeneralConfig();
        generalConfig.setName(formItem.getName());
        generalConfig.setValue(formItem.getValue());
        generalConfig.setDescription(formItem.getDescription());
        formItem.setGeneralConfig(generalConfig);
    }
}
