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
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }
}
