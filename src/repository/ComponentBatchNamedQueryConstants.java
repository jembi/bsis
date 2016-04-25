package repository;

public class ComponentBatchNamedQueryConstants {

  public static final String NAME_FIND_COMPONENT_BATCHES_BY_STATUS =
      "ComponentBatch.findByStatus";
  public static final String QUERY_FIND_COMPONENT_BATCHES_BY_STATUS =
      "SELECT cb " +
          "FROM ComponentBatch cb " +
          "WHERE cb.status IN :statuses " +
          "AND cb.isDeleted = :isDeleted ";
  
  public static final String NAME_FIND_COMPONENT_BATCH_BY_ID =
      "ComponentBatch.findById";
  public static final String QUERY_FIND_COMPONENT_BATCH_BY_ID =
      "SELECT cb " +
          "FROM ComponentBatch cb " +
          "WHERE cb.id = :id " +
          "AND cb.isDeleted = :isDeleted ";
}
