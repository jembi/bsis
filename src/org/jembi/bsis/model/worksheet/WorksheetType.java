package org.jembi.bsis.model.worksheet;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseEntity;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestContext;

@Entity
@Audited
public class WorksheetType extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 30)
  private String worksheetType;

  @ManyToMany(mappedBy = "worksheetTypes")
  private Set<BloodTest> bloodTests;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private BloodTestContext context;

  private Boolean isDeleted;

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

  public BloodTestContext getContext() {
    return context;
  }

  public void setContext(BloodTestContext context) {
    this.context = context;
  }
}
