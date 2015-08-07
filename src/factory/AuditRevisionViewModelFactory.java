package factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

            if (auditRevision.getModifiedEntityNames() != null) {
                // Get simple names from class names
                Set<String> simpleEntityNames = new HashSet<>();
                for (String entityName : auditRevision.getModifiedEntityNames()) {
                    String simpleName = entityName.substring(entityName.lastIndexOf('.') + 1);
                    simpleEntityNames.add(simpleName);
                }
                viewModel.setEntityNames(simpleEntityNames);
            }

            viewModels.add(viewModel);
        }
        
        return viewModels;
    }

}
