package repository;

import model.admin.ConfigPropertyConstants;
import model.admin.GenericConfig;
import model.bloodtesting.BloodTestContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class GenericConfigRepository {

  @PersistenceContext
  private EntityManager em;

  public Map<String, String> getConfigProperties(List<String> propertyOwners) {
    Map<String, String> configProperties = new HashMap<>();
    String queryStr = "SELECT c FROM GenericConfig c WHERE c.propertyOwner IN :propertyOwners";
    TypedQuery<GenericConfig> query = em.createQuery(queryStr, GenericConfig.class);
    query.setParameter("propertyOwners", propertyOwners);
    for (GenericConfig gc : query.getResultList()) {
      configProperties.put(gc.getPropertyName(), gc.getPropertyValue());
    }
    return configProperties;
  }

  public void updateConfigProperties(String propertyOwner, Map<String, String> params) {
    String queryStr = "SELECT c FROM GenericConfig c WHERE c.propertyOwner=:propertyOwner AND c.propertyName=:propertyName";
    for (String propertyName : params.keySet()) {
      TypedQuery<GenericConfig> query = em.createQuery(queryStr, GenericConfig.class);
      query.setParameter("propertyOwner", propertyOwner);
      query.setParameter("propertyName", propertyName);
      GenericConfig config = query.getSingleResult();
      config.setPropertyValue(params.get(propertyName));
      em.merge(config);
    }
    em.flush();
  }

  public void updateWorksheetProperties(Map<String, String> params) {
    updateConfigProperties(ConfigPropertyConstants.DONATIONS_WORKSHEET, params);
  }

  public Map<String, String> getConfigProperties(String propertyOwner) {
    Map<String, String> configProperties = new HashMap<>();
    String queryStr = "SELECT c FROM GenericConfig c WHERE c.propertyOwner = :propertyOwner";
    TypedQuery<GenericConfig> query = em.createQuery(queryStr, GenericConfig.class);
    query.setParameter("propertyOwner", propertyOwner);
    for (GenericConfig gc : query.getResultList()) {
      configProperties.put(gc.getPropertyName(), gc.getPropertyValue());
    }
    return configProperties;
  }

  public BloodTestContext getCurrentBloodTypingContext() {
    String contextStr = getConfigProperties("labsetup").get("bloodTypingContext");
    return BloodTestContext.valueOf(contextStr);
  }
}
