package model.admin;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class GeneralConfig implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(unique = true)
  private String name;

  private String value;
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  private DataType dataType;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public void copy(GeneralConfig generalConfig) {
    this.name = generalConfig.getName();
    this.description = generalConfig.getDescription();
    this.value = generalConfig.getValue();
    this.dataType = generalConfig.getDataType();
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
    if (!(object instanceof GeneralConfig)) {
      return false;
    }
    GeneralConfig other = (GeneralConfig) object;
    return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
  }

  @Override
  public String toString() {
    return "model.admin.GeneralConfig[ id=" + id + " ]";
  }

}