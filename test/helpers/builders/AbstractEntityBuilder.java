package helpers.builders;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.DefaultPersister;

import javax.persistence.EntityManager;

import model.BaseEntity;

public abstract class AbstractEntityBuilder<T extends BaseEntity> extends AbstractBuilder<T> {
    
    public AbstractEntityPersister<T> getPersister() {
        return new DefaultPersister<>();
    }
    
    public T buildAndPersist(EntityManager entityManager) {
        T entity = build();
        return getPersister().deepPersist(entity, entityManager);
    }

}
