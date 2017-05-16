/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jembi.bsis.model.idtype;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.jembi.bsis.model.BaseUUIDEntity;

/**
 * Entity representing the various types of Donor identification accepted by the application.
 */
@Entity
public class IdType extends BaseUUIDEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 20)
  private String idType;

  private String regExp; // stores regular Expression To validate Id Numbers

  public String getIdType() {
    return idType;
  }

  public void setIdType(String idType) {
    this.idType = idType;
  }

  public String getRegExp() {
    return regExp;
  }

  public void setRegExp(String regExp) {
    this.regExp = regExp;
  }
}
