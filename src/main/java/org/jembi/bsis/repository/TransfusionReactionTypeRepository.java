package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.constant.TransfusionReactionTypeNamedQueryConstants;
import org.springframework.stereotype.Repository;

@Repository
public class TransfusionReactionTypeRepository extends AbstractRepository<TransfusionReactionType> {

  public List<TransfusionReactionType> getAllTransfusionReactionTypes(Boolean includeDeleted) {
    TypedQuery<TransfusionReactionType> query;
    query = entityManager.createNamedQuery(
        TransfusionReactionTypeNamedQueryConstants.NAME_GET_ALL_TRANSFUSION_REACTION_TYPES,
        TransfusionReactionType.class);
    query.setParameter("includeDeleted", includeDeleted);
    return query.getResultList();
  }

  public TransfusionReactionType findById(Long id) throws NoResultException {
    TypedQuery<TransfusionReactionType> query = entityManager.createNamedQuery(
        TransfusionReactionTypeNamedQueryConstants.NAME_FIND_BY_ID, TransfusionReactionType.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }
  
  public boolean isUniqueTransfusionReactionTypeName(Long id, String reactionTypeName) {
    // passing null as the ID parameter does not work because the IDs in mysql are never null. So if
    // id is null, the below rather uses -1 which achieves the same result in the case of this
    // query.
    return entityManager.createNamedQuery(TransfusionReactionTypeNamedQueryConstants.NAME_VERIFY_UNIQUE_TRANSFUSION_REACTION_TYPE_NAME,
        Boolean.class)
        .setParameter("id", id != null ? id : -1L)
        .setParameter("reactionTypeName", reactionTypeName)
        .getSingleResult();
  }
}
