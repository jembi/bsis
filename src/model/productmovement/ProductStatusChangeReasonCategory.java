package model.productmovement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProductStatusChangeReasonCategory {

  @Id
  @Column(length=20, nullable=false)
  private String category;

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
