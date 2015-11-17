package repository;

public class PostDonationCounsellingNamedQueryConstants {
    
    public static final String NAME_FIND_PREVIOUSLY_FLAGGED_POST_DONATION_COUNSELLING_FOR_DONOR =
            "PostDonationCounselling.findPreviouslyFlaggedPostDonationCounsellingForDonor";
    public static final String QUERY_FIND_FLAGGED_POST_DONATION_COUNSELLING_FOR_DONOR =
            "SELECT pdc " +
            "FROM PostDonationCounselling pdc " +
            "WHERE pdc.donation.donor.id = :donorId " +
//            "AND pdc.flaggedForCounselling = :flaggedForCounselling " +
            "ORDER BY pdc.donation.donationDate ";
    
    public static final String NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR =
            "PostDonationCounselling.countFlaggedPostDonationCounsellingsForDonor";
    public static final String QUERY_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR =
            "SELECT COUNT(pdc) " +
            "FROM PostDonationCounselling pdc " +
            "WHERE pdc.donation.donor.id = :donorId " +
            "AND pdc.flaggedForCounselling = :flaggedForCounselling ";
}
