package service;

import javax.transaction.Transactional;

import model.adverseevent.AdverseEventType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.AdverseEventTypeRepository;
import backingform.AdverseEventTypeBackingForm;

@Transactional
@Service
public class AdverseEventTypeCRUDService {

    @Autowired
    private AdverseEventTypeRepository adverseEventTypeRepository;
    
    public AdverseEventType createAdverseEventType(AdverseEventTypeBackingForm backingForm) {
        AdverseEventType adverseEventType = new AdverseEventType();
        adverseEventType.setName(backingForm.getName());
        adverseEventType.setDescription(backingForm.getDescription());
        adverseEventTypeRepository.save(adverseEventType);
        return adverseEventType;
    }
}
