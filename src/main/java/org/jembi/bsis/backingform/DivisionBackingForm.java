
package org.jembi.bsis.backingform;

public class DivisionBackingForm {

  private long id;

  private String name;

  private int level;

  private DivisionBackingForm parent;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public DivisionBackingForm getParent() {
    return parent;
  }

  public void setParent(DivisionBackingForm parent) {
    this.parent = parent;
  }
  
}
