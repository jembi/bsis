/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backingform;

import java.util.List;
import model.admin.GeneralConfig;

/**
 *
 * @author srikanth
 */

public class GeneralConfigBackingForm {

    private List<String> values;
    
    private List<GeneralConfig> configs;

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }


   
    public List<GeneralConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<GeneralConfig> configs) {
        this.configs = configs;
    }

    
}
