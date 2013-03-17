package model.rawbloodtest;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class RawBloodTestGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @Column(length=30)
  private String testGroupName;

  @OneToMany
  private List<RawBloodTest> bloodTestsInGroup;

  public Integer getId() {
    return id;
  }

  public String getTestGroupName() {
    return testGroupName;
  }

  public List<RawBloodTest> getBloodTestsInGroup() {
    return bloodTestsInGroup;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setTestGroupName(String testGroupName) {
    this.testGroupName = testGroupName;
  }

  public void setBloodTestsInGroup(List<RawBloodTest> bloodTestsInGroup) {
    this.bloodTestsInGroup = bloodTestsInGroup;
  }

}
