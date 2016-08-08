package org.jembi.bsis.repository;

public class OrderFormNamedQueryConstants {

  public static final String NAME_FIND_BY_ID = "OrderForm.findById";
  public static final String QUERY_FIND_BY_ID =
      "SELECT o FROM OrderForm o WHERE o.id = :id AND o.isDeleted = :isDeleted";
  
  public static final String NAME_FIND_BLOOD_UNITS_ORDERED =
      "OrderForm.findBloodUnitsOrdered";
  public static final String QUERY_FIND_BLOOD_UNITS_ORDERED =
      "SELECT NEW org.jembi.bsis.dto.BloodUnitsOrderDTO(ofi.componentType, o.dispatchedFrom, SUM(ofi.numberOfUnits)) " +
      "FROM OrderFormItem ofi, OrderForm o " +
      "WHERE ofi.orderForm = o AND o.status = :orderStatus AND o.type = :orderType " +
      "AND o.orderDate BETWEEN :startDate AND :endDate " +
      "GROUP BY o.dispatchedFrom, ofi.componentType " +
      "ORDER BY o.dispatchedFrom, ofi.componentType ";
  
  
  public static final String NAME_FIND_BLOOD_UNITS_ISSUED =
      "OrderForm.findBloodUnitsIssued";
  public static final String QUERY_FIND_BLOOD_UNITS_ISSUED =
      "SELECT NEW org.jembi.bsis.dto.BloodUnitsOrderDTO(c.componentType, o.dispatchedFrom, COUNT(c)) " +
      "FROM OrderForm o " +
      "INNER JOIN o.components c " +
      "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
      "AND o.status = :orderStatus " +
      "AND o.type = :orderType " +
      "GROUP BY o.dispatchedFrom, c.componentType " +
      "ORDER BY o.dispatchedFrom, c.componentType ";

}
