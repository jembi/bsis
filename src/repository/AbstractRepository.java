package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.BaseEntity;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public abstract class AbstractRepository<T extends BaseEntity> {
    
    @PersistenceContext
    protected EntityManager entityManager;
    
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(T entity) {
        entityManager.persist(entity);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public T update(T entity) {
        return entityManager.merge(entity);
    }

}
