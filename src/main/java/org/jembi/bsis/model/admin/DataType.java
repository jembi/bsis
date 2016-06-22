package org.jembi.bsis.model.admin;

import javax.persistence.Entity;

import org.jembi.bsis.model.BaseEntity;

/**
 * Entity used to define the type of data stored in GeneralConfig
 */
@Entity
public class DataType extends BaseEntity {

  private static final long serialVersionUID = 1L;

  private String datatype;

  public String getDatatype() {
    return datatype;
  }

  public void setDatatype(String datatype) {
    this.datatype = datatype;
  }
}