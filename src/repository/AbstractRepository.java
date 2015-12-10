package repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public abstract class AbstractRepository<T> {

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
