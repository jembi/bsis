package helpers.builders;

import model.admin.DataType;

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

}
