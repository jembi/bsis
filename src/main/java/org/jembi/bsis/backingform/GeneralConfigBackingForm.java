package org.jembi.bsis.backingform;

import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.GeneralConfig;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class GeneralConfigBackingForm {

  @JsonIgnore
  private GeneralConfig generalConfig;

  private String value;
  private String name;
  private String description;
  private UUID id;

  @Valid
  private DataType dataType;

  public GeneralConfigBackingForm() {
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

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public GeneralConfig getGeneralConfig() {
    return generalConfig;
  }

  public void setGeneralConfig(GeneralConfig generalConfig) {
    this.generalConfig = generalConfig;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public DataType getDataType() {
    return dataType;
  }
}
