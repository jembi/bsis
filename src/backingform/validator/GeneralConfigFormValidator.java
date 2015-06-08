package backingform.validator;

import backingform.GeneralConfigBackingForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.admin.EnumDataType;
import model.admin.GeneralConfig;
import model.admin.GenericConfig;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.GeneralConfigRepository;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author srikanth
 */
public class GeneralConfigFormValidator implements Validator {

    private Validator validator;

    private GeneralConfigRepository configRepository;

    public GeneralConfigFormValidator(Validator validator, GeneralConfigRepository configRepository) {
        super();
        this.validator = validator;
        this.configRepository = configRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(GeneralConfigBackingForm.class,
                GeneralConfig.class,String.class).contains(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        if (obj == null || validator == null) {
            return;
        }
        boolean error = false;
        GeneralConfigBackingForm form = (GeneralConfigBackingForm) obj;
        List<String> values = form.getValues();
        List<GeneralConfig> configs = new ArrayList<GeneralConfig>();
        int count = 0;
        String configValue = "";
        for (GeneralConfig generalConfig : configRepository.getAll()) {
            configValue = values.get(count);
            EnumDataType dataType = EnumDataType.valueOf(generalConfig.getDataType().getDatatype().toUpperCase());
            switch(dataType){
                case TEXT:
                    if (!configValue.matches("[a-zA-Z]+")) // TODO fix regex to include numeric values
                        error=true;
                    break;
                case INTEGER:
                    if (!configValue.matches("[0-9]+"))
                        error=true;
                    break;
                case DECIMAL:
                    if (!configValue.matches("[0-9]*\\.?[0-9]+"))
                        error=true;
                    break;
                case BOOLEAN:
                    if(!(configValue.equalsIgnoreCase("true") || configValue.equalsIgnoreCase(configValue)))
                        error = true;
                    break;

            }
            if(error)
                errors.rejectValue("","", generalConfig.getName() + " is incorrect"); // TODO include string and error code values
            count++;
            generalConfig.setValue(configValue);
            configs.add(generalConfig);
            error = false;

        }
        form.setConfigs(configs);

    }

}