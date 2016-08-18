package org.jembi.bsis.viewmodel;

import java.util.Objects;

public class DivisionViewModel {
  
  private long id;
  private String name;
  private int level;
  private DivisionViewModel parentDivision;
   
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

  public DivisionViewModel getParentDivision() {
    return parentDivision;
  }

  public void setParentDivision(DivisionViewModel parentDivision) {
    this.parentDivision = parentDivision;
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
        && Objects.equals(otherViewModel.getParentDivision(), getParentDivision());
  }
}
