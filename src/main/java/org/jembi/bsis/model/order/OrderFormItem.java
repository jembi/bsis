package org.jembi.bsis.model.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseUUIDEntity;
import org.jembi.bsis.model.componenttype.ComponentType;

@Entity
@Audited
public class OrderFormItem extends BaseUUIDEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(optional = false)
  private ComponentType componentType;

  @Column(nullable = false)
  private String bloodAbo;
  
  @Column(nullable = false)
  private String bloodRh;
  
  private int numberOfUnits;

  @ManyToOne(optional = false)
  private OrderForm orderForm;

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public int getNumberOfUnits() {
    return numberOfUnits;
  }

  public void setNumberOfUnits(int numberOfUnits) {
    this.numberOfUnits = numberOfUnits;
  }

  public OrderForm getOrderForm() {
    return orderForm;
  }

  public void setOrderForm(OrderForm orderForm) {
    this.orderForm = orderForm;
  }

}
