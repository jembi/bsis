package model.microtiterplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import model.rawbloodtest.RawBloodTest;

@Entity
@Audited
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

  @ManyToOne
  private RawBloodTest rawBloodTest;
  
  @ManyToOne
  private MicrotiterPlate plateForContent;

  private Boolean isDeleted;

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

  public MicrotiterPlate getPlateForContent() {
    return plateForContent;
  }

  public void setPlateForContent(MicrotiterPlate plateForContent) {
    this.plateForContent = plateForContent;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public RawBloodTest getRawBloodTest() {
    return rawBloodTest;
  }

  public void setRawBloodTest(RawBloodTest rawBloodTest) {
    this.rawBloodTest = rawBloodTest;
  }
}
