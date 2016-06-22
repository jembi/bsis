package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.EnumDataType;

public class DataTypeBuilder extends AbstractEntityBuilder<DataType> {

  private Long id;
  private String dataTypeValue;

  public DataTypeBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public DataTypeBuilder withDataType(String dataTypeValue) {
    this.dataTypeValue = dataTypeValue;
    return this;
  }

  public DataTypeBuilder withDataType(EnumDataType dataTypeValue) {
    this.dataTypeValue = dataTypeValue.name();
    return this;
  }

  @Override
  public DataType build() {
    DataType dataType = new DataType();
    dataType.setId(id);
    dataType.setDatatype(dataTypeValue);
    return dataType;
  }

  public static DataTypeBuilder aDataType() {
    return new DataTypeBuilder();
  }
  
  public static DataTypeBuilder aBooleanDataType() {
    return new DataTypeBuilder().withDataType(EnumDataType.BOOLEAN);
  }

}
