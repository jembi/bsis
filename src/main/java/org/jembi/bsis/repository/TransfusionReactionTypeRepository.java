package org.jembi.bsis.repository;

import java.util.List;
import java.util.UUID;

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

  public TransfusionReactionType findById(UUID id) throws NoResultException {
    TypedQuery<TransfusionReactionType> query = entityManager.createNamedQuery(
        TransfusionReactionTypeNamedQueryConstants.NAME_FIND_BY_ID, TransfusionReactionType.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }
  
  public boolean isUniqueTransfusionReactionTypeName(UUID id, String reactionTypeName) {
    return entityManager.createNamedQuery(TransfusionReactionTypeNamedQueryConstants.NAME_VERIFY_UNIQUE_TRANSFUSION_REACTION_TYPE_NAME,
        Boolean.class)
        .setParameter("includeId", id != null)
        .setParameter("id", id)
        .setParameter("reactionTypeName", reactionTypeName)
        .getSingleResult();
  }
}
