/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jembi.bsis.model.idtype;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.jembi.bsis.model.BaseEntity;
import org.jembi.bsis.model.donor.Donor;

/**
 * Entity containing an identification number of a specific type for a Donor.
 */
@Entity
public class IdNumber extends BaseEntity {

  private static final long serialVersionUID = 1L;


  @ManyToOne(targetEntity = Donor.class)
  @JoinColumn(name = "donorId", nullable = false)
  private Donor donorId;


  @ManyToOne(targetEntity = IdType.class)
  @JoinColumn(name = "idType", nullable = false)
  private IdType idType;

  private String idNumber;

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
}
