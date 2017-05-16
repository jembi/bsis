package org.jembi.bsis.viewmodel;

import java.util.UUID;

import org.jembi.bsis.model.admin.DataType;

public class GeneralConfigViewModel {

  private UUID   id;
  private String name;
  private String value;
  private String description;
  private DataType dataType;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

}
