package model.requesttype;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
public class RequestType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  @Column(length = 30)
  private String requestType;

  @Column(length = 100)
  private String description;

  private Boolean isDeleted;

  private Boolean bulkTransfer;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Boolean getBulkTransfer() {
    return bulkTransfer;
  }

  public void setBulkTransfer(Boolean bulkTransfer) {
    this.bulkTransfer = bulkTransfer;
  }

}
