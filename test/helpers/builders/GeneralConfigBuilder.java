package helpers.builders;

import model.admin.DataType;
import model.admin.GeneralConfig;

public class GeneralConfigBuilder extends AbstractEntityBuilder<GeneralConfig> {
    
    private DataType dataType;
    private String value;

    public GeneralConfigBuilder withDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }
    
    public GeneralConfigBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public GeneralConfig build() {
        GeneralConfig generalConfig = new GeneralConfig();
        generalConfig.setDataType(dataType);
        generalConfig.setValue(value);
        return generalConfig;
    }
    
    public static GeneralConfigBuilder aGeneralConfig() {
        return new GeneralConfigBuilder();
    }

}
