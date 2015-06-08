package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.admin.GeneralConfig;

/**
 * Created by debonair on 2015/06/02.
 */
public class GeneralConfigItemBackingForm {

    @JsonIgnore
    private GeneralConfig generalConfig;

    private String value;
    private String name;

    public GeneralConfigItemBackingForm(){
        setGeneralConfig(new GeneralConfig());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GeneralConfig getGeneralConfig() {
        return generalConfig;
    }

    public void setGeneralConfig(GeneralConfig generalConfig) {
        this.generalConfig = generalConfig;
    }

    private String description;
    private Integer id;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value){
        this.value = value;
    }
}
