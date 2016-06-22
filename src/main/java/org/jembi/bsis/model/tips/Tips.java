package org.jembi.bsis.model.tips;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tips {

  @Id
  @Column(nullable = false, updatable = false, insertable = false)
  private String tipsKey;

  @Column
  private String tipsName;

  @Column(length = 1000)
  private String tipsContent;

  private Boolean isDeleted;

  public String getTipsKey() {
    return tipsKey;
  }

  public void setTipsKey(String tipsKey) {
    this.tipsKey = tipsKey;
  }

  public String getTipsName() {
    return tipsName;
  }

  public void setTipsName(String tipsName) {
    this.tipsName = tipsName;
  }

  public String getTipsContent() {
    return tipsContent;
  }

  public void setTipsContent(String tipsContent) {
    this.tipsContent = tipsContent;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
