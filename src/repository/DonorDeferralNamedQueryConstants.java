package repository;

public class DonorDeferralNamedQueryConstants {
    
    public static final String NAME_COUNT_DONOR_DEFERRALS_FOR_DONOR =
            "DonorDeferral.countDonorDeferralsForDonor";
    public static final String QUERY_COUNT_DONOR_DEFERRALS_FOR_DONOR =
            "SELECT COUNT(dd) " +
            "FROM DonorDeferral dd " +
            "WHERE dd.deferredDonor = :donor " +
            "AND dd.isVoided = :voided ";
    public static final String QUERY_FIND_DONOR_DEFERRAL_BY_ID = 
            "SELECT dd FROM DonorDeferral dd " + 
            "LEFT JOIN FETCH dd.deferredDonor " +
    		"WHERE dd.id = :donorDeferralId " +
            "AND dd.isVoided = :voided";
}
