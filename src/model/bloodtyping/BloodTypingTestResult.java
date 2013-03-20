package model.bloodtyping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import model.collectedsample.CollectedSample;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class BloodTypingTestResult {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @ManyToOne
  private CollectedSample collectedSample;

  @ManyToOne
  private BloodTypingTest bloodTypingTest;

  @Column(length=10)
  private String result;

  @Lob
  private String notes;

  public BloodTypingTestResult() {
  }

  public Integer getId() {
    return id;
  }

  public BloodTypingTest getBloodTypingTest() {
    return bloodTypingTest;
  }

  public String getResult() {
    return result;
  }

  public String getNotes() {
    return notes;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setBloodTypingTest(BloodTypingTest bloodTypingTest) {
    this.bloodTypingTest = bloodTypingTest;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    this.collectedSample = collectedSample;
  }
}
