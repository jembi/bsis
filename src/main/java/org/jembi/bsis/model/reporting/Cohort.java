package org.jembi.bsis.model.reporting;

import java.util.Objects;

public class Cohort {

  private String category;
  private Object option;
  private Comparator comparator;

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Object getOption() {
    return option;
  }

  public void setOption(Object option) {
    this.option = option;
  }

  public Comparator getComparator() {
    return comparator;
  }

  public void setComparator(Comparator comparator) {
    this.comparator = comparator;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Cohort)) {
      return false;
    }

    Cohort other = (Cohort) obj;

    return Objects.equals(getCategory(), other.getCategory()) &&
        Objects.equals(getOption(), other.getOption()) &&
        Objects.equals(getComparator(), other.getComparator());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCategory(), getOption(), getComparator());
  }

}
