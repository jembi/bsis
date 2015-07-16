package repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import model.bloodbagtype.BloodBagType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodBagTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<BloodBagType> getAllBloodBagTypes() {
    TypedQuery<BloodBagType> query;
    query = em.createQuery("SELECT b from BloodBagType b where b.isDeleted=:isDeleted", BloodBagType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }
  
  public BloodBagType findBloodBagTypeByName(String bloodBagType){
	String queryString = "SELECT b FROM BloodBagType b WHERE b.bloodBagType = :bloodBagTypeName";
	TypedQuery<BloodBagType> query = em.createQuery(queryString, BloodBagType.class);
	query.setParameter("bloodBagTypeName", bloodBagType);
	BloodBagType result = null;
      try{
    	  result = query.getSingleResult();
      }catch(NoResultException ex){}
      return result;
  }

  public BloodBagType getBloodBagTypeById(Integer bloodBagTypeId) {
    TypedQuery<BloodBagType> query;
    query = em.createQuery("SELECT b from BloodBagType b " +
            "where b.id=:id AND b.isDeleted=:isDeleted", BloodBagType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", bloodBagTypeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllBloodBagTypes(List<BloodBagType> allBloodBagTypes) {
      for (BloodBagType bt: allBloodBagTypes) {
        BloodBagType existingBloodBagType = getBloodBagTypeById(bt.getId());
        if (existingBloodBagType != null) {
          existingBloodBagType.setBloodBagType(bt.getBloodBagType());
          em.merge(existingBloodBagType);
        }
        else {
          em.persist(bt);
        }
    }
    em.flush();
  }
  
  public BloodBagType saveBloodBagType(BloodBagType packType){
      em.persist(packType);
      em.flush();
      return packType;
  }
  
  public BloodBagType updateBloodBagType(BloodBagType packType){
	  BloodBagType existingPackType = getBloodBagTypeById(packType.getId());
      if (existingPackType == null) {
          return null;
      }
      existingPackType.copy(packType);
      em.merge(existingPackType);
      em.flush();
      return existingPackType;
  }
}
