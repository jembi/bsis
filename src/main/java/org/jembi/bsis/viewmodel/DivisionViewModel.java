package org.jembi.bsis.viewmodel;

import java.util.Objects;

public class DivisionViewModel {
  
  private long id;
  private String name;
  private int level;
  private DivisionViewModel parent;
   
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

  public DivisionViewModel getParent() {
    return parent;
  }

  public void setParent(DivisionViewModel parent) {
    this.parent = parent;
  }

  @Override
  public boolean equals(Object other) {
    
    if (!(other instanceof DivisionViewModel)) {
      return false;
    }
    
    DivisionViewModel otherViewModel = (DivisionViewModel) other;
    return Objects.equals(otherViewModel.getId(), getId())
        && Objects.equals(otherViewModel.getName(), getName())
        && Objects.equals(otherViewModel.getLevel(), getLevel())
        && Objects.equals(otherViewModel.getParent(), getParent());
  }
}
