package model.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import model.BaseModificationTrackerEntity;
import model.component.Component;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class OrderFormComponent extends BaseModificationTrackerEntity {
  
  private static final long serialVersionUID = 1L;
  
  @ManyToOne(optional = false)
  private OrderForm orderForm;
  
  @ManyToOne(optional = false)
  private Component component;
  
  @Column
  private boolean isDeleted = false;

  public OrderForm getOrderForm() {
    return orderForm;
  }

  public void setOrderForm(OrderForm orderForm) {
    this.orderForm = orderForm;
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }  
}
