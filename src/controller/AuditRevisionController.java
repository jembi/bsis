package controller;

import java.util.List;

import model.audit.AuditRevision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.AuditRevisionRepository;
import viewmodel.AuditRevisionViewModel;
import factory.AuditRevisionViewModelFactory;

@RestController
@RequestMapping("auditrevisions")
public class AuditRevisionController {
    
    @Autowired
    private AuditRevisionRepository auditRevisionRepository;
    @Autowired
    private AuditRevisionViewModelFactory auditRevisionViewModelFactory;

    public void setAuditRevisionRepository(AuditRevisionRepository auditRevisionRepository) {
        this.auditRevisionRepository = auditRevisionRepository;
    }

    public void setAuditRevisionViewModelFactory(AuditRevisionViewModelFactory auditRevisionViewModelFactory) {
        this.auditRevisionViewModelFactory = auditRevisionViewModelFactory;
    }

    // TODO: Authorisation
    @RequestMapping(method = RequestMethod.GET)
    public List<AuditRevisionViewModel> getAuditRevisions() {
        List<AuditRevision> auditRevisions = auditRevisionRepository.findRecentAuditRevisions();
        return auditRevisionViewModelFactory.createAuditRevisionViewModels(auditRevisions);
    }
}
