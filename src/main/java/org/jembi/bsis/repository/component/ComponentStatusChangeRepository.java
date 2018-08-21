package org.jembi.bsis.repository.component;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import javax.persistence.NoResultException;

import static org.jembi.bsis.model.component.ComponentStatus.DISCARDED;

@Repository
public class ComponentStatusChangeRepository extends AbstractRepository<ComponentStatusChange> {

  public static final String NAME_FIND_REASONS_IN_CATEGORY_FOR_COMPONENT_ORDERED_BY_DATE_DESC =
      "ComponentStatusChange.findLatestReasonInCategoryForComponentOrderedByDateDesc";
  public static final String QUERY_FIND_REASONS_IN_CATEGORY_FOR_COMPONENT_ORDERED_BY_DATE_DESC =
      "SELECT csc " +
          "FROM ComponentStatusChange csc " +
          "WHERE csc.component = :component " +
          "AND csc.newStatus = :status " +
          "AND csc.isDeleted = :deleted " +
          "ORDER BY csc.statusChangedOn DESC";

  public Optional<ComponentStatusChange> findLatestDiscardReasonForComponent(Component component) {
      try {
        return Optional.of(entityManager.createNamedQuery(NAME_FIND_REASONS_IN_CATEGORY_FOR_COMPONENT_ORDERED_BY_DATE_DESC,
            ComponentStatusChange.class)
            .setParameter("component", component)
            .setParameter("status", DISCARDED)
            .setParameter("deleted", false)
            .setMaxResults(1)
            .getSingleResult());
      } catch (NoResultException nre) {
        return Optional.empty();
      }
  }
}
