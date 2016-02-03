package factory;

import java.util.ArrayList;
import java.util.List;

import model.audit.AuditRevision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.UserRepository;
import viewmodel.AuditRevisionViewModel;

@Service
public class AuditRevisionViewModelFactory {

  @Autowired
  private UserRepository userRepository;

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
        viewModel.setUser(userRepository.findUser(auditRevision.getUsername()));
      }
      viewModel.setEntityModifications(auditRevision.getEntityModifications());
      viewModels.add(viewModel);
    }

    return viewModels;
  }

}
