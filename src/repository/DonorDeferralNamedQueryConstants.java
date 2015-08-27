package repository;

public class DonorDeferralNamedQueryConstants {
    
    public static final String NAME_COUNT_DONOR_DEFERRALS_FOR_DONOR =
            "DonorDeferral.countDonorDeferralsForDonor";
    public static final String QUERY_COUNT_DONOR_DEFERRALS_FOR_DONOR =
            "SELECT COUNT(dd) " +
            "FROM DonorDeferral dd " +
            "WHERE dd.donor = :donor " +
            "AND dd.isVoided = :voided ";

}
