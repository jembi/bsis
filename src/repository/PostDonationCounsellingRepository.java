package repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.counselling.PostDonationCounselling;
import model.donation.Donation;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PostDonationCounsellingRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public PostDonationCounselling findById(long id) {
        return entityManager.find(PostDonationCounselling.class, id);
    }
    
    public void save(PostDonationCounselling postDonationCounselling) {
        entityManager.persist(postDonationCounselling);
    }
    
    public PostDonationCounselling update(PostDonationCounselling postDonationCounselling) {
        return entityManager.merge(postDonationCounselling);
    }
    
    public List<Donation> findDonationsFlaggedForCounselling(Date startDate, Date endDate, Set<Long> venueIds) {
        
        StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT DISTINCT(pdc.donation) ")
                .append("FROM PostDonationCounselling pdc ")
                .append("WHERE pdc.flaggedForCounselling = :flaggedForCounselling ");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("flaggedForCounselling", true);
        
        if (startDate != null) {
            queryBuilder.append("AND pdc.donation.donationDate >= :startDate ");
            parameters.put("startDate", startDate);
        }
        
        if (endDate != null) {
            queryBuilder.append("AND pdc.donation.donationDate <= :endDate ");
            parameters.put("endDate", endDate);
        }
        
        if (venueIds != null && !venueIds.isEmpty()) {
            queryBuilder.append("AND pdc.donation.venue.id IN :venueIds ");
            parameters.put("venueIds", venueIds);
        }
        
        TypedQuery<Donation> query = entityManager.createQuery(queryBuilder.toString(), Donation.class);
        for (Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }
    
    public PostDonationCounselling findPreviouslyFlaggedPostDonationCounsellingForDonor(Long donorId) throws NoResultException {

        return entityManager.createNamedQuery(
                PostDonationCounsellingNamedQueryConstants.NAME_FIND_PREVIOUSLY_FLAGGED_POST_DONATION_COUNSELLING_FOR_DONOR,
                PostDonationCounselling.class)
                .setParameter("donorId", donorId)
                .setMaxResults(1)
                .getSingleResult();
    }
    
    public int countFlaggedPostDonationCounsellingsForDonor(Long donorId) {

        return entityManager.createNamedQuery(
                PostDonationCounsellingNamedQueryConstants.NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR,
                Number.class)
                .setParameter("donorId", donorId)
                .setParameter("flaggedForCounselling", true)
                .getSingleResult()
                .intValue();
    }

}
