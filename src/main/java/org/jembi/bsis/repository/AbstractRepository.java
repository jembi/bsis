package org.jembi.bsis.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.BSISEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public abstract class AbstractRepository<T extends BSISEntity> {

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

  /**
   * Simple method to add a where clause condition to the String builder whereClause parameter. 
   * This method expects the whereClause parameter to be either blank or contain an existing valid formatted
   * SQL where clause. 
   * @param whereClause
   * @param condition - Condition statement not including the keywords 'AND" or 'WHERE'
   */
  protected void addWhereCondition(StringBuilder whereClause, String condition) {
    if (StringUtils.isBlank(whereClause)) {
      if(whereClause == null){
        throw new IllegalArgumentException("whereClause parameter cannot be null");
      }
      whereClause.append("WHERE ");
    } else {
      whereClause.append(" AND ");
    }
    whereClause.append(condition);
  }
}
