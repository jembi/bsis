package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.DisplayNames;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DisplayNamesRepository {
    @PersistenceContext
    private EntityManager em;

    public void saveDisplayNames(DisplayNames displayNames) {
        em.persist(displayNames);
        em.flush();
    }

    public List<DisplayNames> getAllReportConfigs() {
        Query query = em.createQuery("SELECT dn FROM DisplayNames dn");
        return query.getResultList();
    }


    public DisplayNames getDisplayName(String formType) {
        return em.find(DisplayNames.class, formType);
    }

    public DisplayNames updateDisplayNames(String formType, String fieldNames) {
        DisplayNames existingDisplayNames = em.find(DisplayNames.class, formType);
        existingDisplayNames.setFieldNames(fieldNames);
        em.merge(existingDisplayNames);
        em.flush();
        return existingDisplayNames;
    }
}
