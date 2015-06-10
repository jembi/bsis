package viewmodel;

import model.admin.DataType;
import model.admin.GeneralConfig;

/**
 * Created by duma on 2015/06/01.
 */
public class GeneralConfigViewModel {

    private GeneralConfig generalConfig;

    public GeneralConfigViewModel(GeneralConfig generalConfig) {
        this.generalConfig = generalConfig;
    }

    public Integer getId() {
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
