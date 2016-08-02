package org.jembi.bsis.repository.constant;

public class ComponentStatusChangeReasonNamedQueryConstants {

  public static final String NAME_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY =
      "ComponentStatusChangeReason.findFirstComponentStatusChangeReasonForCategory";
  public static final String QUERY_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY =
      "SELECT cscr "
      + "FROM ComponentStatusChangeReason cscr "
      + "WHERE cscr.category = :category "
      + "AND cscr.isDeleted = :deleted ";
  
  public static final String NAME_COUNT_DISCARD_REASON_WITH_ID = "ComponentStatusChangeReason.countDiscardReasonWithId";
  public static final String QUERY_COUNT_DISCARD_REASON_WITH_ID =
      "SELECT count(*) FROM ComponentStatusChangeReason c WHERE c.id=:id AND c.category= :category AND c.isDeleted = false";
  
  public static final String NAME_FIND_COMPONENT_STATUS_CHANGE_REASON_BY_CATEGORY_AND_TYPE =
      "ComponentStatusChangeReason.findComponentStatusChangeReasonByCategoryAndType";
  public static final String QUERY_FIND_COMPONENT_STATUS_CHANGE_REASON_BY_CATEGORY_AND_TYPE = 
      "SELECT cscr "
          + "FROM ComponentStatusChangeReason cscr "
          + "WHERE cscr.category = :category "
          + "AND cscr.type = :type "
          + "AND cscr.isDeleted = :deleted ";

}
