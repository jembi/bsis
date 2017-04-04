/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jembi.bsis.model.preferredlanguage;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.jembi.bsis.model.BaseUUIDEntity;

/**
 * Entity that defines the various languages that can be selected as the Donor's preferred
 * language.
 */
@Entity
public class PreferredLanguage extends BaseUUIDEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 20)
  private String preferredLanguage;

  public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }
}
