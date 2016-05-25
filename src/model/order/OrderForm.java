package model.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import model.BaseModificationTrackerEntity;
import model.component.Component;
import model.location.Location;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class OrderForm extends BaseModificationTrackerEntity {
  
  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private Date orderDate;
  
  @ManyToOne(optional = false)
  private Location dispatchedFrom;
  
  @ManyToOne(optional = false)
  private Location dispatchedTo;
  
  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private OrderStatus status = OrderStatus.CREATED;

  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private OrderType type;

  @Column
  private boolean isDeleted = false;

  @OneToMany(mappedBy = "orderForm", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @Where(clause = "isDeleted = 0")
  private List<OrderFormItem> items = new ArrayList<OrderFormItem>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "OrderForm_Component", joinColumns = {@JoinColumn(name = "orderForm_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "component_id", referencedColumnName = "id")})
  private List<Component> components = new ArrayList<Component>();

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public Location getDispatchedFrom() {
    return dispatchedFrom;
  }

  public void setDispatchedFrom(Location dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
  }

  public Location getDispatchedTo() {
    return dispatchedTo;
  }

  public void setDispatchedTo(Location dispatchedTo) {
    this.dispatchedTo = dispatchedTo;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public OrderType getType() {
    return type;
  }

  public void setType(OrderType type) {
    this.type = type;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public List<OrderFormItem> getItems() {
    return items;
  }

  public void setItems(List<OrderFormItem> items) {
    this.items = items;
  }

  public List<Component> getComponents() {
    return components;
  }

  public void setComponents(List<Component> components) {
    this.components = components;
  }
}
