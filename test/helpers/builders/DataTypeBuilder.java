package helpers.builders;

import model.admin.DataType;

public class DataTypeBuilder extends AbstractEntityBuilder<DataType> {

  private String dataTypeValue;

  public static DataTypeBuilder aDataType() {
    return new DataTypeBuilder();
  }

  public DataTypeBuilder withDataType(String dataTypeValue) {
    this.dataTypeValue = dataTypeValue;
    return this;
  }

  @Override
  public DataType build() {
    DataType dataType = new DataType();
    dataType.setDatatype(dataTypeValue);
    return dataType;
  }

}
