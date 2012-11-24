package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.configchange.ConfigChange;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConfigChangeRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveConfigChange(ConfigChange configChange) {
    em.persist(configChange);
    em.flush();
  }

  public ConfigChange findConfigChangeById(Long configChangeId) {
    try {
      String queryString = "SELECT c FROM ConfigChange c WHERE c.id = :configChangeId";
      TypedQuery<ConfigChange> query = em.createQuery(queryString, ConfigChange.class);
      return query.setParameter("configChangeId", configChangeId).getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public List<ConfigChange> allConfigChanges() {
    try {
      String queryString = "SELECT c FROM ConfigChange c";
      TypedQuery<ConfigChange> query = em.createQuery(queryString, ConfigChange.class);
      return query.getResultList();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public void addConfigChange(ConfigChange configChange) {
    em.persist(configChange);
    em.flush();
  }

  public boolean isFirstTimeConfig() {
    List<ConfigChange> configChanges;
    configChanges = allConfigChanges();
    if (configChanges.size() == 0)
      return true;
    else
      return false;
  }
}
