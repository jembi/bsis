package org.jembi.bsis.model.order;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.repository.OrderFormNamedQueryConstants;

@NamedQueries({
    @NamedQuery(name = OrderFormNamedQueryConstants.NAME_FIND_BY_ID,
        query = OrderFormNamedQueryConstants.QUERY_FIND_BY_ID),
    @NamedQuery(name = OrderFormNamedQueryConstants.NAME_FIND_BLOOD_UNITS_ORDERED,
        query = OrderFormNamedQueryConstants.QUERY_FIND_BLOOD_UNITS_ORDERED),
    @NamedQuery(name = OrderFormNamedQueryConstants.NAME_FIND_BLOOD_UNITS_ISSUED,
        query = OrderFormNamedQueryConstants.QUERY_FIND_BLOOD_UNITS_ISSUED),
    @NamedQuery(name = OrderFormNamedQueryConstants.NAME_FIND_ORDER_FORMS,
        query = OrderFormNamedQueryConstants.QUERY_FIND_ORDER_FORMS),
    @NamedQuery(name = OrderFormNamedQueryConstants.NAME_FIND_BY_COMPONENT,
        query = OrderFormNamedQueryConstants.QUERY_FIND_BY_COMPONENT),
    @NamedQuery(name = OrderFormNamedQueryConstants.NAME_IS_COMPONENT_IN_ANOTHER_ORDER_FORM,
        query = OrderFormNamedQueryConstants.QUERY_IS_COMPONENT_IN_ANOTHER_ORDER_FORM)
})
@Entity
@Audited
public class OrderForm extends BaseModificationTrackerUUIDEntity {
  
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

  @OneToMany(mappedBy = "orderForm", fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
  private List<OrderFormItem> items = new ArrayList<OrderFormItem>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "OrderForm_Component", joinColumns = {@JoinColumn(name = "orderForm_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "component_id", referencedColumnName = "id")})
  private List<Component> components = new ArrayList<Component>();

  @ManyToOne(optional = true, cascade = {CascadeType.ALL})
  private Patient patient;

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

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }
}
