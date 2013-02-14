package model.sequencenumber;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SequenceNumberStore {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(nullable=false)
  private Integer id;
  
  private String targetTable;
  private String columnName;

  /**
   * Sometimes sequence numbers recycle after an year
   * of use. Store this kind of context information
   * in a generic manner using this field. It could be
   * anything else for another field. 
   */
  private String sequenceNumberContext;

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

  public String getSequenceNumberContext() {
    return sequenceNumberContext;
  }

  public void setSequenceNumberContext(String sequenceNumberContext) {
    this.sequenceNumberContext = sequenceNumberContext;
  }

  public Long getLastNumber() {
    return lastNumber;
  }

  public void setLastNumber(Long lastNumber) {
    this.lastNumber = lastNumber;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

}
