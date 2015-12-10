package model.worksheet;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestContext;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class WorksheetType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Long id;

  @Column(length=30)
  private String worksheetType;

  @ManyToMany(mappedBy="worksheetTypes")
  private Set<BloodTest> bloodTests;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private BloodTestContext context;
  
  private Boolean isDeleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWorksheetType() {
    return worksheetType;
  }

  public void setWorksheetType(String worksheetType) {
    this.worksheetType = worksheetType;
  }

  public Set<BloodTest> getBloodTests() {
    return bloodTests;
  }

  public void setBloodTests(Set<BloodTest> bloodTests) {
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

  @Override
  public boolean equals(Object o) {
    if (o instanceof WorksheetType) {
      WorksheetType wt = (WorksheetType) o;
      return id != null && wt.id != null && id.equals(wt.id);
    }
    else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
