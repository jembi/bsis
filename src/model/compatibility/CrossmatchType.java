package model.compatibility;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
public class CrossmatchType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  private String crossmatchType;

  private Boolean isDeleted;

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

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
