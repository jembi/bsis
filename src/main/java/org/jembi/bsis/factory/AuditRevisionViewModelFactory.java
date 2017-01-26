package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.audit.AuditRevision;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.viewmodel.AuditRevisionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditRevisionViewModelFactory {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserFactory userFactory;

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<AuditRevisionViewModel> createAuditRevisionViewModels(List<AuditRevision> auditRevisions) {
    List<AuditRevisionViewModel> viewModels = new ArrayList<>();

    for (AuditRevision auditRevision : auditRevisions) {
      AuditRevisionViewModel viewModel = new AuditRevisionViewModel();
      viewModel.setId(auditRevision.getId());
      viewModel.setRevisionDate(auditRevision.getRevisionDate());
      if (auditRevision.getUsername() != null) {
        viewModel.setUser(userFactory.createViewModel(userRepository.findUser(auditRevision.getUsername())));
      }
      viewModel.setEntityModifications(auditRevision.getEntityModifications());
      viewModels.add(viewModel);
    }

    return viewModels;
  }

}
