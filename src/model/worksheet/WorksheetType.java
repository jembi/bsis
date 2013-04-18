package model.worksheet;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestContext;

@Entity
@Audited
public class WorksheetType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=30)
  private String worksheetType;

  @ManyToMany(mappedBy="worksheetTypes")
  private List<BloodTest> bloodTests;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private BloodTestContext context;
  
  private Boolean isDeleted;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getWorksheetType() {
    return worksheetType;
  }

  public void setWorksheetType(String worksheetType) {
    this.worksheetType = worksheetType;
  }

  public List<BloodTest> getBloodTests() {
    return bloodTests;
  }

  public void setBloodTests(List<BloodTest> bloodTests) {
    this.bloodTests = bloodTests;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @Override
  public String toString() {
    return worksheetType;
  }

  public BloodTestContext getContext() {
    return context;
  }

  public void setContext(BloodTestContext context) {
    this.context = context;
  }
}
