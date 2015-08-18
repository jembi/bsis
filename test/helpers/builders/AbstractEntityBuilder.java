package helpers.builders;

import javax.persistence.EntityManager;

public abstract class AbstractEntityBuilder<T> {
    
    public abstract T build();
    
    public T buildAndPersist(EntityManager entityManager) {
        T entity = build();
        entityManager.persist(entity);
        return entity;
    }

}
