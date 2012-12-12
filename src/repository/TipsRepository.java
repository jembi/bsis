package repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.location.Location;
import model.tips.Tips;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TipsRepository {

  @PersistenceContext
  private EntityManager em;

  public TipsRepository() {
  }

  public Tips findTipsByKey(String key) {
    return em.find(Tips.class, key);
  }

  public String getTipsContent(String key) {
    Tips tips = em.find(Tips.class, key);
    if (tips == null)
      return "";
    return tips.getTipsContent();
  }

  public void updateTipsContent(String key, String newContent) {
    Tips tips = findTipsByKey(key);
    tips.setTipsContent(newContent);
    em.merge(tips);
    em.flush();
  }

  public void saveAllTips(List<Tips> allTips) {
    for (Tips tips: allTips) {
      if (tips.getTipsKey() == null) {
        em.persist(tips);
      }
      else {
        Tips existingTips = em.find(Tips.class, tips.getTipsKey());
        if (existingTips != null) {
          existingTips.setTipsContent(tips.getTipsContent());
          em.merge(existingTips);
        }
      }
    }
    em.flush();
  }

  public List<Tips> getAllTips() {
    TypedQuery<Tips> query = em
        .createQuery("SELECT t FROM Tips t", Tips.class);
    return query.getResultList();
  }
}
