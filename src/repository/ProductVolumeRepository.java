package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.productvolume.ProductVolume;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProductVolumeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<ProductVolume> getAllProductVolumes() {
    TypedQuery<ProductVolume> query;
    query = em.createQuery("SELECT pv from ProductVolume pv where pv.isDeleted=:isDeleted", ProductVolume.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public ProductVolume getProductVolumeById(Integer productVolumeId) {
    TypedQuery<ProductVolume> query;
    query = em.createQuery("SELECT pv from ProductVolume pv " +
    		    "where pv.id=:id AND pv.isDeleted=:isDeleted", ProductVolume.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", productVolumeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllProductVolumes(List<ProductVolume> allProductVolumes) {
    for (ProductVolume pv: allProductVolumes) {
        ProductVolume existingProductVolume = getProductVolumeById(pv.getId());
        if (existingProductVolume != null) {
          existingProductVolume.setVolume(pv.getVolume());
          em.merge(existingProductVolume);
        }
        else {
          pv.setUnit("ml");
          pv.setIsDeleted(false);
          pv.setDescription("");
          em.persist(pv);
        }
    }
    em.flush();
  }
}
