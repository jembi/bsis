/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.idtype;

import model.donor.Donor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author srikanth
 */
@Entity
public class IdNumber implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  @ManyToOne(targetEntity = Donor.class)
  @JoinColumn(name = "donorId", nullable = false)
  private Donor donorId;


  @ManyToOne(targetEntity = IdType.class)
  @JoinColumn(name = "idType", nullable = false)
  private IdType idType;

  private String idNumber;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public Donor getDonorId() {
    return donorId;
  }

  public void setDonorId(Donor donorId) {
    this.donorId = donorId;
  }

  public IdType getIdType() {
    return idType;
  }

  public void setIdType(IdType idType) {
    this.idType = idType;
  }


  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }


  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof IdNumber)) {
      return false;
    }
    IdNumber other = (IdNumber) object;
    return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
  }

  @Override
  public String toString() {
    return "model.idtype.IdNumber[ id=" + id + " ]";
  }

}
