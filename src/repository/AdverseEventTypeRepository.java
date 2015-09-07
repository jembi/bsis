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
                .setParameter("deleted", false)
                .getResultList();
    }
}
