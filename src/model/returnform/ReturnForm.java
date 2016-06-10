package model.returnform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import model.BaseModificationTrackerEntity;
import model.component.Component;
import model.location.Location;

@Entity
@Audited
public class ReturnForm extends BaseModificationTrackerEntity {
  
  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private Date returnDate;
  
  @ManyToOne(optional = false)
  private Location returnedFrom;
  
  @ManyToOne(optional = false)
  private Location returnedTo;
  
  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private ReturnStatus status = ReturnStatus.CREATED;

  @Column
  private boolean isDeleted = false;
  
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "ReturnForm_Component", joinColumns = {@JoinColumn(name = "returnForm_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "component_id", referencedColumnName = "id")})
  private List<Component> components = new ArrayList<Component>();

  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date returnDate) {
    this.returnDate = returnDate;
  }

  public Location getReturnedFrom() {
    return returnedFrom;
  }

  public void setReturnedFrom(Location returnedFrom) {
    this.returnedFrom = returnedFrom;
  }

  public Location getReturnedTo() {
    return returnedTo;
  }

  public void setReturnedTo(Location returnedTo) {
    this.returnedTo = returnedTo;
  }

  public ReturnStatus getStatus() {
    return status;
  }

  public void setStatus(ReturnStatus status) {
    this.status = status;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public List<Component> getComponents() {
    return components;
  }

  public void setComponents(List<Component> components) {
    this.components = components;
  }

}
