package repository;

public class ComponentNamedQueryConstants {
    
    public static final String NAME_UPDATE_COMPONENT_STATUS_FOR_DONOR =
            "Component.updateComponentStatusForDonor";
    public static final String QUERY_UPDATE_COMPONENT_STATUS_FOR_DONOR =
            "UPDATE Component c " +
            "SET c.status = :newStatus " +
            "WHERE c.status = :oldStatus " +
            "AND c.donation IN (" +
            "  SELECT d " +
            "  FROM Donation d " +
            "  WHERE d.donor = :donor " +
            ") ";

}
