package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;

public class CohortBuilder extends AbstractBuilder<Cohort> {

  private String category;
  private Comparator comparator;
  private Object option;

  public CohortBuilder withCategory(String category) {
    this.category = category;
    return this;
  }

  public CohortBuilder withComparator(Comparator comparator) {
    this.comparator = comparator;
    return this;
  }

  public CohortBuilder withOption(Object option) {
    this.option = option;
    return this;
  }

  @Override
  public Cohort build() {
    Cohort cohort = new Cohort();
    cohort.setCategory(category);
    cohort.setComparator(comparator);
    cohort.setOption(option);
    return cohort;
  }

  public static CohortBuilder aCohort() {
    return new CohortBuilder();
  }

}
