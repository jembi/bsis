package repository;

public class ReturnFormNamedQueryConstants {

  public static final String NAME_FIND_BY_ID = "ReturnForm.findById";
  public static final String QUERY_FIND_BY_ID =
      "SELECT r FROM ReturnForm r WHERE r.id = :id AND r.isDeleted = :isDeleted";
  
  public static final String NAME_FIND_CREATED_RETURN_FORMS = "ReturnForm.findCreatedReturnForms";
  public static final String QUERY_FIND_CREATED_RETURN_FORMS =
      "SELECT rf "
      + "FROM ReturnForm rf "
      + "WHERE rf.status = :status "
      + "AND rf.isDeleted = :deleted ";

}
