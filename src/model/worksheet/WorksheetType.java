package model.worksheet;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestContext;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Audited
public class WorksheetType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  @Column(length = 30)
  private String worksheetType;

  @ManyToMany(mappedBy = "worksheetTypes")
  private Set<BloodTest> bloodTests;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
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
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
