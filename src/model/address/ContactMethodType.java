package model.address;

import javax.persistence.*;


@Entity
public class ContactMethodType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  @Column(length = 30)
  private String contactMethodType;

  private boolean isDeleted;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getContactMethodType() {
    return contactMethodType;
  }

  public void setContactMethodType(String contactMethodType) {
    this.contactMethodType = contactMethodType;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
