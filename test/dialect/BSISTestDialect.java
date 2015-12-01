package dialect;

import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class BSISTestDialect extends HSQLDialect {

  public BSISTestDialect() {
    super();
    registerFunction("date_add_days", new SQLFunctionTemplate(StandardBasicTypes.DATE,
        "?1 + INTERVAL DAY(?2)"));
  }

}
