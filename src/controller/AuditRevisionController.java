package controller;

import java.util.List;

import model.audit.AuditRevision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import repository.AuditRevisionRepository;
import utils.PermissionConstants;
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

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_AUDIT_LOG + "')")
    public List<AuditRevisionViewModel> getAuditRevisions(@RequestParam(required = false) String search) {
        List<AuditRevision> auditRevisions;
        if (search == null) {
            auditRevisions = auditRevisionRepository.findRecentAuditRevisions();
        } else {
            auditRevisions = auditRevisionRepository.findAuditRevisionsByUser(search);
        }
        return auditRevisionViewModelFactory.createAuditRevisionViewModels(auditRevisions);
    }
}
