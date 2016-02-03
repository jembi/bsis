package repository;

import java.util.List;

import model.adverseevent.AdverseEventType;

import org.springframework.stereotype.Repository;

import repository.constant.AdverseEventTypeNamedQueryConstants;
import viewmodel.AdverseEventTypeViewModel;

@Repository
public class AdverseEventTypeRepository extends AbstractRepository<AdverseEventType> {

  public List<AdverseEventTypeViewModel> findAdverseEventTypeViewModels() {

    return entityManager.createNamedQuery(
        AdverseEventTypeNamedQueryConstants.NAME_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS,
        AdverseEventTypeViewModel.class)
        .getResultList();
  }

  public AdverseEventType findById(Long id) {
    return entityManager.find(AdverseEventType.class, id);
  }

  public List<Long> findIdsByName(String name) {
    return entityManager.createNamedQuery(
        AdverseEventTypeNamedQueryConstants.NAME_FIND_ADVERSE_EVENT_TYPE_IDS_BY_NAME,
        Long.class)
        .setParameter("name", name)
        .getResultList();
  }

  public List<AdverseEventTypeViewModel> findNonDeletedAdverseEventTypeViewModels() {

    return entityManager.createNamedQuery(
        AdverseEventTypeNamedQueryConstants.NAME_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS_WITH_DELETED_FLAG,
        AdverseEventTypeViewModel.class)
        .setParameter("deleted", false)
        .getResultList();
  }
}
