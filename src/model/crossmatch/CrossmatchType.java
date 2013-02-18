package model.crossmatch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CrossmatchType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Integer id;

  private String crossmatchType;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCrossmatchType() {
    return crossmatchType;
  }

  public void setCrossmatchType(String crossmatchType) {
    this.crossmatchType = crossmatchType;
  }
}
