package model.microtiterplate;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class MicrotiterPlate {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @Column(length=15, unique=true)
  private String plateKey;
  
  @Column(length=20)
  private String plateName;

  @Column(columnDefinition="SMALLINT")
  private Integer numRows;

  @Column(columnDefinition="SMALLINT")
  private Integer numColumns;

  @OneToMany(mappedBy="plateForContent")
  private List<PlateContent> specialContents;

  @Lob
  private String notes;

  private Boolean isDeleted;

  public Integer getId() {
    return id;
  }

  public String getPlateName() {
    return plateName;
  }

  public Integer getNumRows() {
    return numRows;
  }

  public Integer getNumColumns() {
    return numColumns;
  }

  public List<PlateContent> getSpecialContents() {
    return specialContents;
  }

  public String getNotes() {
    return notes;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setPlateName(String plateName) {
    this.plateName = plateName;
  }

  public void setNumRows(Integer numRows) {
    this.numRows = numRows;
  }

  public void setNumColumns(Integer numColumns) {
    this.numColumns = numColumns;
  }

  public void setSpecialContents(List<PlateContent> specialContents) {
    this.specialContents = specialContents;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getPlateKey() {
    return plateKey;
  }

  public void setPlateKey(String plateKey) {
    this.plateKey = plateKey;
  }
}
