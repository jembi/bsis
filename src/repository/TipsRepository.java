package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
