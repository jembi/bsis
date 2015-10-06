package repository;

public class ComponentNamedQueryConstants {
    
    public static final String NAME_COUNT_CHANGED_COMPONENTS_FOR_DONATION =
            "Component.countChangedComponentsForDonation";
    public static final String QUERY_COUNT_CHANGED_COMPONENTS_FOR_DONATION =
            "SELECT COUNT(c) " +
            "FROM Component c " +
            "WHERE c.donation.id = :donationId " +
            "AND c.isDeleted = :deleted " +
            // Has a parent component
            "AND (c.parentComponent IS NOT NULL " +
            // Has a status other than the initial status
            " OR c.status != :initialStatus " +
            // Has status changes
            " OR c.statusChanges IS NOT EMPTY) ";
    
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
    
    public static final String NAME_UPDATE_COMPONENT_STATUS_FOR_DONATION =
            "Component.updateComponentStatusForDonation";
    public static final String QUERY_UPDATE_COMPONENT_STATUS_FOR_DONATION =
            "UPDATE Component c " +
            "SET c.status = :newStatus " +
            "WHERE c.status = :oldStatus " +
            "AND c.donation = :donation ";

}
