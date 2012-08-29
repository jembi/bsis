package model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RecordFieldsConfig {
    @Id
    private String recordType;
    private String fieldNames;

    public RecordFieldsConfig(String recordType, String fieldNames) {

        this.recordType = recordType;
        this.fieldNames = fieldNames;
    }

    public RecordFieldsConfig() {
    }

    public String getRecordType() {
        return recordType;
    }

    public String getFieldNames() {
        return fieldNames;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public void setFieldNames(String fieldNames) {
        this.fieldNames = fieldNames;
    }
}
