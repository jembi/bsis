package repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.componentprocessing.ComponentProcessing;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ComponentProcessingRepository {
	
	@PersistenceContext
  private EntityManager em;
	
	public List<ComponentProcessing> getAllComponentProcessing() {
    TypedQuery<ComponentProcessing> query;
    query = em.createQuery("SELECT b from ComponentProcessing b where b.isDeleted=:isDeleted", ComponentProcessing.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }
	
	public void saveNewComponentProcessing(Map<String, Object> newComponentProcessingAsMap) {  
		ComponentProcessing componentProcessing=new ComponentProcessing();
		componentProcessing.setIsDeleted(false);
		componentProcessing.setProductProcessed(Integer.parseInt((String) newComponentProcessingAsMap.get("productType")));
		componentProcessing.setProductSource(Integer.parseInt((String) newComponentProcessingAsMap.get("productType")));
		if(newComponentProcessingAsMap.get("unitsMax")!=null && newComponentProcessingAsMap.get("unitsMax")!=""){
			componentProcessing.setUnitsMax(Integer.parseInt((String) newComponentProcessingAsMap.get("unitsMax")));
		}else{
			componentProcessing.setUnitsMax(1);
		}
		if(newComponentProcessingAsMap.get("unitsMin")!=null && newComponentProcessingAsMap.get("unitsMin")!=""){
			componentProcessing.setUnitsMin(Integer.parseInt((String) newComponentProcessingAsMap.get("unitsMin")));
		}else{
			componentProcessing.setUnitsMin(1);
		}
		em.persist(componentProcessing);
	  }
		
	public ComponentProcessing getComponentProcessingById(Integer id) {
    TypedQuery<ComponentProcessing> query;
    query = em.createQuery("SELECT cp from ComponentProcessing cp " +
            "where cp.id=:id", ComponentProcessing.class);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }
	
	public void deactivateComponentProcessing(Integer ComponentProcessingId) {
		String updatedeactivateflag = "UPDATE ComponentProcessing cp SET cp.isDeleted=:isDeleted where cp.id=:ComponentProcessingId";
		Query query = em.createQuery(updatedeactivateflag);
		query.setParameter("ComponentProcessingId",ComponentProcessingId);
		query.setParameter("isDeleted", true);
		query.executeUpdate(); 
  }
	
	public void activateComponentProcessing(Integer ComponentProcessingId) {
		String updateactivateflag = "UPDATE ComponentProcessing cp SET cp.isDeleted=:isDeleted where cp.id=:ComponentProcessingId";
		Query query = em.createQuery(updateactivateflag);
		query.setParameter("ComponentProcessingId",ComponentProcessingId);
		query.setParameter("isDeleted", false);
		query.executeUpdate(); 
  }
	
	 public void updateComponentProcessing(Map<String, Object> newComponentProcessingAsMap) {
		 
		 
		 String updateExpiryQuery = "UPDATE ComponentProcessing cp SET cp.productProcessed=:productProcessed, productSource=:productSource, "
		 		+ "unitsMax=:unitsMax, unitsMin=:unitsMin  where cp.id=:id";
		 Query query = em.createQuery(updateExpiryQuery);
		 		
		 		query.setParameter("id", Integer.parseInt((String) newComponentProcessingAsMap.get("id")));
		 		query.setParameter("productProcessed", Integer.parseInt((String) newComponentProcessingAsMap.get("productType")));
		 		query.setParameter("productSource", Integer.parseInt((String) newComponentProcessingAsMap.get("productType")));
		 		if(newComponentProcessingAsMap.get("unitsMax")!=null && newComponentProcessingAsMap.get("unitsMax")!=""){
		 			query.setParameter("unitsMax", Integer.parseInt((String) newComponentProcessingAsMap.get("unitsMax")));
				}else{
					query.setParameter("unitsMax",1);
				}
		 		if(newComponentProcessingAsMap.get("unitsMin")!=null && newComponentProcessingAsMap.get("unitsMin")!=""){
		 			query.setParameter("unitsMin", Integer.parseInt((String) newComponentProcessingAsMap.get("unitsMin")));
				}else{
					query.setParameter("unitsMin", 1);
				}
		 		
		 		//int numUpdated = query.executeUpdate();
		 		query.executeUpdate(); 
	  }
}
