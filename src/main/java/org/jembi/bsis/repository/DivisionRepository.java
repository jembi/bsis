package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.constant.DivisionNamedQueryConstants;
import org.springframework.stereotype.Repository;

@Repository
public class DivisionRepository extends AbstractRepository<Division> {

  public Division findDivisionById(long id) {
    return entityManager.createNamedQuery(DivisionNamedQueryConstants.NAME_FIND_DIVISION_BY_ID, Division.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public Division findDivisionByName(String name) {
    return entityManager.createNamedQuery(DivisionNamedQueryConstants.NAME_FIND_DIVISION_BY_NAME, Division.class)
        .setParameter("name", name)
        .getSingleResult();
  }

  public List<Division> findDivisions(String name, boolean includeSimilarResults, Integer level) {
    // build up Query string
    StringBuilder queryBuilder = new StringBuilder("SELECT div FROM Division div ");

    //WHERE
    StringBuilder whereClause = new StringBuilder();
    if (!StringUtils.isBlank(name)) {
      if (includeSimilarResults) {
        addWhereCondition(whereClause, "div.name LIKE :name ");
      } else {
        addWhereCondition(whereClause, "div.name = :name ");
      }
    }

    if (level != null) {
      addWhereCondition(whereClause, "div.level = :level ");
    }
    
    if(!StringUtils.isBlank(whereClause.toString())) {
      queryBuilder.append(whereClause);
    }
    
    //ORDER BY
    queryBuilder.append("ORDER BY div.name ASC");

    TypedQuery<Division> query = entityManager.createQuery(queryBuilder.toString(), Division.class);

    //SET NAMED PARAMS
    if (!StringUtils.isBlank(name)) {
      if (includeSimilarResults) {
        query.setParameter("name", "%"+name+"%");
      } else {
        query.setParameter("name", name);
      }
    }
    
    if (level != null) {
      query.setParameter("level", level);
    }
    
    //EXECUTE QUERY
    return query.getResultList();
  }
  
  /**
   * Count the number of divisions which have the given division as their parent.
   * 
   * @param parent The parent division.
   * @return The count.
   */
  public long countDivisionsWithParent(Division parent) {
    return entityManager.createNamedQuery(DivisionNamedQueryConstants.NAME_COUNT_DIVISIONS_WITH_PARENT, Long.class)
        .setParameter("parent", parent)
        .getSingleResult();
  }

}