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
        GeneralConfig generalConfig = generalConfigRepository.getGeneralConfigByName(name);
        
        if (generalConfig == null) {
            throw new IllegalArgumentException("General config \"" + name + "\" not found.");
        }

        if (getEnumDataType(generalConfig.getDataType()) != EnumDataType.BOOLEAN) {
            throw new IllegalArgumentException("General config \"" + name + "\" is not of boolean data type.");
        }
        
        return Boolean.toString(true).equalsIgnoreCase(generalConfig.getValue());
    }
    
    private EnumDataType getEnumDataType(DataType dataType) {
        return EnumDataType.valueOf(dataType.getDatatype().toUpperCase());
    }

}
