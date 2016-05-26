package repository;

public class OrderFormNamedQueryConstants {

  public static final String NAME_FIND_BY_ID = "OrderForm.findById";
  public static final String QUERY_FIND_BY_ID =
      "SELECT o FROM OrderForm o WHERE o.id = :id AND o.isDeleted = :isDeleted";
  
  public static final String NAME_FIND_BY_STATUS = "OrderForm.findByStatus";
  public static final String QUERY_FIND_BY_STATUS =
      "SELECT o FROM OrderForm o WHERE o.status = :status AND o.isDeleted = :isDeleted";
  
}
