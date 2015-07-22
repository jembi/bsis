package repository;
import model.productmovement.ProductStatusChangeReason;

import model.productmovement.ProductStatusChangeReasonCategory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional

public class DiscardReasonRepository {

    @PersistenceContext
    private EntityManager em;

    public List <ProductStatusChangeReason> getAllDiscardReasons (){
        TypedQuery <ProductStatusChangeReason> query;
        query = em.createQuery("SELECT p from ProductStatusChangeReason p WHERE p.category = :category", ProductStatusChangeReason.class);
        query.setParameter("category", ProductStatusChangeReasonCategory.DISCARDED);
        return query.getResultList();
    }

    public ProductStatusChangeReason findDiscardReason(String reason){
        String queryString = "SELECT p FROM ProductStatusChangeReason p WHERE p.statusChangeReason = :reason AND p.category = :category";
        TypedQuery<ProductStatusChangeReason> query = em.createQuery(queryString, ProductStatusChangeReason.class);
        query.setParameter("reason", reason);
        query.setParameter("category", ProductStatusChangeReasonCategory.DISCARDED);
        ProductStatusChangeReason result = null;
        try{
            result = query.getSingleResult();
        }catch(NoResultException ex){}
        return result;
    }

    public ProductStatusChangeReason getDiscardReasonById(Integer DiscardReasonId) {
        TypedQuery<ProductStatusChangeReason> query;
        query = em.createQuery("SELECT p from ProductStatusChangeReason p " +
                "where p.id=:id AND p.category= :category", ProductStatusChangeReason.class);
        query.setParameter("id", DiscardReasonId);
        query.setParameter("category", ProductStatusChangeReasonCategory.DISCARDED);
        if (query.getResultList().size() == 0)
            return null;
        return query.getSingleResult();
    }

    public void saveAllDiscardReasons(List<ProductStatusChangeReason> allDiscardReasons) {
        for (ProductStatusChangeReason dr: allDiscardReasons) {
            ProductStatusChangeReason existingDiscardReason = getDiscardReasonById(dr.getId());
            if (existingDiscardReason != null) {
                existingDiscardReason.setStatusChangeReason(dr.getStatusChangeReason());
                em.merge(existingDiscardReason);
            }
            else {
                em.persist(dr);
            }
        }
        em.flush();
    }

    public ProductStatusChangeReason saveDiscardReason(ProductStatusChangeReason deferralReason){
        em.persist(deferralReason);
        em.flush();
        return deferralReason;
    }

    public ProductStatusChangeReason updateDiscardReason(ProductStatusChangeReason deferralReason){
        ProductStatusChangeReason existingDiscardReason = getDiscardReasonById(deferralReason.getId());
        if (existingDiscardReason == null) {
            return null;
        }
        existingDiscardReason.copy(deferralReason);
        em.merge(existingDiscardReason);
        em.flush();
        return existingDiscardReason;
    }
}