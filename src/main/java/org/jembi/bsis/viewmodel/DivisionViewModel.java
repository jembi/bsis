package org.jembi.bsis.viewmodel;

public class DivisionViewModel extends BaseViewModel {
  
  private String name;
  private int level;
  private DivisionViewModel parent;
   
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

  public DivisionViewModel getParent() {
    return parent;
  }

  public void setParent(DivisionViewModel parent) {
    this.parent = parent;
  }
}
