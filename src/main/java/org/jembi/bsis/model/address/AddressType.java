/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jembi.bsis.model.address;

import javax.persistence.Entity;

import org.jembi.bsis.model.BaseUUIDEntity;

/**
 * Entity containing the Donor's preferred contact address
 */
@Entity
public class AddressType extends BaseUUIDEntity {
  private static final long serialVersionUID = 1L;

  private String preferredAddressType;


  public String getPreferredAddressType() {
    return preferredAddressType;
  }

  public void setPreferredAddressType(String preferredAddressType) {
    this.preferredAddressType = preferredAddressType;
  }
}
