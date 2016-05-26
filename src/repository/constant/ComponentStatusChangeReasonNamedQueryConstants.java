package repository.constant;

public class ComponentStatusChangeReasonNamedQueryConstants {

  public static final String NAME_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY =
      "ComponentStatusChangeReason.findFirstComponentStatusChangeReasonForCategory";
  public static final String QUERY_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY =
      "SELECT cscr "
      + "FROM ComponentStatusChangeReason cscr "
      + "WHERE cscr.category = :category "
      + "AND cscr.isDeleted = :deleted ";
  
}
