package model.bloodtest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BloodTestResult {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  @ManyToOne
  private BloodTest bloodTest;

  @Column(length=255)
  private String result;

  public Long getId() {
    return id;
  }

  public BloodTest getBloodTest() {
    return bloodTest;
  }

  public String getResult() {
    return result;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
  }

  public void setResult(String result) {
    this.result = result;
  }

}
