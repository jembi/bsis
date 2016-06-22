package org.jembi.bsis.repository.constant;

public class AdverseEventTypeNamedQueryConstants {

  public static final String NAME_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS =
      "AdverseEventType.findAdverseEventTypeViewModels";
  public static final String QUERY_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS =
      "SELECT NEW org.jembi.bsis.viewmodel.AdverseEventTypeViewModel(aet.id, aet.name, aet.description, aet.isDeleted) " +
          "FROM AdverseEventType aet " +
          "ORDER BY case when aet.name = 'Other' then 1 else 0 end, aet.name ";

  public static final String NAME_FIND_ADVERSE_EVENT_TYPE_IDS_BY_NAME =
      "AdverseEventType.findAdverseEventTypeIdsByName";
  public static final String QUERY_FIND_ADVERSE_EVENT_TYPE_IDS_BY_NAME =
      "SELECT aet.id " +
          "FROM AdverseEventType aet " +
          "WHERE aet.name = :name ";


  public static final String NAME_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS_WITH_DELETED_FLAG =
      "AdverseEventType.findAdverseEventTypeViewModelsWithDeletedFlag";
  public static final String QUERY_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS_WITH_DELETED_FLAG =
      "SELECT NEW org.jembi.bsis.viewmodel.AdverseEventTypeViewModel(aet.id, aet.name, aet.description, aet.isDeleted) " +
          "FROM AdverseEventType aet " +
          "WHERE aet.isDeleted = :deleted " +
          "ORDER BY case when aet.name = 'Other' then 1 else 0 end, aet.name  ";

}
