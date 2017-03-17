package org.jembi.bsis.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.constant.DivisionNamedQueryConstants;
import org.springframework.stereotype.Repository;

@Repository
public class DivisionRepository extends AbstractRepository<Division> {

  public Division findDivisionById(UUID id) {
    return entityManager.createNamedQuery(DivisionNamedQueryConstants.NAME_FIND_DIVISION_BY_ID, Division.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  /**
   * Finds Division by name, and returns null if there is no division with the given name.
   *
   * @param name
   * @return The division
   */
  public Division findDivisionByName(String name) {
    TypedQuery<Division> query =
        entityManager.createNamedQuery(DivisionNamedQueryConstants.NAME_FIND_DIVISION_BY_NAME, Division.class);
    query.setParameter("name", name);
    List<Division> divisions = query.getResultList();

    if (divisions.isEmpty()) {
      return null;
    }

    // there should only ever be 0 or 1 division with a given name, so if there is > 0 we can
    // safely take the first division
    return divisions.get(0);
  }

  public List<Division> findDivisions(String name, boolean includeSimilarResults, Integer level, UUID parentId) {
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
    
    if (parentId != null) {
      addWhereCondition(whereClause, "div.parent.id = :parentId ");
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
    
    if (parentId != null) {
      query.setParameter("parentId", parentId);
    }

    //EXECUTE QUERY
    return query.getResultList();
  }
  
  public List<Division> getAllDivisions() {
    TypedQuery<Division> query =
        entityManager.createNamedQuery(DivisionNamedQueryConstants.NAME_GET_ALL_DIVISIONS, Division.class);
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