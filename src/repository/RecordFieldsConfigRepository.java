package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.RecordFieldsConfig;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RecordFieldsConfigRepository {
    @PersistenceContext
    private EntityManager em;

    public void saveRecordFieldsConfig(RecordFieldsConfig recordFieldsConfig) {
        em.persist(recordFieldsConfig);
        em.flush();
    }

    public List<RecordFieldsConfig> getAllRecordFieldsConfigs() {
        Query query = em.createQuery("SELECT rc FROM RecordFieldsConfig rc");
        return query.getResultList();
    }


    public RecordFieldsConfig getRecordFieldsConfig(String recordType) {
        return em.find(RecordFieldsConfig.class, recordType);
    }

    public RecordFieldsConfig updateRecordFieldsConfig(String recordType, String fieldNames) {
        RecordFieldsConfig existingRecordFieldsConfig = em.find(RecordFieldsConfig.class, recordType);
        existingRecordFieldsConfig.setFieldNames(fieldNames);
        em.merge(existingRecordFieldsConfig);
        em.flush();
        return existingRecordFieldsConfig;
    }
}
