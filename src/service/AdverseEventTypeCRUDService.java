package service;

import backingform.AdverseEventTypeBackingForm;
import model.adverseevent.AdverseEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.AdverseEventTypeRepository;

import javax.transaction.Transactional;

@Transactional
@Service
public class AdverseEventTypeCRUDService {

  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;

  public AdverseEventType createAdverseEventType(AdverseEventTypeBackingForm backingForm) {
    AdverseEventType adverseEventType = new AdverseEventType();
    adverseEventType.setName(backingForm.getName());
    adverseEventType.setDescription(backingForm.getDescription());
    adverseEventType.setDeleted(backingForm.getIsDeleted());
    adverseEventTypeRepository.save(adverseEventType);
    return adverseEventType;
  }

  public AdverseEventType updateAdverseEventType(Long id, AdverseEventTypeBackingForm backingForm) {
    AdverseEventType adverseEventType = adverseEventTypeRepository.findById(id);
    adverseEventType.setName(backingForm.getName());
    adverseEventType.setDescription(backingForm.getDescription());
    adverseEventType.setDeleted(backingForm.getIsDeleted());
    return adverseEventTypeRepository.update(adverseEventType);
  }
}
