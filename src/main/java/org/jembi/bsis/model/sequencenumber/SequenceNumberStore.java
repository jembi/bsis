package org.jembi.bsis.model.sequencenumber;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.jembi.bsis.model.BaseEntity;

@Entity
public class SequenceNumberStore extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 50)
  private String targetTable;

  @Column(length = 50)
  private String columnName;

  @Column(length = 5)
  private String prefix;

  private Long lastNumber;

  public String getTargetTable() {
    return targetTable;
  }

  public void setTargetTable(String targetTable) {
    this.targetTable = targetTable;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public Long getLastNumber() {
    return lastNumber;
  }

  public void setLastNumber(Long lastNumber) {
    this.lastNumber = lastNumber;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

}
