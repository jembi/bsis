
package org.jembi.bsis.backingform;

public class DivisionBackingForm {

  private Long id;

  private String name;

  private Integer level;

  private DivisionBackingForm parent;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public DivisionBackingForm getParent() {
    return parent;
  }

  public void setParent(DivisionBackingForm parent) {
    this.parent = parent;
  }
  
}
