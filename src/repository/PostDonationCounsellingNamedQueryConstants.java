package repository;

public class PostDonationCounsellingNamedQueryConstants {
    
    public static final String NAME_FIND_PREVIOUSLY_FLAGGED_POST_DONATION_COUNSELLING_FOR_DONOR =
            "PostDonationCounselling.findPreviouslyFlaggedPostDonationCounsellingForDonor";
    public static final String QUERY_FIND_FLAGGED_POST_DONATION_COUNSELLING_FOR_DONOR =
            "SELECT pdc " +
            "FROM PostDonationCounselling pdc " +
            "WHERE pdc.donation.donor.id = :donorId " +
            "AND pdc.isDeleted = :isDeleted " +
//            "AND pdc.flaggedForCounselling = :flaggedForCounselling " +
            "ORDER BY pdc.donation.donationDate ";
    
    public static final String NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR =
            "PostDonationCounselling.countFlaggedPostDonationCounsellingsForDonor";
    public static final String QUERY_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR =
            "SELECT COUNT(pdc) " +
            "FROM PostDonationCounselling pdc " +
            "WHERE pdc.donation.donor.id = :donorId " +
            "AND pdc.isDeleted = :isDeleted " +
            "AND pdc.flaggedForCounselling = :flaggedForCounselling ";
    
    public static final String NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONATION =
        "PostDonationCounselling.findPostDonationCounsellingForDonation";
    public static final String QUERY_FIND_POST_DONATION_COUNSELLING_FOR_DONATION =
        "SELECT pdc " +
        "FROM PostDonationCounselling pdc " +
        "WHERE pdc.donation = :donation " +
        "AND pdc.isDeleted = :isDeleted ";
}
