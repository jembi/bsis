package org.jembi.bsis.repository;

public class OrderFormNamedQueryConstants {

  public static final String NAME_FIND_BY_ID = "OrderForm.findById";
  public static final String QUERY_FIND_BY_ID =
      "SELECT o FROM OrderForm o WHERE o.id = :id AND o.isDeleted = :isDeleted";

  public static final String NAME_FIND_BY_COMPONENT = "OrderForm.findByComponent";
  public static final String QUERY_FIND_BY_COMPONENT =
      "SELECT o FROM OrderForm o " +
      "INNER JOIN o.components c " +
      "WHERE c.id = :componentId " +
      "AND o.isDeleted = :isDeleted";

  public static final String NAME_FIND_ORDER_FORMS = "OrderForm.findOrderForms";
  public static final String QUERY_FIND_ORDER_FORMS =
      "SELECT o FROM OrderForm o " +
      "WHERE o.isDeleted=:isDeleted " +
      "AND (:orderDateFrom is null OR o.orderDate >= :orderDateFrom) " +
      "AND (:orderDateTo is null OR o.orderDate <= :orderDateTo) " +
      "AND (:includeAllDispatchFromLocations is true OR o.dispatchedFrom.id = :dispatchedFromId) " +
      "AND (:includeAllDispatchToLocations is true OR o.dispatchedTo.id = :dispatchedToId) " +
      "AND (:includeAllStatuses is true OR o.status = :status) " +
      "AND (:incudeAllTypes is true OR o.type = :type) " +
      "ORDER BY o.orderDate DESC";
  
  public static final String NAME_FIND_BLOOD_UNITS_ORDERED =
      "OrderForm.findBloodUnitsOrdered";
  public static final String QUERY_FIND_BLOOD_UNITS_ORDERED =
      "SELECT NEW org.jembi.bsis.dto.BloodUnitsOrderDTO(ofi.componentType, o.dispatchedFrom, o.type, SUM(ofi.numberOfUnits)) " +
      "FROM OrderFormItem ofi, OrderForm o " +
      "WHERE ofi.orderForm = o " +
      "AND o.status = :orderStatus " +
      "AND o.type IN (:orderTypes) " +
      "AND o.isDeleted = :orderDeleted " +
      "AND o.orderDate BETWEEN :startDate AND :endDate " +
      "GROUP BY o.dispatchedFrom, ofi.componentType, o.type " +
      "ORDER BY o.dispatchedFrom, ofi.componentType ";
  
  
  public static final String NAME_FIND_BLOOD_UNITS_ISSUED =
      "OrderForm.findBloodUnitsIssued";
  public static final String QUERY_FIND_BLOOD_UNITS_ISSUED =
      "SELECT NEW org.jembi.bsis.dto.BloodUnitsOrderDTO(c.componentType, o.dispatchedFrom, o.type, COUNT(c)) " +
      "FROM OrderForm o " +
      "INNER JOIN o.components c " +
      "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
      "AND o.status = :orderStatus " +
      "AND o.type IN (:orderTypes) " +
      "AND o.isDeleted = :orderDeleted " +
      "GROUP BY o.dispatchedFrom, c.componentType, o.type " +
      "ORDER BY o.dispatchedFrom, c.componentType ";

  public static final String NAME_IS_COMPONENT_IN_ANOTHER_ORDER_FORM =
      "OrderForm.isComponentInAnotherOrderForm";
  public static final String QUERY_IS_COMPONENT_IN_ANOTHER_ORDER_FORM =
      "SELECT count(o) > 0 " +
          "FROM OrderForm o " +
          "INNER JOIN o.components c " +
          "WHERE (:includeId = false OR o.id <> :id) " +
          "AND c.id = :componentId " +
          "AND o.status = :orderStatus " +
          "AND o.isDeleted = :isDeleted";
}
