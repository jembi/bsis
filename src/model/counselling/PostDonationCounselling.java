package model.counselling;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import model.donation.Donation;
import repository.PostDonationCounsellingNamedQueryConstants;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import constraintvalidator.DonationExists;

@NamedQueries({
    @NamedQuery(name = PostDonationCounsellingNamedQueryConstants.NAME_FIND_PREVIOUSLY_FLAGGED_POST_DONATION_COUNSELLING_FOR_DONOR,
            query = PostDonationCounsellingNamedQueryConstants.QUERY_FIND_FLAGGED_POST_DONATION_COUNSELLING_FOR_DONOR),
    @NamedQuery(name = PostDonationCounsellingNamedQueryConstants.NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR,
            query = PostDonationCounsellingNamedQueryConstants.QUERY_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR),
    @NamedQuery(name = PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONATION,
            query = PostDonationCounsellingNamedQueryConstants.QUERY_FIND_POST_DONATION_COUNSELLING_FOR_DONATION)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class PostDonationCounselling {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @DonationExists
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Donation donation;

    @Column(nullable = false)
    private boolean flaggedForCounselling;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = true)
    private CounsellingStatus counsellingStatus;

    @Column(nullable = true)
    private Date counsellingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public boolean isFlaggedForCounselling() {
        return flaggedForCounselling;
    }

    public void setFlaggedForCounselling(boolean flaggedForCounselling) {
        this.flaggedForCounselling = flaggedForCounselling;
    }

    public CounsellingStatus getCounsellingStatus() {
        return counsellingStatus;
    }

    public void setCounsellingStatus(CounsellingStatus counsellingStatus) {
        this.counsellingStatus = counsellingStatus;
    }

    public Date getCounsellingDate() {
        return counsellingDate;
    }

    public void setCounsellingDate(Date counsellingDate) {
        this.counsellingDate = counsellingDate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof PostDonationCounselling &&
                ((PostDonationCounselling) other).id == id;
    }

}
