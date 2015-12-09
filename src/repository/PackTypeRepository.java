package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.packtype.PackType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PackTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<PackType> getAllPackTypes() {
    TypedQuery<PackType> query;
    query = em.createQuery("SELECT b from PackType b", PackType.class);
    return query.getResultList();
  }

  public List<PackType> getAllEnabledPackTypes() {
    TypedQuery<PackType> query;
    query = em.createQuery("SELECT b from PackType b where b.isDeleted=:isDeleted", PackType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }
  
  public PackType findPackTypeByName(String packType){
	String queryString = "SELECT b FROM PackType b WHERE b.packType = :packTypeName";
	TypedQuery<PackType> query = em.createQuery(queryString, PackType.class);
	query.setParameter("packTypeName", packType);
	PackType result = null;
      try{
    	  result = query.getSingleResult();
      }catch(NoResultException ex){}
      return result;
  }

  public PackType getPackTypeById(Integer packTypeId) {
    TypedQuery<PackType> query;
    query = em.createQuery("SELECT b from PackType b " +
            "where b.id=:id", PackType.class);

    query.setParameter("id", packTypeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllPackTypes(List<PackType> allPackTypes) {
      for (PackType bt: allPackTypes) {
        PackType existingPackType = getPackTypeById(bt.getId());
        if (existingPackType != null) {
          existingPackType.setPackType(bt.getPackType());
          em.merge(existingPackType);
        }
        else {
          em.persist(bt);
        }
    }
    em.flush();
  }
  
  public PackType savePackType(PackType packType){
      em.persist(packType);
      em.flush();
      return packType;
  }
  
  public PackType updatePackType(PackType packType){
	  PackType existingPackType = getPackTypeById(packType.getId());
      if (existingPackType == null) {
          return null;
      }
      existingPackType.copy(packType);
      em.merge(existingPackType);
      em.flush();
      return existingPackType;
  }
}
