package model.microtiterplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PlateContent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @Column(columnDefinition="SMALLINT")
  private Integer rowNumber;

  @Column(columnDefinition="SMALLINT")
  private Integer colNumber;

  @Enumerated(EnumType.STRING)
  @Column(length=15)
  private ContentCoverage coverage;

  @Enumerated(EnumType.STRING)
  @Column(length=15)
  private PlateContentType contentType;

  public Integer getId() {
    return id;
  }

  public Integer getRowNumber() {
    return rowNumber;
  }

  public Integer getColNumber() {
    return colNumber;
  }

  public ContentCoverage getCoverage() {
    return coverage;
  }

  public PlateContentType getContentType() {
    return contentType;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setRowNumber(Integer rowNumber) {
    this.rowNumber = rowNumber;
  }

  public void setColNumber(Integer colNumber) {
    this.colNumber = colNumber;
  }

  public void setCoverage(ContentCoverage coverage) {
    this.coverage = coverage;
  }

  public void setContentType(PlateContentType contentType) {
    this.contentType = contentType;
  }
}
