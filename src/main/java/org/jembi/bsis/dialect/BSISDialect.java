package org.jembi.bsis.dialect;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class BSISDialect extends MySQL5InnoDBDialect {

  public BSISDialect() {
    super();
    registerFunction("date_add_days", new SQLFunctionTemplate(StandardBasicTypes.DATE,
        "?1 + INTERVAL ?2 DAY"));
  }

}
