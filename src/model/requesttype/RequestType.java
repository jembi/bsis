package model.requesttype;

import javax.persistence.Column;
import javax.persistence.Entity;

import model.BaseEntity;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class RequestType extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 30)
  private String requestType;

  @Column(length = 100)
  private String description;

  private Boolean isDeleted;

  private Boolean bulkTransfer;

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