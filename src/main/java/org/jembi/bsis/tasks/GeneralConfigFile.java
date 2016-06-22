package org.jembi.bsis.tasks;

public class GeneralConfigFile {


  private String value;
  private String name;
  private String description;
  private String dataType;


  public GeneralConfigFile() {
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }

  public String getDataType() {
    return dataType;
  }

}