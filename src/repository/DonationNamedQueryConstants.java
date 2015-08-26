package repository;

public class DonationNamedQueryConstants {
    
    public static final String NAME_COUNT_DONATIONS_FOR_DONOR =
            "Donation.countDonationsForDonor";
    public static final String QUERY_COUNT_DONATION_FOR_DONOR =
            "SELECT COUNT(d) " +
            "FROM Donation d " +
            "WHERE d.donor = :donor ";

}
