package model.rawbloodtest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import model.bloodtest.BloodTest;

@Entity
public class BloodTestMapper {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @Column(length=20)
  private String name;

  /**
   * typically pattern of + and - in the order of rank in RawBloodTest.
   * Pattern only important for blood typing. For TTI Testing assume
   * that pattern is just a single character + or -.
   */
  @Column(length=20)
  private String pattern;

  @ManyToOne(optional=false)
  private BloodTest bloodTest;

  @Column(length=30)
  private String result;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public BloodTest getBloodTest() {
    return bloodTest;
  }

  public void setBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }


}
