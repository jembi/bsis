package tasks;

public class GeneralConfigFile {


    private String value;
    private String name;
    private String description;
    private String datatype;


    public GeneralConfigFile() {
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDatatype(String datatype){
        this.datatype = datatype;
    }

    public void setValue(String value){
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription(){
        return description;
    }

    public String getDatatype(){
        return datatype;
    }

}