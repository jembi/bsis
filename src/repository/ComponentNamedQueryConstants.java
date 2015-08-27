package repository;

public class ComponentNamedQueryConstants {
    
    public static final String NAME_COUNT_COMPONENTS_FOR_DONATION =
            "Component.countComponentsForDonation";
    public static final String QUERY_COUNT_COMPONENTS_FOR_DONATION =
            "SELECT COUNT(c) " +
            "FROM Component c " +
            "WHERE c.donation.id = :donationId " +
            "AND c.isDeleted = :deleted ";

}
