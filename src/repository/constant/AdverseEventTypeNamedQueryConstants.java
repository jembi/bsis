package repository.constant;

public class AdverseEventTypeNamedQueryConstants {
    
    public static final String NAME_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS = 
            "AdverseEventType.findAdverseEventTypeViewModels";
    public static final String QUERY_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS = 
            "SELECT NEW viewmodel.AdverseEventTypeViewModel(aet.id, aet.name, aet.description, aet.isDeleted) " +
            "FROM AdverseEventType aet " +
            "ORDER BY aet.name ";

    public static final String NAME_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS_WITH_DELETED_FLAG = 
            "AdverseEventType.findAdverseEventTypeViewModelsWithDeletedFlag";
    public static final String QUERY_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS_WITH_DELETED_FLAG = 
            "SELECT NEW viewmodel.AdverseEventTypeViewModel(aet.id, aet.name, aet.description, aet.isDeleted) " +
            "FROM AdverseEventType aet " +
            "WHERE aet.isDeleted = :deleted " +
            "ORDER BY aet.name ";

}
